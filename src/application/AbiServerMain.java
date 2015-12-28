package application;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class AbiServerMain extends Application {

	// singleton design
	// private AbiServerMain() {
	// } // make constructor private, so the only way
	// to access "application" is through singleton pattern

	private static AbiServerMain app;

	public static AbiServerMain getAbiServerMainInstance() {
		if (app == null)
			app = new AbiServerMain();
		return app;
	}

	private String statusMessage;

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage
	 *            the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			VBox rootVbox = (VBox) FXMLLoader.load(getClass().getResource("AbiServerGUI.fxml"));

			Scene scene = new Scene(rootVbox, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Adding nodes - Menu and GridPane
			MenuBar menuBar = new MenuBar();
			final Menu controlsMenu = new Menu("Controls");
			MenuItem startMenuItem = new MenuItem("Start Server");
			MenuItem stopMenuItem = new MenuItem("Stop Server");
			stopMenuItem.setDisable(true);// Stop server menu disabled
			MenuItem exitMenuItem = new MenuItem("Exit");
			controlsMenu.getItems().add(0, startMenuItem);
			controlsMenu.getItems().add(1, stopMenuItem);
			controlsMenu.getItems().add(2, exitMenuItem);
			final Menu aboutMenu = new Menu("About");
			final Menu helpMenu = new Menu("Help");
			menuBar.getMenus().addAll(controlsMenu, aboutMenu, helpMenu);

			GridPane rootGrid = new GridPane();
			Text header = new Text("AbiServer - A java based simple concurrent HTTP server");
			// USe CSS - scenetitle.setFont(Font.font("Tahoma",
			// FontWeight.NORMAL, 20));

			// The numbering for columns and rows in the grid starts at zero,
			// and scenetitle is added in column 0, row 0. The last two
			// arguments of the grid.add() method set the column span to 2 and
			// the row span to 1.
			rootGrid.add(header, 0, 0, 2, 1);

			Text portNumber = new Text("Port");
			rootGrid.add(portNumber, 0, 1);
			TextField portNumberField = new TextField("8080");
			rootGrid.add(portNumberField, 1, 1);

			Text serverStatus = new Text("Status");
			rootGrid.add(serverStatus, 0, 2);
			Text serverStatusField = new Text("Stopped");
			// serverStatusField.setEditable(false);
			// serverStatusField.setDisable(true);
			rootGrid.add(serverStatusField, 1, 2);

			Button rootDirectory = new Button("Select Root Directory");
			rootGrid.add(rootDirectory, 0, 3);
			Text rootDirectoryField = new Text();
			// setting text wrapping for long directory selections
			rootDirectoryField.setWrappingWidth(200);
			rootGrid.add(rootDirectoryField, 1, 3);

			ButtonBar controlButtonsBar = new ButtonBar();
			// TODO add custom images
			Image imageOk = new Image(getClass().getResourceAsStream("../images/ok.png"));
			Button start = new Button("Start", new ImageView(imageOk));

			Image imageNotOk = new Image(getClass().getResourceAsStream("../images/not.png"));
			Button stop = new Button("Stop", new ImageView(imageNotOk));
			stop.setDisable(true);// disabled by default
			controlButtonsBar.getButtons().addAll(start, stop);
			rootGrid.add(controlButtonsBar, 0, 4);

			Text consoleLog = new Text("Log");
			rootGrid.add(consoleLog, 0, 5, 2, 1);
			TextArea logTArea = new TextArea();
			logTArea.setEditable(false);
			logTArea.setWrapText(true);
			rootGrid.add(logTArea, 0, 6, 2, 1);

			rootVbox.getChildren().addAll(menuBar, rootGrid);
			primaryStage.setTitle("AbiServer - Welcome!");
			primaryStage.setScene(scene);

			start.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					String portDetails = portNumberField.getText();
					boolean startCheck = true;
					logTArea.clear();
					// Numerical check
					if (portDetails.isEmpty() || Integer.valueOf(portDetails) < 1024
							|| Integer.valueOf(portDetails) > 65535) {
						logTArea.appendText(
								"Failed to start the server.\nEnter a port number between 1024 and 65535!\n\n"
										+ "Theoretically, there are 65536 ports that can be used.\n"
										+ "The first 1024 ports are reserved.\ne.g. 80 for HTTP, 22 for SSH");
						startCheck = false;
					}
					if (rootDirectoryField.getText() == null || rootDirectoryField.getText().equals("")) {
						logTArea.appendText("Please select a valid root directory.");
						startCheck = false;
					}
					// TODO check if root directory is a valid folder
					if (startCheck) {
						logTArea.appendText("Selected root directory - " + rootDirectoryField.getText());
						logTArea.appendText("\n" + getStatusMessage());
						// starting the server master thread
						// new Thread(new AbiServerMaster()).start();
						AbiServerMaster abiServerMaster = new AbiServerMaster();
						abiServerMaster.startServer(AbiServerMain.getAbiServerMainInstance(),
								Integer.valueOf(portDetails), rootDirectoryField.getText());
						start.setDisable(true);
						stop.setDisable(false);
						startMenuItem.setDisable(true);
						stopMenuItem.setDisable(false);// Stop server menu
					}

				}
			});

			stop.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					logTArea.clear();
					logTArea.appendText("Server stopped succefully.");
					start.setDisable(false);
					stop.setDisable(true);
					startMenuItem.setDisable(false);
					stopMenuItem.setDisable(true);// Stop server menu disabled
				}
			});

			// adding the file directory selector
			rootDirectory.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					DirectoryChooser directoryChooser = new DirectoryChooser();
					File selectedRootDirectory = directoryChooser.showDialog(primaryStage);
					if (selectedRootDirectory != null) {
						rootDirectoryField.setText(selectedRootDirectory.getAbsolutePath());
					}

				}
			});

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AbiServerMain.getAbiServerMainInstance();
		launch(args);
	}
}
