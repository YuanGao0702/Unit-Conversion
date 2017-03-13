package DiscoveryServer.copy;

/******************************************************************************
 * 
 *
 *  CS 6421 - Simple Conversation
 *  Compilation:  javac ConvServer.java
 *  Execution:    java ConvServer 
 *  Team Member:  Yuan Gao, Bei Xia
 *  Default Port: 3333
 ******************************************************************************/

import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConvServer {

	public static void process(Socket clientSocket) throws IOException {
		// open up IO streams
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

		/* Write a welcome message to the client */
		out.println("**************************************************************************");
		out.println("Welcome to the Dollars ($) <---> Ounces (oz) Java-based conversion server!");
		out.println("Conversion: <input unit> <output unit> <input amount>($ oz XX)or(oz $ XX)");
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
		// --TODO: add your converting functions here, msg = func(userInput);
		try {
			// split the string, input, output, amount
			String str[] = userInput.split(" ");
			// dollars converse to ounces
			if (str[0].equals("$") && str[1].equals("oz"))
				out.println(Float.parseFloat(str[2]) * 20);
			// ounces converse to dollars
			else if (str[0].equals("oz") && str[1].equals("$"))
				out.println(Float.parseFloat(str[2]) / 20);
			else {
				out.println("**************************************************************************");
				out.println("Invalid Input");
				out.println("Conversion: <input unit> <output unit> <input amount>($ oz XX)or(oz $ XX)");
				out.println("**************************************************************************");
			}
		} catch (Exception e) {
			out.println("**************************************************************************");
			out.println("Invalid Input");
			out.println("Conversion: <input unit> <output unit> <input amount>($ oz XX)or(oz $ XX)");
			out.println("**************************************************************************");
		}
		// close IO streams, then socket
		out.close();
		in.close();
		clientSocket.close();
	}

	private static void start() throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("127.0.0.1", 1111);
		InputStreamReader streamReader = new InputStreamReader(
				socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader reader = new BufferedReader(streamReader);

		out.println("add $ oz 127.0.0.1 3333");
		for (int i = 0; i < 6; i++) {
			reader.readLine();
		}
		System.out.println(reader.readLine());

		Socket socket1 = new Socket("127.0.0.1", 9999);
		InputStreamReader streamReader1 = new InputStreamReader(
				socket1.getInputStream());
		PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
		BufferedReader reader1 = new BufferedReader(streamReader1);

		out1.println("add $ oz 127.0.0.1 3333");
		for (int i = 0; i < 6; i++) {
			reader1.readLine();
		}
		System.out.println(reader1.readLine());
	}

	private static void end() throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("127.0.0.1", 1111);
		InputStreamReader streamReader = new InputStreamReader(
				socket.getInputStream());
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader reader = new BufferedReader(streamReader);

		out.println("remove 127.0.0.1 3333");
		out.println(reader.readLine());

		socket.close();
		//
		Socket socket1 = new Socket("127.0.0.1", 9999);
		InputStreamReader streamReader1 = new InputStreamReader(
				socket1.getInputStream());
		PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
		BufferedReader reader1 = new BufferedReader(streamReader1);

		out1.println("remove 127.0.0.1 3333");
		out1.println(reader1.readLine());

		socket1.close();

	}

	public static void main(String[] args) throws Exception {

		// check if argument length is invalid
		// if(args.length != 1) {
		// System.err.println("Usage: java ConvServer port");
		// }
		// create socket
		// int port = Integer.parseInt(args[0]);
		int port = 3333;
		ServerSocket serverSocket = new ServerSocket(port);
		System.err.println("Started server on port " + port);
		// start server
		start();
		// wait for connections, and process
		System.out.println("type 'q' to exit");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();

		try {

			while (!input.equals("q")) {
				// a "blocking" call which waits until a connection is requested
				Socket clientSocket = serverSocket.accept();
				System.err.println("\nAccepted connection from client");
				process(clientSocket);

			}
			end();
		} catch (IOException e) {
			System.err.println("Connection Error");
			end();
		}
		System.exit(0);
		end();
	}
}
