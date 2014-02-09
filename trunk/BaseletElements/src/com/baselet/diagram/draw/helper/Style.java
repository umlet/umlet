
package com.baselet.diagram.draw.helper;

import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.LineType;

public class Style {
	private LineType lineType;
	private double lineThickness;
	private ColorOwn fgColor;
	private ColorOwn bgColor;

	private double fontSize;
	private boolean applyZoom;
	
	public Style() {
		this.lineThickness = (double) SharedConstants.DEFAULT_LINE_THICKNESS;
		this.lineType = LineType.SOLID;
		this.applyZoom = true;
	}
	
	public Style cloneFromMe() {
		Style clone = new Style();
		clone.lineThickness = this.lineThickness;
		clone.lineType = this.lineType;
		clone.fgColor = this.fgColor;
		clone.bgColor = this.bgColor;
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
	public void setLineThickness(double lineThickness) {
		this.lineThickness = lineThickness;
	}
	public double getLineThickness() {
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
	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	public double getFontSize() {
		return fontSize;
	}
	public void setApplyZoom(boolean applyZoom) {
		this.applyZoom = applyZoom;
	}
	public boolean isApplyZoom() {
		return applyZoom;
	}
}