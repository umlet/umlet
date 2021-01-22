package com.baselet.gwt.client.jsinterop;

import com.google.gwt.core.shared.GWT;

public class FontData {
	public static final String fontName = "UmletFont";

	public static String fontNormal;
	public static String fontItalic;
	public static String fontBold;

	public static final String backupFontNormal;
	public static final String backupFontItalic;
	public static final String backupFontBold;

	static {
		FontResource fontResource = GWT.create(FontResource.class);
		fontNormal = fontResource.fontNormal().getText();
		fontItalic = fontResource.fontItalic().getText();
		fontBold = fontResource.fontBold().getText();

		backupFontNormal = fontNormal;
		backupFontItalic = fontItalic;
		backupFontBold = fontBold;
	}
}
