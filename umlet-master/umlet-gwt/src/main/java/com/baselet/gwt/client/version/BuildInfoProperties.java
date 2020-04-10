package com.baselet.gwt.client.version;

import com.google.gwt.core.client.GWT;

public class BuildInfoProperties {
	private static BuildInfo PROPERTIES = GWT.create(BuildInfo.class);

	public static String getVersionString() {
		return "Version: " + PROPERTIES.version() + "\nBuildTime: " + PROPERTIES.buildtime();
	}

	public static String getVersion() {
		return PROPERTIES.version();
	}
}
