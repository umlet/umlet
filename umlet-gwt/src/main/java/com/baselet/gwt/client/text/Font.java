package com.baselet.gwt.client.text;

public class Font {
	private String fontDescription;
	private String fontStyle;
	private double fontSize;

	public Font() {}

	public Font(String fontDescription, String fontStyle, double fontSize) {
		this.fontDescription = fontDescription;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
	}

	public String getFontDescription() {
		return fontDescription;
	}

	public void setFontDescription(String fontDescription) {
		this.fontDescription = fontDescription;
	}

	public double getFontSize() {
		return fontSize;
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
}
