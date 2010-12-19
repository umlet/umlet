package com.baselet.diagram.draw.objects;

import java.awt.Color;
import java.awt.Dimension;

import com.baselet.diagram.DiagramHandler;


public class PlotGridDrawConfig {
	
	private final DiagramHandler diagramHandler;
	private final Dimension realSize;
	private final Dimension size;
	private final Color fgColor;
	private final Color bgColor;
	private final boolean isSelected;
	private final Double minValue;
	private final Double maxValue;
	
	public PlotGridDrawConfig(DiagramHandler diagramHandler, Dimension realSize, Dimension size, Color fgColor, Color bgColor, boolean isSelected, Double minValue, Double maxValue) {
		super();
		this.diagramHandler = diagramHandler;
		this.realSize = realSize;
		this.size = size;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.isSelected = isSelected;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public DiagramHandler getDiagramHandler() {
		return diagramHandler;
	}

	public Dimension getRealSize() {
		return realSize;
	}

	public Dimension getSize() {
		return size;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public Double getMinValue() {
		return minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}
}