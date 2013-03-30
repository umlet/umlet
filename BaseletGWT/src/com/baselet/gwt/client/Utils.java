package com.baselet.gwt.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Utils {
	
	private static DateTimeFormat df = DateTimeFormat.getFormat("yyyy.MM.dd_HH:mm:ss");

	public static void showCursor(Style.Cursor cursor) {
		RootLayoutPanel.get().getElement().getStyle().setCursor(cursor);
	}

	public static String getProgramnameLowerCase() {
		return "umlet";
	}

	public static String getProgramVersion() {
		return "12.2";
	}

}
