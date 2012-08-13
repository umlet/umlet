/**
 * 
 */
package com.baselet.diagram.draw;

import java.awt.Color;

import com.baselet.control.Constants;
import com.baselet.control.Constants.LineType;

class Style {
	private LineType lineType;
	private Float lineThickness;
	private Color fgColor;
	private Color bgColor;
	private Float fgAlpha;
	private Float bgAlpha;

	private float fontSize;
	private boolean applyZoom;
	
	public Style() {
		this.lineThickness = (float) Constants.DEFAULT_LINE_THICKNESS;
		this.lineType = LineType.SOLID;
		this.fgAlpha = Constants.ALPHA_NO_TRANSPARENCY;
		this.bgAlpha = Constants.ALPHA_MIDDLE_TRANSPARENCY;
		this.applyZoom = true;
	}
	
	public Style cloneFromMe() {
		Style clone = new Style();
		clone.lineThickness = this.lineThickness;
		clone.lineType = this.lineType;
		clone.fgColor = this.fgColor;
		clone.bgColor = this.bgColor;
		clone.fgAlpha = this.fgAlpha;
		clone.bgAlpha = this.bgAlpha;
		clone.fontSize = this.fontSize;
		clone.applyZoom = this.applyZoom;
		return clone;
	}
	
	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}
	public LineType getLineType() {
		return lineType;
	}
	public void setLineThickness(float lineThickness) {
		this.lineThickness = lineThickness;
	}
	public float getLineThickness() {
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
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setApplyZoom(boolean applyZoom) {
		this.applyZoom = applyZoom;
	}
	public boolean isApplyZoom() {
		return applyZoom;
	}
}