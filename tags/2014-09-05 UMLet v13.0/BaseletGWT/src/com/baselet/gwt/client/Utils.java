package com.baselet.gwt.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Utils {

	private static DateTimeFormat df = DateTimeFormat.getFormat("yyyy.MM.dd_HH:mm:ss");

	public static void showCursor(Style.Cursor cursor) {
		RootLayoutPanel.get().getElement().getStyle().setCursor(cursor);
	}

	public static native String b64encode(String a) /*-{
		return window.btoa(a);
	}-*/;

	public static native String b64decode(String a) /*-{
		return window.atob(a);
	}-*/;

}
