package DistSys.team.prototype;

/******************************************************************************
 *
 *  CS 6421 - Discovery Server
 *  Compilation:  javac SimpleServer.java
 *  Execution:    java SimpleServer 
 *  Team Member:  Yuan Gao, Bei Xia
 *  Default Port: 9991
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleServer {

	public static void process(Socket clientSocket) throws IOException {
		// open up IO streams
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		/* Write a welcome message to the client */
		out.println("**************************************************************************");
		out.println("Welcome to the Simple server!");
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
		try {
			String request[] = userInput.split(" ");
			switch (request[0]) {
			case "set":
				set(request[1]);
				break;
			case "get":
				out.println(get());
				break;
			}
		} catch (Exception e) {
			out.println("Invalid Input");
		}
		// close IO streams, then socket
		out.close();
		in.close();
		clientSocket.close();
	}

	private static void set(String str1) throws UnknownHostException,
			IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("127.0.0.1", 9992);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		out.println("set "+str1);
		
		socket.close();
	}

	private static String get() throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("127.0.0.1", 9992);
		InputStreamReader streamReader = new InputStreamReader(
				socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader reader = new BufferedReader(streamReader);

		out.println("get");
		for (int i = 0; i < 3; i++) {
			out.println(reader.readLine());
		}
		String result = (reader.readLine());
		socket.close();
		return result;
	}

	public static void main(String[] args) throws Exception {

		// check if argument length is invalid
		// if(args.length != 1) {
		// System.err.println("Usage: java ConvServer port");
		// }
		// create socket
		// int port = Integer.parseInt(args[0]);
		int port = 9991;
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
