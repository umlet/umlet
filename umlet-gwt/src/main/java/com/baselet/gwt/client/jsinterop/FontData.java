package com.baselet.gwt.client.jsinterop;

import com.google.gwt.core.shared.GWT;

public class FontData {
	public static final String fontName = "UmletFont";

	public static String fontNormal;
	public static String fontItalic;
	public static String fontBold;

	static {
		FontResource fontResource = GWT.create(FontResource.class);
		fontNormal = fontResource.fontNormal().getText();
		fontItalic = fontResource.fontItalic().getText();
		fontBold = fontResource.fontBold().getText();
	}
}
