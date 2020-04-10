package com.baselet.gwt.client.base;

import com.baselet.element.interfaces.CursorOwn;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Utils {

	public static void showCursor(CursorOwn cursor) {
		RootLayoutPanel.get().getElement().getStyle().setCursor(Converter.convert(cursor));
	}

	public static native String b64encode(String a) /*-{
		return window.btoa(a);
	}-*/;

	public static native String b64decode(String a) /*-{
		return window.atob(a);
	}-*/;

}
