package com.baselet.gwt.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BaseletGWT implements EntryPoint {
	
	public void onModuleLoad() {
		RootLayoutPanel.get().add(new DrawPanel());
	}
}
