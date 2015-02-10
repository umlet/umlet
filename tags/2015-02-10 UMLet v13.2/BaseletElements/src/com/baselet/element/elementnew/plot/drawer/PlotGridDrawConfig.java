package com.baselet.element.elementnew.plot.drawer;

import com.baselet.control.basics.geom.Dimension;

public class PlotGridDrawConfig {

	private final Dimension realSize;
	private final Dimension size;
	private final Double minValue;
	private final Double maxValue;

	public PlotGridDrawConfig(Dimension realSize, Dimension size, Double minValue, Double maxValue) {
		super();
		this.realSize = realSize;
		this.size = size;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public Dimension getRealSize() {
		return realSize;
	}

	public Dimension getSize() {
		return size;
	}

	public Double getMinValue() {
		return minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}
}