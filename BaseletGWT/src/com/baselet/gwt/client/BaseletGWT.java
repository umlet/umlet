package com.baselet.gwt.client;


import com.baselet.control.NewGridElementConstants;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BaseletGWT implements EntryPoint {
	
	public void onModuleLoad() {
		NewGridElementConstants.isDevMode = Location.getParameter("dev") != null;
		NewGridElementConstants.program = "umlet_web";
		RootLayoutPanel.get().add(new MainView());
	}
}
