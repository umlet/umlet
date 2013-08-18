package com.plotlet.element.plotgrid;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.swing.objects.PlotGridDrawConfig;
import com.plotlet.parser.PlotState;

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
