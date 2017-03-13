package DiscoveryServer.copy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws Exception {
		// wait for connections, and process
		
		while (true) {
			Socket socket = new Socket("127.0.0.1", 2222);
			//keyboard input
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			//send message to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			//receive message from server
			InputStreamReader streamReader = new InputStreamReader(
					socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			//send message which is from keyboard to server
			out.println(in.readLine());
			//receive from proxy server
			for (int i = 0; i < 3; i++) {
				reader.readLine();
			}
			System.out.println(reader.readLine());
		}
	}
}
