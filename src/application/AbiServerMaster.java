package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class AbiServerMaster implements Runnable {

	private int portNumber;
	private String rootDirectory;
	// accessed across multiple instances
	private static volatile boolean serverStatus;
	// serverSocket is used to listen for incoming requests on the network
	private ServerSocket serverSocket;

	private AbiServerMain abiServerMain;
	// the request property is used to store the incoming request as it is read
	// from the input stream, we impose a 2K limit
	StringBuffer request = new StringBuffer(2048);

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public static boolean isServerStatus() {
		return serverStatus;
	}

	public static void setServerStatus(boolean serverStatus) {
		AbiServerMaster.serverStatus = serverStatus;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public StringBuffer getRequest() {
		return request;
	}

	public void setRequest(StringBuffer request) {
		this.request = request;
	}

	/**
	 * @return the abiServerMain
	 */
	public AbiServerMain getAbiServerMain() {
		return abiServerMain;
	}

	/**
	 * @param abiServerMain
	 *            the abiServerMain to set
	 */
	public void setAbiServerMain(AbiServerMain abiServerMain) {
		this.abiServerMain = abiServerMain;
	}

	@Override
	public void run() {
		// A continuous loop to listen to the network socket
		while (true) {
			this.abiServerMain.setStatusMessage(request.toString());
		}

	}

	public void startServer(AbiServerMain abiServerMain, int port, String documentRoot) {
		setAbiServerMain(abiServerMain);
		setPortNumber(port);
		setRootDirectory(documentRoot);

		String message = "Starting server on port " + port;
		abiServerMain.setStatusMessage(message);

		try {
			this.serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverStatus = true;
		new Thread(new AbiServerMaster()).start();

	}

	public void stopServer() {

	}

}
