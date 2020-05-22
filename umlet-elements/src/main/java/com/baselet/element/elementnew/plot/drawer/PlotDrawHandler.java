package com.baselet.element.elementnew.plot.drawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;

public class PlotDrawHandler {

	// Enumerations
	public enum Position {
		LEFT, UP, DOWN, RIGHT
	}

	// Plot specific settings
	private String[] title;
	private String[] desc;
	private Double[][] values;
	private TreeSet<Double> valuesSorted;
	private TreeSet<Double> valuesShownOnAxisSorted;
	// private Double[][] valuesMinMaxCorrected; // if all values are >0 or all values are <0 the distance from 0 to the first real value will be subtracted

	protected DrawHandler base;

	private Double minVal = null;
	private Double maxVal = null;
	private List<String> colors;
	private final Canvas canvas;
	private final AxisConfig axisConfig;

	public PlotDrawHandler(DrawHandler baseDrawHandler, Dimension size) {
		base = baseDrawHandler;
		// drawLegend = false;
		axisConfig = new AxisConfig();
		canvas = new Canvas(size);
	}

	// Legend Settings
	// private boolean drawLegend;
	// private Rectangle legendPos;

	// /**
	// * Abstracts the axis drawing from the type of variables on the axis (description or values)
	// * Methods called from this method don't know if they handle a description or value axis
	// * @param xAxis if true this is the method call for the x-axis
	// * @param valuesSorted the sorted list of values
	// */
	// private void abstractValueDescFromAxisAndDraw(boolean xAxis) {
	// int segmentDisp, lastDrawnSegmentDisp;
	//
	// if /* thisIsDescAxis */ ((xAxis && axisConfig.isxDescription()) || (!xAxis && !axisConfig.isxDescription())) {
	// // if (axisConfig.drawDescriptionAxis()) drawAxisLine(xAxis);
	// if (true) {
	// lastDrawnSegmentDisp = -axisConfig.getDescSegment()/2;
	// for (int i = 0; i < desc.length; i++) {
	// segmentDisp = (i * axisConfig.getDescSegment()) + (axisConfig.getDescSegment()/2);
	// String value;
	// if (xAxis) value = desc[i];
	// else value = desc[desc.length-i-1]; // yAxis is drawn from bottom to top, therefore invert computation direction
	// // axisConfig.activateDescriptionAxis();
	// lastDrawnSegmentDisp = drawMarkerTextIfThereIsEnoughSpace(xAxis, segmentDisp, lastDrawnSegmentDisp, value);
	// }
	// }
	// }
	// else /* thisIsValueAxis */ {
	// // if (axisConfig.drawValueAxis()) drawAxisLine(xAxis);
	// if (true) {
	// Double[] valuesToDisplay;
	// if (axisConfig.drawValueAxisMarkersAll()) {
	// if (axisConfig.getValueSegment() < 1) valuesToDisplay = Utils.createDoubleArrayFromTo(minVal, maxVal, Math.ceil(1/axisConfig.getValueSegment()));
	// else valuesToDisplay = Utils.createDoubleArrayFromTo(minVal, maxVal);
	// }
	// else valuesToDisplay = valuesSorted;
	//
	// lastDrawnSegmentDisp = (int) (valuesToDisplay[0] * axisConfig.getValueSegment() - 100); // Start at the lowest possible number
	// for (Double v : valuesToDisplay) {
	// segmentDisp = (int) calculateValuePos(v, axisConfig.getValueSegment());
	// // valueStringToDisplay is the String representation of the value (".0" should not be displayed)
	// String valueStringToDisplay = (Math.round(v) == v) ? String.valueOf(Math.round(v)) : String.valueOf(v);
	// if (axisConfig.drawValueAxisMarkersAll()) {
	// int oldLength = valueStringToDisplay.length();
	// if (oldLength > 2) {
	// valueStringToDisplay = valueStringToDisplay.substring(0, 2);
	// for (int i = 0; i < oldLength-2; i++) valueStringToDisplay += "0";
	// }
	// }
	// // if (value == 0) continue; // 0 is not displayed because it would overlap with the arrow end
	// // axisConfig.activateValueAxis();
	// lastDrawnSegmentDisp = drawMarkerTextIfThereIsEnoughSpace(xAxis, segmentDisp, lastDrawnSegmentDisp, valueStringToDisplay);
	// }
	// }
	// }
	// }
	//
	// /**
	// * Draws text descriptions of axes only if there is enough space for it.
	// */
	// private int drawMarkerTextIfThereIsEnoughSpace(boolean xAxis, int segmentDisp, int lastDrawnSegmentDisp, String valueAsString) {
	// boolean drawMarker = false;
	// // If text should be displayed markers where there would be no space for the text are not drawn
	// // if (axisConfig.drawActiveAxisMarkerText()) {
	// int textSpaceNeeded;
	// if (xAxis) textSpaceNeeded = base.textWidth(valueAsString);
	// else textSpaceNeeded = base.textHeight();
	// if ((segmentDisp - lastDrawnSegmentDisp) >= textSpaceNeeded) {
	// drawMarker = true;
	// lastDrawnSegmentDisp = segmentDisp;
	// }
	// // }
	// else drawMarker = true;
	//
	// if (drawMarker) drawAxisMarker(xAxis, segmentDisp, valueAsString);
	// return lastDrawnSegmentDisp;
	// }

