package com.baselet.diagram.draw;

import java.util.ArrayList;
import java.util.Collection;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.Lines;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.diagram.draw.helper.StyleException;

public abstract class DrawHandler {

	protected static final double HALF_PX = 0.5f;

	protected Style style = new Style();
	private final Style overlay = new Style();

	private final ArrayList<DrawFunction> drawablesBackground = new ArrayList<DrawFunction>();
	private final ArrayList<DrawFunction> drawablesForeground = new ArrayList<DrawFunction>();

	public static enum Layer {
		Foreground, Background
	}

	private Layer layer = Layer.Background;

	private boolean disableDrawing = false;

	/**
	 * all background elements are drawn before drawing the foreground elements
	 * can be useful e.g. if a printText call is made before a drawRectangle call although it should be placed behind the rectangle
	 */
	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	public void setDisableDrawing(boolean disableDrawing) {
		this.disableDrawing = disableDrawing;
	}

	protected Style getOverlay() {
		return overlay;
	}

	protected void addDrawable(DrawFunction drawable) {
		if (!disableDrawing) {
			if (layer == Layer.Foreground) {
				drawablesForeground.add(drawable);
			}
			else {
				drawablesBackground.add(drawable);
			}
		}
		// if drawing is disabled don't add the DrawFunction to any collection
	}

	public void drawAll(boolean isSelected) {
		if (isSelected) {
			overlay.setForegroundColor(ColorOwn.SELECTION_FG);
		}
		else {
			overlay.setForegroundColor(null);
		}
		drawAll();
	}

	public void clearCache() {
		drawablesBackground.clear();
		drawablesForeground.clear();
	}

	public final double textHeightMaxWithSpace() {
		return textHeightMax() + getDistanceBetweenTextLines();
	}

	public final double textHeightMax() {
		return textDimension("Hy").getHeight(); // "Hy" is a good dummy for a generic max height and depth
	}

	public final double textHeight(String text) {
		return textDimension(text).getHeight();
	}

	public final double textWidth(String text) {
		return textDimension(text).getWidth();
	}

	public final void setForegroundColor(String color) {
		if (color.equals(FacetConstants.FOREGROUND_COLOR_KEY)) {
			setForegroundColor(ColorOwn.DEFAULT_FOREGROUND);
		}
		else {
			setForegroundColor(ColorOwn.forString(color, Transparency.FOREGROUND)); // if fgColor is not a valid string null will be set
		}
	}

	public final void setForegroundColor(ColorOwn color) {
		if (color == null) {
			style.setForegroundColor(ColorOwn.DEFAULT_FOREGROUND);
		}
		else {
			style.setForegroundColor(color);
		}
	}

	public final void setBackgroundColor(String color) {
		if (color.equals(FacetConstants.BACKGROUND_COLOR_KEY)) {
			setBackgroundColor(ColorOwn.DEFAULT_BACKGROUND);
		}
		else {
			setBackgroundColor(ColorOwn.forString(color, Transparency.BACKGROUND));
		}
	}

	public final void setBackgroundColor(ColorOwn color) {
		if (color == null) {
			style.setBackgroundColor(ColorOwn.DEFAULT_BACKGROUND);
		}
		else {
			style.setBackgroundColor(color);
		}
	}

	public ColorOwn getForegroundColor() {
		return style.getForegroundColor();
	}

	public ColorOwn getBackgroundColor() {
		return style.getBackgroundColor();
	}

	public double getLineWidth() {
		return style.getLineWidth();
	}

	public void resetColorSettings() {
		setForegroundColor(FacetConstants.FOREGROUND_COLOR_KEY);
		setBackgroundColor(FacetConstants.BACKGROUND_COLOR_KEY);
	}

	public final void setFontSize(double fontSize) {
		assertDoubleRange(fontSize);
		style.setFontSize(fontSize);
	}

	public double getFontSize() {
		return style.getFontSize();
	}

	public final void setLineType(LineType type) {
		style.setLineType(type);
	}

	public LineType getLineType() {
		return style.getLineType();
	}

	public final void setLineWidth(double lineWidth) {
		assertDoubleRange(lineWidth);
		style.setLineWidth(lineWidth);
	}

	private void assertDoubleRange(double doubleValue) {
		if (doubleValue <= 0 || doubleValue > 100) {
			throw new StyleException("value must be >0 and <=100");
		}
	}

	public void resetStyle() {
		resetColorSettings();
		style.setFontSize(getDefaultFontSize());
		style.setLineType(LineType.SOLID);
		style.setLineWidth(1);
	}

	public Style getStyleClone() {
		return style.cloneFromMe();
	}

	public void setStyle(Style style) {
		this.style = style.cloneFromMe();
	}

	public void drawAll() {
		for (DrawFunction d : drawablesBackground) {
			d.run();
		}
		for (DrawFunction d : drawablesForeground) {
			d.run();
		}
	}

	public double getDistanceBorderToText() {
		return 5;
	}

	public double getDistanceBetweenTextLines() {
		return 3;
	}

	protected DimensionDouble textDimension(String string) {
		return textDimensionHelper(StringStyle.replaceNotEscaped(string));
	}

	/* HELPER METHODS */

	protected abstract DimensionDouble textDimensionHelper(String string);

	protected abstract double getDefaultFontSize();

	/* DRAW METHODS */
	public void drawRectangle(Rectangle rect) {
		drawRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public void drawLine(Line line) {
		drawLine(line.getStart().getX(), line.getStart().getY(), line.getEnd().getX(), line.getEnd().getY());
	}

	public void drawLine(double x1, double y1, double x2, double y2) {
		drawLines(new PointDouble(x1, y1), new PointDouble(x2, y2));
	}

	public void drawLines(Collection<PointDouble> points) {
		drawLines(points.toArray(new PointDouble[points.size()]));
	}

	public void drawLines(Line... lines) {
		drawLines(Lines.toPoints(lines));
	}

	public void print(String text, double x, double y, AlignHorizontal align) {
		print(text, new PointDouble(x, y), align);
	}

	protected String escape(String input) {
		return StringStyle.replaceNotEscaped(input);
	}

	public void print(String text, PointDouble point, AlignHorizontal align) {
		printHelper(StringStyle.replaceNotEscaped(text), point, align);
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param start 0 starts as a horizontal line
	 * @param extent can be up to 360 (extend in degrees from param start)
	 */
	public abstract void drawArc(double x, double y, double width, double height, double start, double extent, boolean open);

	public abstract void drawCircle(double x, double y, double radius);

	public abstract void drawEllipse(double x, double y, double width, double height);

	public abstract void drawLines(PointDouble... points);

	public abstract void drawRectangle(double x, double y, double width, double height);

	public abstract void drawRectangleRound(double x, double y, double width, double height, double radius);

	public abstract void printHelper(String text, PointDouble point, AlignHorizontal align);
}
