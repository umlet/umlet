package com.baselet.element.elementnew.plot.elements;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.elementnew.plot.drawer.PlotGridDrawConfig;
import com.baselet.element.elementnew.plot.parser.PlotConstants.AxisList;
import com.baselet.element.elementnew.plot.parser.PlotConstants.AxisShow;
import com.baselet.element.elementnew.plot.parser.PlotState;

public class PiePlot extends AbstractPlot {

	public PiePlot(DrawHandler drawer, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPos, int yPos) {
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
