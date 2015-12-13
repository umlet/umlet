package com.baselet.element.old.custom;

import java.awt.Color;
import java.awt.Shape;

import com.baselet.control.enums.LineType;

/**
 * Extended shape which supports the stroketype and the line thickness
 */
public class StyleShape {

	private Shape shape;
	private LineType lineType;
	private int lineThickness;
	private Color fgColor;
	private Color bgColor;
	private float alpha;

	public StyleShape(Shape shape, LineType lineType, int lineThickness, Color fgColor, Color bgColor, float alpha) {
		super();
		this.shape = shape;
		this.lineType = lineType;
		this.lineThickness = lineThickness;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.alpha = alpha;
	}

	public Shape getShape() {
		return shape;
	}

	public LineType getLineType() {
		return lineType;
	}

	public int getLineThickness() {
		return lineThickness;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}

	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(alpha);
		result = prime * result + (bgColor == null ? 0 : bgColor.hashCode());
		result = prime * result + (fgColor == null ? 0 : fgColor.hashCode());
		result = prime * result + lineThickness;
		result = prime * result + (shape == null ? 0 : shape.hashCode());
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
		StyleShape other = (StyleShape) obj;
		if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha)) {
			return false;
		}
		if (bgColor == null) {
			if (other.bgColor != null) {
				return false;
			}
		}
		else if (!bgColor.equals(other.bgColor)) {
			return false;
		}
		if (fgColor == null) {
			if (other.fgColor != null) {
				return false;
			}
		}
		else if (!fgColor.equals(other.fgColor)) {
			return false;
		}
		if (lineThickness != other.lineThickness) {
			return false;
		}
		if (lineType != other.lineType) {
			return false;
		}
		if (shape == null) {
			if (other.shape != null) {
				return false;
			}
		}
		else if (!shape.equals(other.shape)) {
			return false;
		}
		return true;
	}

}
