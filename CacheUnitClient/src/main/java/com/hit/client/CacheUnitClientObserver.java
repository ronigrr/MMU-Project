package com.hit.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.UnknownHostException;

import com.hit.view.CacheUnitView;

public class CacheUnitClientObserver implements PropertyChangeListener {

	private CacheUnitView theView;

	public CacheUnitClientObserver(CacheUnitView view) {
		this.theView = view;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		CacheUnitClient theClient = null;
		try {
			theClient = new CacheUnitClient();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		String change = evt.getPropertyName();
		String strUpdateUI = new String();

		if (change.equalsIgnoreCase("s")) {
			try {
				strUpdateUI = theClient.send("s");
				theView.updateUIData(strUpdateUI);
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		} else {
			// needs to return to UI
			try {
				strUpdateUI = theClient.send(change);
				theView.updateUIData(strUpdateUI);
			} catch (ClassNotFoundException | IOException e) {

				e.printStackTrace();
			}
		}

	}
}
