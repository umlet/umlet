package com.baselet.element.elementnew.plot.elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.elementnew.plot.drawer.PlotGridDrawConfig;
import com.baselet.element.elementnew.plot.parser.PlotConstants;
import com.baselet.element.elementnew.plot.parser.PlotConstants.AxisList;
import com.baselet.element.elementnew.plot.parser.PlotConstants.AxisShow;
import com.baselet.element.elementnew.plot.parser.PlotState;

public class BarPlot extends AbstractPlot {

	public BarPlot(DrawHandler drawer, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPos, int yPos) {
		super(drawer, plotDrawConfig, plotState, xPos, yPos);
	}

	@Override
	public void plot(int columnCount, int rowCount) {
		setPlotPosition(columnCount, rowCount);

		// The barplot should always start at 0 even if there are only values which are > 0 or only values < 0
		try {
			if (!plotState.containsKey(PlotConstants.KEY_INT_MIN_VALUE)) {
				plot.setMinValue(0.0);
			}
		} catch (IOException e) {}
		try {
			if (!plotState.containsKey(PlotConstants.KEY_INT_MAX_VALUE)) {
				plot.setMaxValue(0.0);
			}
		} catch (IOException e) {}

		Boolean tilt = plotState.getValueAsBoolean(PlotConstants.KEY_BOOL_PLOT_TILT, PlotConstants.PLOT_TILT_DEFAULT);
		plotState.checkIfAllValuesUsed();
		plot.drawPlotAndDescValueAxis(!tilt, true, false, false);
	}

	@Override
	protected List<AxisShow> defaultDescAxisShow() {
		return Arrays.asList(AxisShow.Axis, AxisShow.Marker, AxisShow.Text);
	}

	@Override
	protected List<AxisShow> defaultValueAxisShow() {
		return Arrays.asList(AxisShow.Axis, AxisShow.Line, AxisShow.Marker, AxisShow.Text);
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
