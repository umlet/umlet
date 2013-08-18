package com.baselet.diagram.draw.swing.objects;

import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Dimension;


public class PlotGridDrawConfig {
	
	private final DiagramHandler diagramHandler;
	private final Dimension realSize;
	private final Dimension size;
	private final boolean isSelected;
	private final Double minValue;
	private final Double maxValue;
	
	public PlotGridDrawConfig(DiagramHandler diagramHandler, Dimension realSize, Dimension size, boolean isSelected, Double minValue, Double maxValue) {
		super();
		this.diagramHandler = diagramHandler;
		this.realSize = realSize;
		this.size = size;
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