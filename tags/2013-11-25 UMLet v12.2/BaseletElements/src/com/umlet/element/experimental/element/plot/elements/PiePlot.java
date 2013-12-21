package com.umlet.element.experimental.element.plot.elements;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.element.plot.drawer.PlotGridDrawConfig;
import com.umlet.element.experimental.element.plot.parser.PlotConstants.AxisList;
import com.umlet.element.experimental.element.plot.parser.PlotConstants.AxisShow;
import com.umlet.element.experimental.element.plot.parser.PlotState;

public class PiePlot extends AbstractPlot {

	public PiePlot(BaseDrawHandler drawer, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPos, int yPos) {
		super(drawer, plotDrawConfig, plotState, xPos, yPos); 
	}

	@Override
	public void plot(int columnCount, int rowCount) {
		setPlotPosition(columnCount, rowCount);
		plotState.checkIfAllValuesUsed();
		plot.drawPiePlot();
	}

	@Override
	protected List<AxisShow> defaultDescAxisShow() {
		return Arrays.asList();
	}

	@Override
	protected List<AxisShow> defaultValueAxisShow() {
		return defaultDescAxisShow();
	}

	@Override
	protected List<AxisList> defaultValueAxisList() {
		return Arrays.asList();
	}

	@Override
	protected int getMaxAllowedValueRows() {
		return 1;
	}


}
