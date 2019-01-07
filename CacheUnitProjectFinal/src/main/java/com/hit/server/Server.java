package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EventListener;
import com.hit.services.CacheUnitController;

public class Server implements PropertyChangeListener, Runnable, EventListener {
	private ServerSocket myServerSocket = null;
	private String reqFromCLI;
	private boolean serverIsOn = false;
	private HandleRequest<String> rHandleRequest;
	private CacheUnitController<String> cacheUnitController = new CacheUnitController<>();

	public Server() throws IOException {
	}

	@Override
	public void run() {
		try {
			while (serverIsOn) {
				Socket someClient = myServerSocket.accept();
				rHandleRequest = new HandleRequest<String>(someClient, cacheUnitController);
				new Thread(rHandleRequest).start();
			}

		} catch (IOException e) {
			System.out.println("serever was colsed! you cant add more requsts");
			// e.printStackTrace();
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		reqFromCLI = (String) evt.getNewValue();

		// if server is already running
		if (reqFromCLI.equalsIgnoreCase("start") && serverIsOn)
			System.out.println("server is already running");

		// if start && server is closed
		else if (reqFromCLI.equalsIgnoreCase("start") && !serverIsOn) {
			System.out.println("server is starting");
			serverIsOn = true;
			try {
				myServerSocket = new ServerSocket(12345);
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(this).start();

		}

		// Shutdown && server on
		else if (reqFromCLI.equalsIgnoreCase("shutdown") && serverIsOn) {
			System.out.println("server is shutingdown.....");
			serverIsOn = false;
			try {
				myServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Shutdown && server off
		else {
			System.out.println("server is off");
		}

	}

}
