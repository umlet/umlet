package com.baselet.gwt.client;


import com.baselet.control.NewGridElementConstants;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BaseletGWT implements EntryPoint {

	public void onModuleLoad() {
		NewGridElementConstants.isDevMode = Location.getParameter("dev") != null;
		NewGridElementConstants.program = "umlet_web";
		
		if (!browserSupportsLocalStorage() || !browserSupportsFileReader()) {
			if (browserIsIE10()) {
				Notification.showFeatureNotSupported("You have opened this webpage from your filesystem, therefore<br/>Internet Explorer 10 will not support some essential features<br/><br/>Please use another browser like Firefox or Chrome,<br/>or open this application from the web url");
			} else {
				Notification.showFeatureNotSupported("Sorry, but your browser does not support all necessary features<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+");
			}
		} else {
			RootLayoutPanel.get().add(new MainView());
		}
	}

	private final native boolean browserSupportsFileReader() /*-{
    	return typeof FileReader != "undefined";
	}-*/;

	private final native boolean browserIsIE10() /*-{
    	return navigator.appVersion.indexOf("MSIE 10") != -1;
	}-*/;
	
	private final boolean browserSupportsLocalStorage() {
		return Storage.getLocalStorageIfSupported() != null;
	}
}
