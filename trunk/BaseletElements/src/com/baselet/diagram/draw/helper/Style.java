
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
		this.lineThickness = SharedConstants.DEFAULT_LINE_THICKNESS;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (applyZoom ? 1231 : 1237);
		result = prime * result + ((bgColor == null) ? 0 : bgColor.hashCode());
		result = prime * result + ((fgColor == null) ? 0 : fgColor.hashCode());
		long temp;
		temp = Double.doubleToLongBits(fontSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lineThickness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((lineType == null) ? 0 : lineType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Style other = (Style) obj;
		if (applyZoom != other.applyZoom) return false;
		if (bgColor == null) {
			if (other.bgColor != null) return false;
		}
		else if (!bgColor.equals(other.bgColor)) return false;
		if (fgColor == null) {
			if (other.fgColor != null) return false;
		}
		else if (!fgColor.equals(other.fgColor)) return false;
		if (Double.doubleToLongBits(fontSize) != Double.doubleToLongBits(other.fontSize)) return false;
		if (Double.doubleToLongBits(lineThickness) != Double.doubleToLongBits(other.lineThickness)) return false;
		if (lineType != other.lineType) return false;
		return true;
	}
	
}