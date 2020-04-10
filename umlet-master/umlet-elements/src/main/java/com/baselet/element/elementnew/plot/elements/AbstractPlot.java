package com.baselet.element.elementnew.plot.elements;

import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.elementnew.plot.drawer.AxisConfig;
import com.baselet.element.elementnew.plot.drawer.PlotDrawHandler;
import com.baselet.element.elementnew.plot.drawer.PlotGridDrawConfig;
import com.baselet.element.elementnew.plot.parser.DataSet;
import com.baselet.element.elementnew.plot.parser.ParserException;
import com.baselet.element.elementnew.plot.parser.PlotConstants;
import com.baselet.element.elementnew.plot.parser.PlotConstants.AxisList;
import com.baselet.element.elementnew.plot.parser.PlotConstants.AxisShow;
import com.baselet.element.elementnew.plot.parser.PlotState;

public abstract class AbstractPlot {

	protected PlotDrawHandler plot;

	protected PlotGridDrawConfig plotDrawConfig;
	protected PlotState plotState;

	private final Integer xPosition;
	private final Integer yPosition;

	public AbstractPlot(DrawHandler drawer, PlotGridDrawConfig plotDrawConfig, PlotState plotState, int xPosition, int yPosition) {
		this.plotDrawConfig = plotDrawConfig;
		this.plotState = plotState;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		plot = new PlotDrawHandler(drawer, plotDrawConfig.getRealSize());
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
				plotState.getValueListValidated(AxisShow.getKeyDescAxis(), PlotConstants.toStringList(defaultDescAxisShow()), PlotConstants.toStringList(AxisShow.values()), false));
		plot.getAxisConfig().enableValueAxis(
				plotState.getValueListValidated(AxisShow.getKeyValueAxis(), PlotConstants.toStringList(defaultValueAxisShow()), PlotConstants.toStringList(AxisShow.values()), false),
				plotState.getValueListValidated(AxisList.getKey(), PlotConstants.toStringList(defaultValueAxisList()), PlotConstants.toStringList(AxisList.values()), true));
	}

	private void setupAbstractPlot() {
		DataSet ds = plotState.getDataSet();
		ds.setInvert(plotState.getValueAsBoolean(PlotConstants.KEY_BOOL_DATA_INVERT, PlotConstants.DATA_INVERT_DEFAULT));
		String[] desc = ds.titleRow();
		String[] title = ds.titleCol();
		// System.out.print("\ntitle of ds " + ds.getLineNr() + " :");
		// for (String t : title) System.out.print("<" + t + ">");
		Double[][] values = ds.data();
		List<String> colors = plotState.getValueList(PlotConstants.KEY_LIST_COLORS, PlotConstants.COLORS_DEFAULT);
		for (String color : colors) {
			if (ColorOwn.forStringOrNull(color, Transparency.FOREGROUND) == null) {
				throw new ParserException("Unknown color: " + color + "(line: " + plotState.getLine(PlotConstants.KEY_LIST_COLORS) + ")");
			}
		}
		if (values.length > getMaxAllowedValueRows()) {
			throw new ParserException("The dataset (line: " + plotState.getDataSet().getLineNr() + ") has too many rows for the plot (line: " + plotState.getPlotLineNr() + ")");
		}

		plot.setValues(desc, title, values, colors);
		setMinMaxValue(PlotConstants.KEY_INT_MIN_VALUE);
		setMinMaxValue(PlotConstants.KEY_INT_MAX_VALUE);
	}

	private void setMinMaxValue(String key) {
		String stringValue = plotState.getValue(key, null);
		if (stringValue != null) {
			try {
				if (key.equals(PlotConstants.KEY_INT_MIN_VALUE)) {
					if (stringValue.equals(PlotConstants.MIN_VALUE_ALL)) {
						plot.setMinValue(plotDrawConfig.getMinValue());
					}
					else {
						plot.setMinValue(Double.valueOf(stringValue));
					}
				}
				else if (key.equals(PlotConstants.KEY_INT_MAX_VALUE)) {
					if (stringValue.equals(PlotConstants.MAX_VALUE_ALL)) {
						plot.setMaxValue(plotDrawConfig.getMaxValue());
					}
					else {
						plot.setMaxValue(Double.valueOf(stringValue));
					}
				}
			} catch (Exception e) {
				throw new ParserException(key, stringValue, plotState.getLine(key), e.getMessage());
			}
		}
	}

	protected void setPlotPosition(int columnCount, int rowCount) {
		if (xPosition > columnCount) {
			throw new ParserException("The x coordinate is invalid. PlotGrid width is too small");
		}
		if (yPosition > rowCount) {
			throw new ParserException("The y coordinate is invalid. PlotGrid height is too small");
		}

		double segmentWidth = (double) plotDrawConfig.getRealSize().width / columnCount;
		double segmentHeight = (double) plotDrawConfig.getRealSize().height / rowCount;

		int spaceLeft = (int) (segmentWidth * xPosition);
		int spaceRight = (int) (segmentWidth * (columnCount - xPosition - 1));
		int spaceTop = (int) (segmentHeight * yPosition);
		int spaceBottom = (int) (segmentHeight * (rowCount - yPosition - 1));
		plot.getCanvas().setBorder(spaceLeft, spaceTop, spaceRight, spaceBottom, AxisConfig.ARROW_DISTANCE);
	}

	public abstract void plot(int columnCount, int rowCount);

	protected abstract List<AxisShow> defaultDescAxisShow();

	protected abstract List<AxisShow> defaultValueAxisShow();

	protected abstract List<AxisList> defaultValueAxisList();

	protected abstract int getMaxAllowedValueRows();

}
