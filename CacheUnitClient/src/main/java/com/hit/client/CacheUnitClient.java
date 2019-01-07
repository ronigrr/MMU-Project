package com.hit.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class CacheUnitClient {
	private Socket myServer;

	public CacheUnitClient() throws UnknownHostException, IOException {
		myServer = new Socket("localhost", 12345);
	}

	private String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public String send(String request) throws ClassNotFoundException, IOException {

		ObjectOutputStream writer = new ObjectOutputStream(myServer.getOutputStream());
		ObjectInputStream reader = new ObjectInputStream(myServer.getInputStream());
		LinkedList<String> listOfReturnMsgs = new LinkedList<>();

		String serverMesg = new String((String) reader.readObject());

		System.out.println(serverMesg);// hello client

		// client file path selection
		if (request.equalsIgnoreCase("s")) {
			writer.writeObject("s");
			writer.flush();
		} else {
			System.out.println(request);// request file path as string
			System.out.println(readFile(request));
			writer.writeObject(readFile(request));// send request file's content
			writer.flush();
		}

		serverMesg = (String) reader.readObject();

		while (!serverMesg.equals("-1")) {

			listOfReturnMsgs.add(serverMesg);
			System.out.println(serverMesg);
			serverMesg = (String) reader.readObject();
		}

		String returnMsg = String.join(" ", listOfReturnMsgs);

		writer.close();
		reader.close();
		myServer.close();

		return returnMsg;
	}

}
