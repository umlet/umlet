package com.plotlet.element.plotgrid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.diagram.draw.PlotDrawHandler;
import com.baselet.diagram.draw.objects.AxisConfig;
import com.baselet.diagram.draw.objects.PlotGridDrawConfig;
import com.plotlet.parser.DataSet;
import com.plotlet.parser.ParserException;
import com.plotlet.parser.PlotConstants;
import com.plotlet.parser.PlotState;

public abstract class AbstractPlot {

	protected final static Logger log = Logger.getLogger(Utils.getClassName());
	protected PlotDrawHandler plot;

	protected PlotGridDrawConfig plotDrawConfig;
	protected PlotState plotState;

	private Integer xPosition;
	private Integer yPosition;

	protected Integer maxDatasetRows = Integer.MAX_VALUE;

	public AbstractPlot(Graphics g, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPosition, int yPosition) {
		this.plotDrawConfig = plotDrawConfig;
		this.plotState = plotState;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		plot = new PlotDrawHandler(g,plotDrawConfig);
		setupAxis();
		setupAbstractPlot();
	}

	public Integer getPlotLineNr() {
		return plotState.getPlotLineNr();
	}

	public Integer getXPosition() {
		return xPosition;
	}

	public Integer getYPosition() {
		return yPosition;
	}
	
	private void setupAxis() {
		plot.getAxisConfig().enableDescAxis(
				plotState.getValueListValidated(PlotConstants.KEY_LIST_DESC_AXIS_SHOW, defaultDescAxisShow(), PlotConstants.getValuesForKey(PlotConstants.KEY_LIST_DESC_AXIS_SHOW), false));
		plot.getAxisConfig().enableValueAxis(
				plotState.getValueListValidated(PlotConstants.KEY_LIST_VALUE_AXIS_SHOW, defaultValueAxisShow(), PlotConstants.getValuesForKey(PlotConstants.KEY_LIST_VALUE_AXIS_SHOW), false),
				plotState.getValueListValidated(PlotConstants.KEY_LIST_VALUE_AXIS_LIST, defaultValueAxisList(), PlotConstants.getValuesForKey(PlotConstants.KEY_LIST_VALUE_AXIS_LIST), true));
	}

	private void setupAbstractPlot() {
		DataSet ds = plotState.getDataSet();
		ds.setInvert(plotState.getValueAsBoolean(PlotConstants.KEY_BOOL_DATA_INVERT, PlotConstants.DATA_INVERT_DEFAULT));
		String[] desc = ds.titleRow();
		String[] title = ds.titleCol();
		//		System.out.print("\ntitle of ds " + ds.getLineNr() + " :");
		//		for (String t : title) System.out.print("<" + t + ">");
		Double[][] values = ds.data();
		List<String> colors = plotState.getValueList(PlotConstants.KEY_LIST_COLORS, PlotConstants.COLORS_DEFAULT);
		for (String color : colors) {
			if (!Constants.colorMap.containsKey(color)) {
				try { // If the color is decodable, it's valid
					Color.decode(color);
				}
				catch(NumberFormatException e) {
					throw new ParserException("Unknown color: " + color + "(line: " + plotState.getLine(PlotConstants.KEY_LIST_COLORS) + ")");
				}
			}
		}
		if (values.length > getMaxAllowedValueRows()) throw new ParserException("The dataset (line: " + plotState.getDataSet().getLineNr() + ") has too many rows for the plot (line: " + plotState.getPlotLineNr() + ")");

		plot.setValues(desc, title, values, colors);
		setMinMaxValue(PlotConstants.KEY_INT_MIN_VALUE);
		setMinMaxValue(PlotConstants.KEY_INT_MAX_VALUE);
	}

	private void setMinMaxValue(String key) {
		String stringValue = plotState.getValue(key, null);
		if (stringValue != null) {
			try {
				if (key.equals(PlotConstants.KEY_INT_MIN_VALUE)) {
					if (stringValue.equals(PlotConstants.MIN_VALUE_ALL)) plot.setMinValue(plotDrawConfig.getMinValue());
					else plot.setMinValue(Double.valueOf(stringValue));
				}
				else if (key.equals(PlotConstants.KEY_INT_MAX_VALUE)) {
					if (stringValue.equals(PlotConstants.MAX_VALUE_ALL)) plot.setMaxValue(plotDrawConfig.getMaxValue());
					else plot.setMaxValue(Double.valueOf(stringValue));
				}
			} catch (Exception e) {
				throw new ParserException(key, stringValue, plotState.getLine(key), e.getMessage());
			}
		}
	}

	protected void setPlotPosition(int columnCount, int rowCount) {
		if (xPosition > columnCount) throw new ParserException("The x coordinate is invalid. PlotGrid width is too small");
		if (yPosition > rowCount) throw new ParserException("The y coordinate is invalid. PlotGrid height is too small");

		double segmentWidth = ((double) plotDrawConfig.getRealSize().width / columnCount);
		double segmentHeight = ((double) plotDrawConfig.getRealSize().height / rowCount);

		int spaceLeft = (int) (segmentWidth * xPosition);
		int spaceRight = (int) (segmentWidth * (columnCount -xPosition - 1));
		int spaceTop = (int) (segmentHeight * yPosition);
		int spaceBottom = (int) (segmentHeight * (rowCount - yPosition - 1));
		plot.getCanvas().setBorder(spaceLeft, spaceTop, spaceRight, spaceBottom, AxisConfig.ARROW_DISTANCE);
	}

	public abstract void plot(int columnCount, int rowCount);

	protected abstract List<String> defaultDescAxisShow();
	protected abstract List<String> defaultValueAxisShow();
	protected abstract List<String> defaultValueAxisList();
	protected abstract int getMaxAllowedValueRows();

}
