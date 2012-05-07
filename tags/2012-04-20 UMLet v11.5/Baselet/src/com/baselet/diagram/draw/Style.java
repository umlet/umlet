/**
 * 
 */
package com.baselet.diagram.draw;

import java.awt.Color;

import com.baselet.control.Constants;
import com.baselet.control.Constants.LineType;

class Style {
	private LineType lineType;
	private int lineThickness;
	private Color fgColor;
	private Color bgColor;
	private float fgAlpha;
	private float bgAlpha;

	private int fontSize;
	private boolean applyZoom;
	
	public Style(Color foregroundColor, Color backgroundColor, int fontSize) {
		this.lineThickness = Constants.DEFAULT_LINE_THICKNESS;
		this.lineType = LineType.SOLID;
		this.fgColor = foregroundColor;
		this.bgColor = backgroundColor;
		this.fgAlpha = Constants.ALPHA_NO_TRANSPARENCY;
		this.bgAlpha = Constants.ALPHA_MIDDLE_TRANSPARENCY;
		this.fontSize = fontSize;
		this.applyZoom = true;
	}
	
	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}
	public LineType getLineType() {
		return lineType;
	}
	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}
	public int getLineThickness() {
		return lineThickness;
	}
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}
	public Color getFgColor() {
		return fgColor;
	}
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	public Color getBgColor() {
		return bgColor;
	}
	public void setFgAlpha(float fgAlpha) {
		this.fgAlpha = fgAlpha;
	}
	public float getFgAlpha() {
		return fgAlpha;
	}
	public void setBgAlpha(float bgAlpha) {
		this.bgAlpha = bgAlpha;
	}
	public float getBgAlpha() {
		return bgAlpha;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setApplyZoom(boolean applyZoom) {
		this.applyZoom = applyZoom;
	}
	public boolean isApplyZoom() {
		return applyZoom;
	}
}