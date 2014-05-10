package com.baselet.gwt.client;

import org.apache.log4j.Logger;

import com.baselet.control.SharedConstants;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BaseletGWT implements EntryPoint {

	Logger log = Logger.getLogger(BaseletGWT.class);

	@Override
	public void onModuleLoad() {
		SharedConstants.dev_mode = Location.getParameter("dev") != null;
		SharedConstants.program = "umlet_web";

		if (!browserSupportsLocalStorage() || !browserSupportsFileReader()) {
			if (Browser.get() == Browser.INTERNET_EXPLORER && GWT.getHostPageBaseURL().startsWith("file:")) {
				Notification.showFeatureNotSupported("You have opened this webpage from your filesystem, therefore<br/>Internet Explorer will not support some essential features (localstorage)<br/><br/>Please use another browser like Firefox or Chrome,<br/>or open this application from the web url", false);
			}
			else {
				Notification.showFeatureNotSupported("Sorry, but your browser does not support all necessary features<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false);
			}
		}
		else {
			Notification.showInfo("Loading application ... please wait ...");
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onSuccess() {
					Notification.showInfo("");
					RootLayoutPanel.get().add(new MainView());
				}

				@Override
				public void onFailure(Throwable reason) {
					Notification.showFeatureNotSupported("Cannot load application from server", false);
				}
			});
		}
	}

	private final native boolean browserSupportsFileReader() /*-{
		return typeof FileReader != "undefined";
	}-*/;

	private final boolean browserSupportsLocalStorage() {
		return Storage.getLocalStorageIfSupported() != null;
	}
}
