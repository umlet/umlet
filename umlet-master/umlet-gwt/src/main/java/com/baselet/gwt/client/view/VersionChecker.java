package com.baselet.gwt.client.view;

public class VersionChecker {
	public static enum Version {
		WEB, VSCODE
	}

	public static Version GetVersion() {
		if (isVsCodeVersion()) {
			return Version.VSCODE;
		}
		return Version.WEB;

	}

	public static native boolean isVsCodeVersion() /*-{
		console.log(window.parent.vscode);
		console.log(typeof window.parent.vscode);
		if (typeof window.parent.vscode !== 'undefined') {
			return true;
		}
		return false;
	}-*/;

}
