package com.baselet.gwt.client.base;

public enum Browser {
	INTERNET_EXPLORER("MSIE"),
	FIREFOX("Firefox"),
	CHROME("Chrome"), // also includes CHROME_ANDROID which would have browserFilters=["Android", "Chrome"]
	OPERA("Opera"),
	ANDROID_STOCK_BROWSER("Android"), // android chrome also have "Android" in its useragent but matches CHROME before coming here
	UNKNOWN("######");

	private String[] browserFilters;

	private Browser(String... browserFilters) {
		this.browserFilters = browserFilters;
	}

	public static Browser get() {
		String currentAgent = getUserAgent();
		for (Browser b : Browser.values()) {
			if (browserFiltersMatch(currentAgent, b)) {
				return b;
			}
		}
		return UNKNOWN;
	}

	private static boolean browserFiltersMatch(String currentAgent, Browser b) {
		for (String filterString : b.browserFilters) {
			if (!currentAgent.contains(filterString)) {
				return false;
			}
		}
		return true;
	}

	private final native static String getUserAgent() /*-{
		return navigator.userAgent;
	}-*/;
}
