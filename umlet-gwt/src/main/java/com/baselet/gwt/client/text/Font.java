package com.baselet.gwt.client.text;

import com.baselet.control.enums.FormatLabels;

public class Font {
	private String fontName;
	private FormatLabels fontStyle;
	private double fontSize;

	public Font() {}

	public Font(String fontName, FormatLabels fontStyle, double fontSize) {
		this.fontName = fontName;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public double getFontSize() {
		return fontSize;
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}

	public FormatLabels getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(FormatLabels fontStyle) {
		this.fontStyle = fontStyle;
	}
}
