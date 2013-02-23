package com.baselet.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Utils {

	public static void showCursor(Style.Cursor cursor) {
		RootLayoutPanel.get().getElement().getStyle().setCursor(cursor);
	}

}
