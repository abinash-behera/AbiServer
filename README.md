# AbiServer
A java based basic concurrent http server

Following the tutorial by Charles Dobson, May 2009 @ http://kcd.sytes.net/articles/simple_web_server.php

Client Server communication basics

In simple terms, the web server runs on a computer and listens or waits for a HTTP request from a client. Normally a HTTP server will listen on a TCP/IP port, typically port 80, but this can effectively be any port number over 1024. The use of ports enables one physical machine to maintain multiple connections simultaneously with many different services. The first 1024 ports are reserved for named purposes, some typical ones being 80 for HTTP, 110 for POP3 (email), 25 for SMTP (also email), 22 for SSH to name but a few. There are theoretically 65535 ports that can be used, although a computer using all of these would be perhaps a little overworked!

Socket = ( IP address, Port number)

So whenever the client or web user application communicates with the web server, it needs four important components also called as TCP Connection tuple. This tuple consists of:
1. Client/Destination/remote IP address

2. Client/destination/remote Port number

3. Source/local IP address

4. Source/local Port number

Reference - http://serverfault.com/questions/296603/understanding-ports-how-do-multiple-browser-tabs-communicate-at-the-same-time

So in short –
1) A web server listens on a single port but may serve several clients concurrently, by creating a child process for each client and establishing a TCP connection between the child process and the client.
	TCP connection is basically a tuple
A server may create several concurrently established TCP sockets with the same local port number and local IP address. They are treated as different sockets by the OS, since the remote socket address (the client IP address and/or port number) are different; i.e. they have different socket pair tuples.

2) In that way, once the server accepts the incoming connection while LISTENing it opens a new socket dedicated to that conversation and hands the processing off to something else, then goes back to LISTENing.


The above steps help in achieving concurrent availability.

The server will be built in the following three stages:

Part 1 - Server GUI - Using Java FX for building the GUI ----- Complete

Part 2 - A simple server for static resources ---------------------- In-progress

Part 3 - Enable server side scripting 

