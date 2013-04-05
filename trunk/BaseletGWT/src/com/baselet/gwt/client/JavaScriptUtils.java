package com.baselet.gwt.client;


public class JavaScriptUtils {

//	public static void downloadStringAsFile(String filename, String content) {
//		if (getBrowser() == Browser.CHROME) {
//			downloadStringAsFileWithCustomFilename(filename, content);
//		}
//		else downloadStringAsFileWithoutCustomName(content);
//	}
//	
//	private static native void downloadStringAsFileWithoutCustomName(String content)/*-{
//    document.location = 'data:Application/octet-stream,' + content;
//	}-*/;
//
//	/**
//	 * uses download-attribute of anchor tag. currently only supported by chrome
//	 */
//	private static native void downloadStringAsFileWithCustomFilename(String filename, String content)/*-{
//		var a = document.createElement('a');
//		var blob = new Blob([content], {'type':'application\/octet-stream'});
//		a.href = window.URL.createObjectURL(blob);
//		a.download = filename;
//		a.click();
//	}-*/;
//	
//	private enum Browser {
//		CHROME, OTHER;
//	}
//	
//	private static Browser getBrowser() {
//		if (getUserAgent().contains("chrome")) return Browser.CHROME;
//		else return Browser.OTHER;
//	}
//
//	private static native String getUserAgent() /*-{
//	return navigator.userAgent.toLowerCase();
//	}-*/;

}
