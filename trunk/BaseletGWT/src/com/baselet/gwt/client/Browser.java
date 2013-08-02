package com.baselet.gwt.client;

public enum Browser {
	INTERNET_EXPLORER("MSIE"),
	FIREFOX("Firefox"),
	CHROME("Chrome"),
	OPERA("Opera"),
	UNKNOWN("######");
	
	private String userAgent;
	
	private Browser(String userAgent) {
		this.userAgent = userAgent;
	}

	public static Browser get() {
		String currentAgent = getUserAgent();
		for (Browser b : Browser.values()) {
			if (currentAgent.contains(b.userAgent)) return b;
		}
		return UNKNOWN;
	}

	private final native static String getUserAgent() /*-{
    	return navigator.userAgent;
	}-*/;
}
