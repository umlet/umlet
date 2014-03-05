package com.baselet.gwt.client.version;

import com.google.gwt.core.client.GWT;

public class BuildProperties {
	private static Build PROPERTIES = GWT.create(Build.class);

	public static String getVersionString() {
		return "Version: " + PROPERTIES.version() + "\nBuildTime: " + PROPERTIES.buildtime();
	}
}