	// //TODO isnt working properly now
	// public void enableLegend(Position position) {
	// this.drawLegend = true;
	// this.legendPos = getLegendPosition(position);
	// }
	//
	// public void disableLegend() {
	// this.drawLegend = false;
	// }
	//
	// private void drawLegend() {
	// base.drawRectangle(legendPos.x, legendPos.y, legendPos.width, legendPos.height);
	// }
	//
	// private Rectangle getLegendPosition(Position position) {
	// // Calculate size of the legend
	// final Rectangle innerLegendBorder = new Rectangle(10, 10, 10, 10);
	// int legendWidth = innerLegendBorder.x + innerLegendBorder.width;
	// int legendHeight = innerLegendBorder.y + innerLegendBorder.height;
	// final int legendSpace = 10;
	//
	// int textWidth;
	// for (String v : desc) {
	// legendHeight += base.textHeight();
	// textWidth = base.textWidth(v) + innerLegendBorder.x + innerLegendBorder.width;
	// if (textWidth > legendWidth) legendWidth = textWidth;
	// }
	//
	// // The outerBorder of the plot must be adjusted to free space for the legend
	// int borderX = canvas.getOuterLeftPos();
	// int borderY = canvas.getOuterUpPos();
	// int borderW = canvas.getOuterRightBorderWidth();
	// int borderH = canvas.getOuterDownBorderHeight();
	//
	// if (position == Position.LEFT) borderX += legendWidth + legendSpace;
	// else if (position == Position.RIGHT) borderW += legendWidth + legendSpace;
	// else if (position == Position.UP) borderY += legendHeight + legendSpace;
	// else if (position == Position.DOWN) borderH += legendHeight + legendSpace;
	//
	// canvas.setBorder(borderX, borderY, borderW, borderH, AxisConfig.ARROW_DISTANCE);
	//
	// // Calculate and return the position of the legend rectangle
	// final int x, y;
	// if (position == Position.LEFT || position == Position.RIGHT) {
	// y = (canvas.getInnerDownPos() - legendHeight) / 2;
	// if (position == Position.LEFT) {
	// x = 1;
	// } else {
	// x = canvas.getInnerRightPos() - legendWidth - legendSpace/2;
	// }
	// } else {
	// x = (canvas.getInnerRightPos() - legendWidth) / 2;
	// if (position == Position.UP) {
	// y = 1;
	// } else {
	// y = canvas.getInnerDownPos() - legendHeight - legendSpace/2;
	// }
	// }
	// return new Rectangle(x, y, legendWidth, legendHeight);
	// }

	public final void drawPlotAndDescValueAxis(boolean xIsDescription, boolean drawBars, boolean drawLines, boolean drawPoints) {
		axisConfig.setxIsDescription(xIsDescription);
		setupAxis();
		calculateAdditionalSpaceForYAxisTextWidth();

		// log.debug("yIsDescription: " + yIsDescription + ", descSegment: " + axisConfig.getDescSegment() + ", valueSegment: " + axisConfig.getValueSegment());
		// log.debug("valueRange: " + valueRange + ", barsCount: " + elementCount + ", SourceAxisPos/DescAxisPos: " + axisConfig.getDescAxisPos() + ", BarStart/ValueAxisPos: " + axisConfig.getValueAxisPos());

		if (drawBars) {
			drawBars(xIsDescription, values, axisConfig.getDescAxisPos(), axisConfig.getValueAxisPos(), axisConfig.getValueSegment(), axisConfig.getDescSegment(), colors);
		}
		if (drawLines) {
			drawLineOrPoints(xIsDescription, values, axisConfig.getDescAxisPos(), axisConfig.getValueAxisPos(), axisConfig.getValueSegment(), axisConfig.getDescSegment(), colors, true);
		}
		if (drawPoints) {
			drawLineOrPoints(xIsDescription, values, axisConfig.getDescAxisPos(), axisConfig.getValueAxisPos(), axisConfig.getValueSegment(), axisConfig.getDescSegment(), colors, false);
		}

		if (axisConfig.showAxis()) {
			drawAxis(xIsDescription, axisConfig.getDescAxisPos(), axisConfig.getValueAxisPos(), axisConfig.getValueSegment(), axisConfig.getDescSegment());
		}
	}

