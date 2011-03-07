package com.plotlet.element.plotgrid;

import java.awt.Graphics;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.objects.PlotGridDrawConfig;
import com.plotlet.parser.PlotConstants;
import com.plotlet.parser.PlotState;

public class BarPlot extends AbstractPlot {

	public BarPlot(Graphics g, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPos, int yPos) {
		super(g,plotDrawConfig, plotState, xPos, yPos);
	}

	@Override
	public void plot(int columnCount, int rowCount) {
		setPlotPosition(columnCount, rowCount);

		// The barplot should always start at 0 even if there are only values which are > 0 or only values < 0
		try { if (!plotState.containsKey(PlotConstants.KEY_INT_MIN_VALUE)) plot.setMinValue(0.0); } catch (IOException e) { }
		try { if (!plotState.containsKey(PlotConstants.KEY_INT_MAX_VALUE)) plot.setMaxValue(0.0); } catch (IOException e) { }

		Boolean tilt = plotState.getValueAsBoolean(PlotConstants.KEY_BOOL_PLOT_TILT, PlotConstants.PLOT_TILT_DEFAULT);
		plotState.checkIfAllValuesUsed();
		plot.drawPlotAndDescValueAxis(!tilt, true, false, false);
	}

	@Override
	protected List<String> defaultDescAxisShow() {
		return Arrays.asList(new String[] {PlotConstants.DESC_AXIS_SHOW_AXIS, PlotConstants.DESC_AXIS_SHOW_MARKER, PlotConstants.DESC_AXIS_SHOW_TEXT});
	}

	@Override
	protected List<String> defaultValueAxisShow() {
		return Arrays.asList(new String[] {PlotConstants.VALUE_AXIS_SHOW_AXIS, PlotConstants.DESC_AXIS_SHOW_LINE, PlotConstants.VALUE_AXIS_SHOW_MARKER, PlotConstants.VALUE_AXIS_SHOW_TEXT});
	}

	@Override
	protected List<String> defaultValueAxisList() {
		return Arrays.asList(new String[] {PlotConstants.VALUE_AXIS_LIST_RELEVANT});
	}

	@Override
	protected int getMaxAllowedValueRows() {
		return Integer.MAX_VALUE;
	}
}
