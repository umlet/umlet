package com.baselet.element.facet;

/**
 * represents the space buffer around the printed PropertiesText
 * Many Facets set restrictions via the buffer (e.g. a Package element should never print text over the upper left rectangle)
 * The most important buffer-reader is the TextPrintFacet which uses it to calculate the text position
 */
public class Buffer {
	private double top = 0; // the top space where no text should be placed (e.g. the upper left rectangle of the Package element)
	private double left = 0;
	private double right = 0;

	public double getTop() {
		return top;
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public void addToLeft(double inc) {
		left += inc;
	}

	public void addToRight(double inc) {
		right += inc;
	}

	/**
	 * sets the required top buffer. it is always the max from this or the previous buffer, because the facets are independent from each other
	 * if one required 20px and the other 10px, in general 20px are required to satisfy the requirements of both facets
	 */
	public void setTopMin(double newMin) {
		top = Math.max(top, newMin);
	}

	public void addToLeftAndRight(double inc) {
		addToLeft(inc);
		addToRight(inc);
	}
}
