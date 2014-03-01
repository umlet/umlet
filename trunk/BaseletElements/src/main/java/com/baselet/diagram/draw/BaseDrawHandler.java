package com.baselet.diagram.draw;

import java.util.ArrayList;
import java.util.Collection;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Lines;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.diagram.draw.helper.StyleException;
import com.umlet.element.experimental.facets.defaults.BackgroundColorFacet;
import com.umlet.element.experimental.facets.defaults.ForegroundColorFacet;

public abstract class BaseDrawHandler {

	private ColorOwn bgDefaultColor;
	private ColorOwn fgDefaultColor;

	protected Style style = new Style();

	private ArrayList<DrawFunction> drawables = new ArrayList<DrawFunction>();
	private Style overlay = new Style();

	public BaseDrawHandler() {
		this.fgDefaultColor = ColorOwn.DEFAULT_FOREGROUND;
		this.bgDefaultColor = ColorOwn.DEFAULT_BACKGROUND;
	}

	public void setFgDefaultColor(ColorOwn fgDefaultColor) {
		this.fgDefaultColor = fgDefaultColor;
	}

	public void setBgDefaultColor(ColorOwn bgDefaultColor) {
		this.bgDefaultColor = bgDefaultColor;
	}

	protected Style getOverlay() {
		return overlay;
	}

	protected void addDrawable(DrawFunction drawable) {
		drawables.add(drawable);
	}

	public void drawAll(boolean isSelected) {
		if (isSelected) {
			overlay.setFgColor(ColorOwn.SELECTION_FG);
		} else {
			overlay.setFgColor(null);
		}
		drawAll();
	}

	public void clearCache() {
		drawables.clear();
	}

	public final double textHeightWithSpace() {
		return textHeight() + 2;
	}

	public final double textHeight() {
		return textDimension("dummy").getHeight();
	}

	public final double textWidth(String text) {
		return textDimension(text).getWidth();
	}

	public final void setForegroundColor(String color) {
		if (color.equals(ForegroundColorFacet.KEY)) setForegroundColor(fgDefaultColor);
		else setForegroundColor(ColorOwn.forString(color, Transparency.FOREGROUND)); // if fgColor is not a valid string null will be set
	}

	public final void setForegroundColor(ColorOwn color) {
		if (color == null) style.setFgColor(ColorOwn.DEFAULT_FOREGROUND);
		else style.setFgColor(color);
	}

	public final void setBackgroundColor(String color) {
		if (color.equals(BackgroundColorFacet.KEY)) setBackgroundColor(bgDefaultColor);
		else setBackgroundColor(ColorOwn.forString(color, Transparency.BACKGROUND));
	}

	public final void setBackgroundColor(ColorOwn color) {
		if (color == null) style.setBgColor(ColorOwn.DEFAULT_BACKGROUND);
		else style.setBgColor(color);
	}

	public void resetColorSettings() {
		setForegroundColor(ForegroundColorFacet.KEY);
		setBackgroundColor(BackgroundColorFacet.KEY);
	}

	public final void setFontSize(double fontSize) {
		assertDoubleRange(fontSize);
		style.setFontSize(fontSize);
	}

	public final void setLineType(LineType type) {
		style.setLineType(type);
	}

	public final void setLineThickness(double lineThickness) {
		assertDoubleRange(lineThickness);
		style.setLineThickness(lineThickness);
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
		style.setLineThickness(1);
	}

	public Style getCurrentStyle() {
		return style.cloneFromMe();
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public void drawAll() {
		for (DrawFunction d : drawables) {
			d.run();
		}
	}

	public double getDistanceHorizontalBorderToText() {
		return 3;
	}

	/*
	 * HELPER METHODS
	 */

	protected abstract DimensionDouble textDimension(String string);
	protected abstract double getDefaultFontSize();
	public abstract BaseDrawHandler getPseudoDrawHandler();

	/*
	 * DRAW METHODS
	 */
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
	public void drawLines(Line ... lines) {
		drawLines(Lines.toPoints(lines));
	}
	public void print(String text, double x, double y, AlignHorizontal align) {
		print(text, new PointDouble(x,y), align);
	}
	public abstract void drawArcPie(double x, double y, double width, double height, double start, double extent);
	public abstract void drawCircle(double x, double y, double radius);
	public abstract void drawEllipse(double x, double y, double width, double height);
	public abstract void drawLines(PointDouble ... points);
	public abstract void drawRectangle(double x, double y, double width, double height);
	public abstract void drawRectangleRound(double x, double y, double width, double height, double radius);
	public abstract void print(String text, PointDouble point, AlignHorizontal align);
}