	private void setupAxis() {
		final Double valueRange = Math.max(1.0, maxVal - minVal); // The range is >=1 (otherwise nothing will be drawn)
		Double negativeRange = 0.0;
		if (minVal > 0) {
			negativeRange = 0.0;
		}
		if (minVal < 0) {
			if (maxVal < 0) {
				negativeRange = valueRange;
			}
			else {
				negativeRange = -minVal;
			}
		}

		int elementCount = desc.length; // Amount of bars/lines/...
		for (Double[] vArray : values) {
			if (vArray.length > elementCount) {
				elementCount = vArray.length;
			}
		}

		// Calculate some necessary variables to draw the bars (these variables abstract from horizontal/vertical to a relative point of view)
		if (axisConfig.isxDescription()) {
			axisConfig.setDescSegment(canvas.getInnerHorizontalDrawspace() / elementCount);
			axisConfig.setValueSegment(canvas.getInnerVerticalDrawspace() / valueRange);
			axisConfig.setDescAxisPos((int) (canvas.getInnerDownPos() - axisConfig.getValueSegment() * negativeRange));
			axisConfig.setValueAxisPos(canvas.getInnerLeftPos());
		}
		else {
			axisConfig.setDescSegment(canvas.getInnerVerticalDrawspace() / elementCount);
			axisConfig.setValueSegment(canvas.getInnerHorizontalDrawspace() / valueRange);
			axisConfig.setDescAxisPos((int) (canvas.getInnerLeftPos() + axisConfig.getValueSegment() * negativeRange));
			axisConfig.setValueAxisPos(canvas.getInnerUpPos());
		}
	}

