package com.plotlet.element.plotgrid;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.objects.PlotGridDrawConfig;
import com.plotlet.parser.PlotConstants;
import com.plotlet.parser.PlotState;

public class PiePlot extends AbstractPlot {

	public PiePlot(Graphics g, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPos, int yPos) {
		super(g,plotDrawConfig, plotState, xPos, yPos); 
	}

	@Override
	public void plot(int columnCount, int rowCount) {
		setPlotPosition(columnCount, rowCount);
		Boolean tilt = plotState.getValueAsBoolean(PlotConstants.KEY_BOOL_PLOT_TILT, PlotConstants.PLOT_TILT_DEFAULT);
		plotState.checkIfAllValuesUsed();
		plot.drawPiePlot(tilt);
	}

	@Override
	protected List<String> defaultDescAxisShow() {
		return Arrays.asList(new String[] {});
	}

	@Override
	protected List<String> defaultValueAxisShow() {
		return Arrays.asList(new String[] {});
	}

	@Override
	protected List<String> defaultValueAxisList() {
		return Arrays.asList(new String[] {});
	}

	@Override
	protected int getMaxAllowedValueRows() {
		return 1;
	}


}
