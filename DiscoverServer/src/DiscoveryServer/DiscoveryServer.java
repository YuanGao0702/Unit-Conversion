package DiscoveryServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles.Lookup;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.LookAndFeel;

public class DiscoveryServer {
	/*
	 * First Hashtable <String conversion,ArrayList String<ip port>> ex.
	 * <"m cm", "127.0.0.1 9999"> Second Hashtable<String ip port,
	 * String<conversion> ex.<"127.0.0.1 9999", "m cm">
	 */
	public static Hashtable<String, ArrayList<String>> Conv_Address = new Hashtable<String, ArrayList<String>>();
	public static Hashtable<String, String> Address_Conv = new Hashtable<String, String>();

	public static void process(Socket clientSocket) throws IOException {
		// open up IO streams
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		/* Write a welcome message to the client */
		out.println("**************************************************************************");
		out.println("Welcome to the Discovery server!");
		out.println("Lookup Format:(Lookup unit1 unit2)");
		out.println("Add Format:(Add unit1 unit2 xxx.xxx.xxx.xxx yyyy)");
		out.println("Remove Format:(remove xxx.xxx.xxx.xxx yyyy)");
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
		// --TODO:
		String choice[] = userInput.split(" ");
		try {
			switch (choice[0].toLowerCase()) {
			case "lookup":
				out.println(lookup(choice[1] + " " + choice[2]));
				break;
			case "add":
				out.println(add(choice[1] + " " + choice[2], choice[3] + " "
						+ choice[4]));
				break;
			case "remove":
				out.println(remove(choice[1] + " " + choice[2]));
				break;
			default:
				out.println("**************************************************************************");
				out.println("Invalid Input");
				out.println("**************************************************************************");
				out.println("Lookup Format:(lookup unit1 unit2)");
				out.println("Add Format:(add unit1 unit2 xxx.xxx.xxx.xxx yyyy)");
				out.println("Remove Format:(remove xxx.xxx.xxx.xxx yyyy)");
				out.println("**************************************************************************");
				break;
			}
		} catch (Exception e) {
			out.println("**************************************************************************");
			out.println("Invalid Input");
			out.println("**************************************************************************");
			out.println("Lookup Format:(lookup unit1 unit2)");
			out.println("Add Format:(add unit1 unit2 xxx.xxx.xxx.xxx yyyy)");
			out.println("Remove Format:(remove xxx.xxx.xxx.xxx yyyy)");
			out.println("**************************************************************************");
		}

		// close IO streams, then socket
		out.close();
		in.close();
		clientSocket.close();
	}

	private static String lookup(String conv) {
		// TODO Auto-generated method stub
		String conversion[] = conv.split(" ");
		// "m cm"-->"cm m"
		String conv1 = conversion[1] + " " + conversion[0];
		ArrayList<String> ip_port;
		// default: return first ip&port
		if (Conv_Address.get(conv) != null) {
			return Conv_Address.get(conv).get(0);
		} else if (Conv_Address.get(conv1) != null) {
			return Conv_Address.get(conv1).get(0);
		} else {
			return "None";
		}
	}

	public static String add(String conversion, String address) {
		// check whether the conversion exists
		if (!lookup(conversion).equals("None")) {
			// check whether the address exists
			if (Address_Conv.get(address) != null) {
				return "Failure: Duplication of Conversion!";
			} else {
				// add value into first Hashtable(Conv_Address)
				ArrayList<String> ip_port;
				// check "m cm" or "cm m"
				String conv[] = conversion.split(" ");
				String conv1 = conv[1] + " " + conv[0];
				if (Conv_Address.get(conversion) != null) {
					ip_port = Conv_Address.get(conversion);
					ip_port.add(address);
					Conv_Address.put(conversion, ip_port);
					// add value into second Hashtable(Address_Conv)
					Address_Conv.put(address, conversion);
				} else {
					ip_port = Conv_Address.get(conv1);
					ip_port.add(address);
					Conv_Address.put(conv1, ip_port);
					// add value into second Hashtable(Address_Conv)
					Address_Conv.put(address, conv1);
				}
				return "Success: Servers Updated!";
			}
		} else {
			// add value into first Hashtable(Conv_Address)
			ArrayList<String> ip_port = new ArrayList<String>();
			ip_port.add(address);
			Conv_Address.put(conversion, ip_port);
			// add value into second Hashtable(Address_Conv)
			Address_Conv.put(address, conversion);
			return "Success: Servers Updated!";
		}

	}

	public static String remove(String str1) {
		if (Address_Conv.get(str1) != null) {
			String conversion = Address_Conv.get(str1);
			;
			// remove from the second Hashtable(Address_Conv)
			Address_Conv.remove(str1);
			// remove from the first Hashtable(Conv_Address)
			for (int i = 0; i < Conv_Address.get(conversion).size(); i++) {
				if (Conv_Address.get(conversion).get(i).equals(str1)) {
					Conv_Address.get(conversion).remove(i);
				}
			}
			if (Conv_Address.get(conversion).isEmpty())
				Conv_Address.remove(conversion);
			return "Success: Servers Updated!";
		} else {
			return "Failture: not exists!";
		}
	}

	public static void main(String[] args) throws IOException {
		// test
		//
		int port = 1111;
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
