package com.umlet.element.experimental.element.plot.elements;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.element.plot.drawer.PlotGridDrawConfig;
import com.umlet.element.experimental.element.plot.parser.PlotConstants;
import com.umlet.element.experimental.element.plot.parser.PlotConstants.AxisList;
import com.umlet.element.experimental.element.plot.parser.PlotConstants.AxisShow;
import com.umlet.element.experimental.element.plot.parser.PlotState;

public class ScatterPlot extends AbstractPlot {

	public ScatterPlot(BaseDrawHandler drawer, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPos, int yPos) {
		super(drawer, plotDrawConfig, plotState, xPos, yPos); 
	}

	@Override
	public void plot(int columnCount, int rowCount) {
		setPlotPosition(columnCount, rowCount);
		Boolean tilt = plotState.getValueAsBoolean(PlotConstants.KEY_BOOL_PLOT_TILT, PlotConstants.PLOT_TILT_DEFAULT);
		plotState.checkIfAllValuesUsed();
		plot.drawPlotAndDescValueAxis(!tilt, false, false, true);
	}

	@Override
	protected List<AxisShow> defaultDescAxisShow() {
		return Arrays.asList(AxisShow.Axis, AxisShow.Marker, AxisShow.Text);
	}

	@Override
	protected List<AxisShow> defaultValueAxisShow() {
		return defaultDescAxisShow();
	}

	@Override
	protected List<AxisList> defaultValueAxisList() {
		return Arrays.asList(AxisList.Relevant);
	}

	@Override
	protected int getMaxAllowedValueRows() {
		return Integer.MAX_VALUE;
	}

}
