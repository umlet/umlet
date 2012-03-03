package com.baselet.diagram.draw.objects;

import java.util.List;
import java.util.TreeSet;

import com.plotlet.parser.PlotConstants;

public class AxisConfig {

	private boolean descAxisLine, valueAxisLine;
	private boolean descAxisMarkers, valueAxisMarkers;
	private boolean descAxisText, valueAxisText;
	private boolean descAxisGray, valueAxisGray;

	private TreeSet<Double> valueAxisList;
	private boolean showRelevantValues;

	public static final int ARROW_SIZE = (int) (5 * 1f); // arrowLength * arrowEndAngle
	public static final int ARROW_DISTANCE = ARROW_SIZE*3; // Distance between outerBorder and innerBorder; plotarea where only axis are allowed to draw

	private boolean xIsDescription;
	private boolean drawAxis;

	private int  descSegment; // The width of a bar (always the "shorter" side of the bar)
	private Double valueSegment; // How many pixels equal one valuesegment (eg: if values reach from 1 to 100000 it's very small, if they reach from 1 to 5 it's very high)

	private int descAxisPos; // If horizontal sourceAxisPos is the x value of the vertical axis, else it's the y value of the horizontal axis
	private int valueAxisPos; // If horizontal sourceAxisPos is the x value of the vertical axis, else it's the y value of the horizontal axis
	private int xAxisPos;
	private int yAxisPos;

	public AxisConfig() {
		super();
		drawAxis = false;
	}

	public final void enableDescAxis(List<String> showList/*, List<String> valueList*/) {
		this.drawAxis = true;
		this.descAxisLine = showList.contains(PlotConstants.DESC_AXIS_SHOW_AXIS);
		this.descAxisGray = showList.contains(PlotConstants.DESC_AXIS_SHOW_LINE);
		this.descAxisMarkers = showList.contains(PlotConstants.DESC_AXIS_SHOW_MARKER);
		this.descAxisText = showList.contains(PlotConstants.DESC_AXIS_SHOW_TEXT);
	}

	public final void enableValueAxis(List<String> showList, List<String> valueList) {
		this.drawAxis = true;
		this.valueAxisLine = showList.contains(PlotConstants.VALUE_AXIS_SHOW_AXIS);
		this.valueAxisGray = showList.contains(PlotConstants.VALUE_AXIS_SHOW_LINE);
		this.valueAxisMarkers = showList.contains(PlotConstants.VALUE_AXIS_SHOW_MARKER);
		this.valueAxisText = showList.contains(PlotConstants.VALUE_AXIS_SHOW_TEXT);

		this.showRelevantValues = valueList.contains(PlotConstants.VALUE_AXIS_LIST_RELEVANT);
		this.valueAxisList = new TreeSet<Double>();
		for (String v : valueList) {
			try {
				valueAxisList.add(Double.parseDouble(v));
			} catch(Exception e) { }
		}
	}

	public boolean isxDescription() {
		return xIsDescription;
	}

	public void setxIsDescription(boolean xIsDescription) {
		this.xIsDescription = xIsDescription;
	}

	public boolean showAxis() {
		return drawAxis;
	}

	public boolean drawXAxis() {
		return isxDescription() && descAxisLine || !isxDescription() && valueAxisLine;
	}

	public boolean drawYAxis() {
		return isxDescription() && valueAxisLine || !isxDescription() && descAxisLine;
	}

	public boolean drawDescriptionAxisMarkers() {
		return descAxisMarkers;
	}

	public boolean drawDescriptionAxisMarkerText() {
		return descAxisText;
	}

	public boolean drawDescriptionAxisMarkerGrayline() {
		return descAxisGray;
	}

	public boolean drawValueAxis() {
		return valueAxisLine;
	}

	public boolean drawValueAxisMarkers() {
		return valueAxisMarkers;
	}

	public boolean drawValueAxisMarkerText() {
		return valueAxisText;
	}

	public boolean drawValueAxisMarkerGrayline() {
		return valueAxisGray;
	}

	public void setDescAxisPos(int pos) {
		this.descAxisPos = pos;
		if (xIsDescription) this.xAxisPos = pos;
		else this.yAxisPos = pos;
	}

	public int getxAxisPos() {
		return xAxisPos;
	}

	public void setValueAxisPos(int pos) {
		this.valueAxisPos = pos;
		if (!xIsDescription) this.xAxisPos = pos;
		else this.yAxisPos = pos;
	}

	public int getyAxisPos() {
		return yAxisPos;
	}

	public int getDescAxisPos() {
		return descAxisPos;
	}

	public int getValueAxisPos() {
		return valueAxisPos;
	}

	public int getDescSegment() {
		return descSegment;
	}

	public void setDescSegment(int descSegment) {
		this.descSegment = descSegment;
	}

	public Double getValueSegment() {
		return valueSegment;
	}

	public void setValueSegment(Double valueSegment) {
		this.valueSegment = valueSegment;
	}

	public TreeSet<Double> setValueAxisList(TreeSet<Double> valuesSorted) {
		if (showRelevantValues) {
			this.valueAxisList.addAll(valuesSorted);
		}
		return this.valueAxisList;
	}

}
