
package com.baselet.diagram.draw.helper;

import com.baselet.control.NewGridElementConstants;
import com.baselet.control.enumerations.LineType;

public class Style {
	private LineType lineType;
	private Float lineThickness;
	private ColorOwn fgColor;
	private ColorOwn bgColor;
	private Float fgAlpha;
	private Float bgAlpha;

	private float fontSize;
	private boolean applyZoom;
	
	public Style() {
		this.lineThickness = (float) NewGridElementConstants.DEFAULT_LINE_THICKNESS;
		this.lineType = LineType.SOLID;
		this.fgAlpha = NewGridElementConstants.ALPHA_NO_TRANSPARENCY;
		this.bgAlpha = NewGridElementConstants.ALPHA_MIDDLE_TRANSPARENCY;
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
	public void setFgColor(ColorOwn fgColor) {
		this.fgColor = fgColor;
	}
	public ColorOwn getFgColor() {
		return fgColor;
	}
	public void setBgColor(ColorOwn bgColor) {
		this.bgColor = bgColor;
	}
	public ColorOwn getBgColor() {
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