	private final void drawAxis(boolean xIsDescription, int sourceAxisPos, int valueAxisPos, Double valueSegment, int descSegment) {
		List<Integer> xpoints = new ArrayList<Integer>();
		List<String> xtext = new ArrayList<String>();
		List<Integer> ypoints = new ArrayList<Integer>();
		List<String> ytext = new ArrayList<String>();

		int lineIterator = valueAxisPos + descSegment / 2;
		for (String d : desc) {
			if (xIsDescription) {
				xpoints.add(lineIterator);
				xtext.add(d);
			}
			else {
				ypoints.add(lineIterator);
				ytext.add(d);
			}
			lineIterator += descSegment;
		}

		for (Double v : valuesShownOnAxisSorted) {
			int linePos = (int) calculateValuePos(v, valueSegment);
			if (xIsDescription) {
				ypoints.add(sourceAxisPos - linePos);
				ytext.add(String.valueOf(v));
			}
			else {
				xpoints.add(sourceAxisPos + linePos);
				xtext.add(String.valueOf(v));
			}
		}

		drawGraylines(xpoints, ypoints);
		base.setForegroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.BLACK).transparency(Transparency.FOREGROUND));
		drawAxisLine();
		drawMarkers(xpoints, ypoints);
		drawMarkerTexts(xpoints, xtext, ypoints, ytext);
	}

	/**
	 * Method to draw one line (which one is specified by the boolean xAxis variable)
	 * @param xAxis
	 * @param drawArrows
	 */
	private void drawAxisLine() {
		if (axisConfig.drawXAxis()) {
			final int x1 = canvas.getInnerLeftPos();
			final int x2 = canvas.getInnerRightPos();
			final int y = axisConfig.getxAxisPos();
			base.drawLine(x1, y, x2, y);
		}
		if (axisConfig.drawYAxis()) {
			final int x = axisConfig.getyAxisPos();
			final int y1 = canvas.getInnerUpPos();
			final int y2 = canvas.getInnerDownPos();
			base.drawLine(x, y1, x, y2);
		}
	}

	private void drawGraylines(List<Integer> xpoints, List<Integer> ypoints) {
		base.setForegroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.BLACK).transparency(Transparency.SELECTION_BACKGROUND));
		boolean drawVerticalGraylines = axisConfig.isxDescription() && axisConfig.drawDescriptionAxisMarkerGrayline() || !axisConfig.isxDescription() && axisConfig.drawValueAxisMarkerGrayline();
		boolean drawHorizontalGraylines = !axisConfig.isxDescription() && axisConfig.drawDescriptionAxisMarkerGrayline() || axisConfig.isxDescription() && axisConfig.drawValueAxisMarkerGrayline();
		if (drawVerticalGraylines) {
			for (Integer x : xpoints) {
				base.drawLine(x, canvas.getInnerUpPos(), x, canvas.getInnerDownPos());
			}
		}
		if (drawHorizontalGraylines) {
			for (Integer y : ypoints) {
				base.drawLine(canvas.getInnerLeftPos(), y, canvas.getInnerRightPos(), y);
			}
		}
	}

	private void drawMarkers(List<Integer> xpoints, List<Integer> ypoints) {
		boolean drawVerticalMarkers = axisConfig.isxDescription() && axisConfig.drawDescriptionAxisMarkers() || !axisConfig.isxDescription() && axisConfig.drawValueAxisMarkers();
		boolean drawHorizontalMarkers = !axisConfig.isxDescription() && axisConfig.drawDescriptionAxisMarkers() || axisConfig.isxDescription() && axisConfig.drawValueAxisMarkers();
		if (drawVerticalMarkers) {
			for (Integer x : xpoints) {
				base.drawLine(x, axisConfig.getxAxisPos(), x, axisConfig.getxAxisPos() + AxisConfig.ARROW_SIZE);
			}
		}
		if (drawHorizontalMarkers) {
			for (Integer y : ypoints) {
				base.drawLine(axisConfig.getyAxisPos() - AxisConfig.ARROW_SIZE, y, axisConfig.getyAxisPos(), y);
			}
		}
	}

	private void drawMarkerTexts(List<Integer> xpoints, List<String> xtext, List<Integer> ypoints, List<String> ytext) {
		boolean drawVerticalMarkerTexts = axisConfig.isxDescription() && axisConfig.drawDescriptionAxisMarkerText() || !axisConfig.isxDescription() && axisConfig.drawValueAxisMarkerText();
		boolean drawHorizontalMarkerTexts = !axisConfig.isxDescription() && axisConfig.drawDescriptionAxisMarkerText() || axisConfig.isxDescription() && axisConfig.drawValueAxisMarkerText();
		if (drawVerticalMarkerTexts) {
			for (int i = 0; i < xpoints.size(); i++) {
				base.print(xtext.get(i), xpoints.get(i), axisConfig.getxAxisPos() + AxisConfig.ARROW_DISTANCE, AlignHorizontal.CENTER);
			}
		}
		if (drawHorizontalMarkerTexts) {
			for (int i = 0; i < ypoints.size(); i++) {
				base.print(ytext.get(i), axisConfig.getyAxisPos() - 8, (int) (ypoints.get(i) + base.textHeightMax() / 2), AlignHorizontal.RIGHT);
			}
		}
	}

	private final void drawLineOrPoints(boolean xIsDescription, Double[][] values, int sourceAxisPos, int valueAxisPos, Double valueSegment, int descSegment, List<String> colors, boolean line) {
		Theme currentTheme = ThemeFactory.getCurrentTheme();
		int cIndex = 0;
		for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
			Double[] vArray = values[valueIndex];
			int actualValPos;
			int lineIterator = valueAxisPos + descSegment / 2;
			List<Point> points = new ArrayList<Point>();
			for (Double v : vArray) {
				actualValPos = (int) calculateValuePos(v, valueSegment);
				if (xIsDescription) {
					points.add(new Point(lineIterator, sourceAxisPos - actualValPos));
				}
				else {
					points.add(new Point(sourceAxisPos + actualValPos, lineIterator));
				}
				lineIterator += descSegment;
			}

			if (cIndex >= colors.size()) {
				cIndex = 0; // Restart with first color if all colors in the array has been used
			}
			base.setForegroundColor(currentTheme.forStringOrNull(colors.get(cIndex), Transparency.FOREGROUND));
			base.setBackgroundColor(currentTheme.forStringOrNull(colors.get(cIndex), Transparency.FOREGROUND));

			if (line) {
				for (int i = 0; i < points.size() - 1; i++) {
					Point point1 = points.get(i);
					Point point2 = points.get(i + 1);
					base.drawLine(point1.x, point1.y, point2.x, point2.y);
				}
			}
			else {
				for (int i = 0; i < points.size(); i++) {
					Point point = points.get(i);
					base.drawCircle(point.x, point.y, 2);
				}
			}
			// print titleCol
			base.setForegroundColor(currentTheme.forStringOrNull(colors.get(cIndex), Transparency.FOREGROUND).darken(75));
			base.print(title[valueIndex], points.get(points.size() - 1).x, points.get(points.size() - 1).y, AlignHorizontal.CENTER);

			cIndex++;
		}

		base.resetColorSettings();
	}

	private final void drawBars(boolean xIsDescription, Double[][] values, int sourceAxisPos, int valueAxisPos, Double valueSegment, int descSegment, List<String> colors) {
		int barLength;
		int valueRowAmount = values.length;
		for (int vIndex = 0; vIndex < valueRowAmount; vIndex++) {
			int cIndex = 0;
			int subBarIterator = valueAxisPos;
			for (Double v : values[vIndex]) {
				if (cIndex >= colors.size()) {
					cIndex = 0; // Restart with first color if all colors in the array has been used
				}
				base.setForegroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.TRANSPARENT));
				base.setBackgroundColorAndKeepTransparency(colors.get(cIndex));

				barLength = (int) calculateValuePos(v, valueSegment);

				int barWidth = 0;
				int ownvar = vIndex * (int) Math.round((double) descSegment / valueRowAmount);
				// calculate last bar width, fixing rounding errors
				if (vIndex == valueRowAmount - 1) {
					barWidth = subBarIterator + descSegment - (subBarIterator + ownvar);
				}
				else {
					barWidth = (int) Math.round((double) descSegment / valueRowAmount);
				}

				if (xIsDescription) {
					if (barLength > 0) {
						base.drawRectangle(subBarIterator + ownvar, sourceAxisPos - barLength, barWidth, barLength);
					}
					else {
						base.drawRectangle(subBarIterator + ownvar, sourceAxisPos, barWidth, -barLength);
					}
				}
				else {
					if (barLength > 0) {
						base.drawRectangle(sourceAxisPos, subBarIterator + ownvar, barLength, barWidth);
					}
					else {
						base.drawRectangle(sourceAxisPos + barLength, subBarIterator + ownvar, -barLength, barWidth);
					}
				}

				subBarIterator += descSegment;
				cIndex++;
			}
		}

		base.resetColorSettings();
	}

	public final void drawPiePlot() {

		Double valueSum = 0.0;
		for (Double v : values[0]) {
			valueSum += Math.abs(v);
		}

		final Point ulCorner;
		final int diameter;

		int height = canvas.getInnerVerticalDrawspace();
		int width = canvas.getInnerHorizontalDrawspace();
		diameter = height > width ? width : height;
		ulCorner = new Point(canvas.getInnerLeftPos(), canvas.getInnerUpPos());
		drawPieArcs(values[0], desc, ulCorner, diameter, valueSum, colors);
	}

	private final void drawPieArcs(Double[] values, String[] desc, Point ulCorner, int diameter, Double valueSum, List<String> colors) {
		Theme currentTheme = ThemeFactory.getCurrentTheme();
		int cIndex = 0;

		Double arcAngle = 0D;
		Double startAngle = 0D;

		for (int i = 0; i < values.length; i++) {
			if (cIndex >= colors.size()) {
				cIndex = 0; // Restart with first color if all colors in the array has been used
			}
			ColorOwn currentFg = base.getForegroundColor();
			base.setForegroundColor(currentTheme.getColor(Theme.PredefinedColors.TRANSPARENT));
			base.setBackgroundColorAndKeepTransparency(colors.get(cIndex));

			arcAngle = i < values.length - 1 ? Math.round(360.0 / valueSum * Math.abs(values[i])) : 360 - startAngle;

			// System.out.println("val: "+values[i]+" winkel: "+arcAngle);

			int height = canvas.getInnerVerticalDrawspace();
			int width = canvas.getInnerHorizontalDrawspace();

			base.drawArc(ulCorner.x + width / 2.0 - diameter / 2.0, ulCorner.y + height / 2.0 - diameter / 2.0, diameter, diameter, startAngle.floatValue(), arcAngle.floatValue(), false);
			base.setForegroundColor(currentFg);

			double radians = (360 - startAngle + (360 - arcAngle / 2)) * Math.PI / 180.0;
			int value_x = (int) (diameter / 2.0 * Math.cos(radians) + ulCorner.x + diameter / 2.0 + width / 2.0 - diameter / 2.0);
			int value_y = (int) (diameter / 2.0 * Math.sin(radians) + ulCorner.y + diameter / 2.0 + height / 2.0 - diameter / 2.0);

			base.setForegroundColor(currentTheme.forStringOrNull(colors.get(cIndex), Transparency.FOREGROUND).darken(75));
			base.print(desc[i], value_x, value_y, AlignHorizontal.CENTER);

			// System.out.println("value_x: "+value_x+" / value_y:"+value_y);

			startAngle += arcAngle;
			cIndex++;
		}

		base.resetColorSettings();
	}

	private void calculateAdditionalSpaceForYAxisTextWidth() {
		double maxWidth = 0;
		double valueWidth;
		if (axisConfig.isxDescription()) { // y-axis contains values
			if (axisConfig.drawValueAxisMarkerText()) {
				for (Double v : valuesShownOnAxisSorted) {
					valueWidth = base.textWidth(String.valueOf(v));
					if (valueWidth > maxWidth) {
						maxWidth = valueWidth;
					}
				}
			}
		}
		else { // y-axis contains description
			if (axisConfig.drawDescriptionAxisMarkerText()) {
				for (String d : desc) {
					valueWidth = base.textWidth(d);
					if (valueWidth > maxWidth) {
						maxWidth = valueWidth;
					}
				}
			}
		}

		double adjustValue = maxWidth + canvas.getOuterLeftPos() - (axisConfig.getyAxisPos() - canvas.getInnerLeftPos()) - 5;
		if (adjustValue > canvas.getOuterLeftPos()) {
			canvas.setBorderX((int) adjustValue);
			setupAxis();
			// If the y-axis is not exactly over the innerleft-border, it will be displaced by the last setupAxis() call and therefore the additional space for it must be recalculated again
			if (axisConfig.getyAxisPos() - canvas.getInnerLeftPos() != 0) {
				adjustValue = maxWidth + canvas.getOuterLeftPos() - (axisConfig.getyAxisPos() - canvas.getInnerLeftPos()) - 5;
				if (adjustValue > canvas.getOuterLeftPos()) {
					canvas.setBorderX((int) adjustValue);
					setupAxis();
				}
			}
		}
	}

	/**
	 * Calculated value * valueSegment but account for displacements of values if all values are positive or negativ (= positive minVal or negative maxVal)
	 */
	public double calculateValuePos(double value, double valueSegment) {
		if (value > 0 && minVal > 0) {
			value -= minVal;
		}
		else if (value < 0 && maxVal < 0) {
			value -= maxVal;
		}
		return value * valueSegment;
	}

	public void setValues(String[] desc, String[] title, Double[][] values, List<String> colors) {
		this.desc = SharedUtils.cloneArray(desc);
		this.title = SharedUtils.cloneArray(title);
		this.colors = new ArrayList<String>(colors);
		this.values = SharedUtils.cloneArray(values);

		valuesSorted = new TreeSet<Double>();
		for (Double[] vArray : values) {
			for (Double v : vArray) {
				valuesSorted.add(v);
			}
		}
		valuesShownOnAxisSorted = axisConfig.setValueAxisList(valuesSorted);

		minVal = minRealOrShownValue();
		maxVal = maxRealOrShownValue();
	}

	public void setMinValue(Double minVal) throws IOException {
		Double limit = Math.min(minRealOrShownValue(), maxVal);
		if (minVal > limit) {
			throw new IOException("minValue must be <= " + limit);
		}
		else {
			this.minVal = minVal;
		}
	}

	public void setMaxValue(Double maxVal) throws IOException {
		Double limit = Math.max(maxRealOrShownValue(), minVal);
		if (maxVal < limit) {
			throw new IOException("maxValue must be >= " + limit);
		}
		else {
			this.maxVal = maxVal;
		}
	}

	private double minRealOrShownValue() {
		if (valuesShownOnAxisSorted.isEmpty()) {
			return valuesSorted.first();
		}
		else {
			return Math.min(valuesSorted.first(), valuesShownOnAxisSorted.first());
		}
	}

	private double maxRealOrShownValue() {
		if (valuesShownOnAxisSorted.isEmpty()) {
			return valuesSorted.last();
		}
		else {
			return Math.max(valuesSorted.last(), valuesShownOnAxisSorted.last());
		}
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public AxisConfig getAxisConfig() {
		return axisConfig;
	}

}
