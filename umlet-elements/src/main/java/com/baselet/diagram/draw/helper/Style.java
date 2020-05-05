package com.baselet.diagram.draw.helper;

import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.LineType;

public class Style {
	private LineType lineType;
	private double lineWidth;
	private ColorOwnBase foregroundColor;
	private ColorOwnBase backgroundColor;

	private double fontSize;
	private boolean applyZoom;

	public Style() {
		lineWidth = FacetConstants.LINE_WIDTH_DEFAULT;
		lineType = LineType.SOLID;
		applyZoom = true;
	}

	public Style cloneFromMe() {
		Style clone = new Style();
		clone.lineWidth = lineWidth;
		clone.lineType = lineType;
		clone.foregroundColor = foregroundColor;
		clone.backgroundColor = backgroundColor;
		clone.fontSize = fontSize;
		clone.applyZoom = applyZoom;
		return clone;
	}

	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}

	public LineType getLineType() {
		return lineType;
	}

	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public double getLineWidth() {
		return lineWidth;
	}

	public void setForegroundColor(ColorOwnBase foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public ColorOwnBase getForegroundColor() {
		return foregroundColor;
	}

	public void setBackgroundColor(ColorOwnBase backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public ColorOwnBase getBackgroundColor() {
		return backgroundColor;
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
		result = prime * result + (backgroundColor == null ? 0 : backgroundColor.hashCode());
		result = prime * result + (foregroundColor == null ? 0 : foregroundColor.hashCode());
		long temp;
		temp = Double.doubleToLongBits(fontSize);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(lineWidth);
		result = prime * result + (int) (temp ^ temp >>> 32);
		result = prime * result + (lineType == null ? 0 : lineType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Style other = (Style) obj;
		if (applyZoom != other.applyZoom) {
			return false;
		}
		if (backgroundColor == null) {
			if (other.backgroundColor != null) {
				return false;
			}
		}
		else if (!backgroundColor.equals(other.backgroundColor)) {
			return false;
		}
		if (foregroundColor == null) {
			if (other.foregroundColor != null) {
				return false;
			}
		}
		else if (!foregroundColor.equals(other.foregroundColor)) {
			return false;
		}
		if (Double.doubleToLongBits(fontSize) != Double.doubleToLongBits(other.fontSize)) {
			return false;
		}
		if (Double.doubleToLongBits(lineWidth) != Double.doubleToLongBits(other.lineWidth)) {
			return false;
		}
		if (lineType != other.lineType) {
			return false;
		}
		return true;
	}

}