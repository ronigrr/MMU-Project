package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CLI implements Runnable {
	Scanner reader;
	PrintWriter writer;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public CLI(InputStream in, OutputStream out) {
		reader = new Scanner(new InputStreamReader(in));

	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);

	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	public void write(String string) {
		pcs.firePropertyChange(string, "", string);

	}

	@Override
	public void run() {
		System.out.println("Please enter Command:");
		System.out.println("Start - turn server on \nShutdown - turn server off");
		String input;
		while (true) {
			input = reader.nextLine();
			if (input.equalsIgnoreCase("start") || input.equalsIgnoreCase("shutdown"))
				write(input);
			else
				System.out.println("Not a valid command");
		}

	}

}
