package DistSys.team.prototype;

/******************************************************************************
 *
 *  CS 6421 - Discovery Server
 *  Compilation:  javac PrintServer.java
 *  Execution:    java PrintServer 
 *  Team Member:  Yuan Gao, Bei Xia
 *  Default Port: 9993
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PrintServer {

	public static void process(Socket clientSocket) throws IOException {
		// open up IO streams
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		/* Write a welcome message to the client */
		out.println("**************************************************************************");
		out.println("Welcome to the Print server!");
		out.println("**************************************************************************");
		/* read and print the client's request */
		// readLine() blocks until the server receives a new line from client
		String userInput;
		if ((userInput = in.readLine()) == null) {
			System.out.println("Error reading message");
			out.close();
			in.close();
			clientSocket.close();
		}
		System.out.println("Received message: " + userInput);
		// --TODO: lookup function;
		out.println(userInput);

		// close IO streams, then socket
		out.close();
		in.close();
		clientSocket.close();
	}

	public static void main(String[] args) throws Exception {

		// check if argument length is invalid
		// if(args.length != 1) {
		// System.err.println("Usage: java ConvServer port");
		// }
		// create socket
		// int port = Integer.parseInt(args[0]);
		int port = 9993;
		ServerSocket serverSocket = new ServerSocket(port);
		System.err.println("Started server on port " + port);

		// wait for connections, and process
		try {
			while (true) {
				// a "blocking" call which waits until a connection is requested
				Socket clientSocket = serverSocket.accept();
				System.err.println("\nAccepted connection from client");
				process(clientSocket);
			}

		} catch (IOException e) {
			System.err.println("Connection Error");
		}
		System.exit(0);
	}
}
