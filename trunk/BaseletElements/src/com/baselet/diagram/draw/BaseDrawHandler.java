package com.baselet.diagram.draw;

import java.util.ArrayList;
import java.util.Collection;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Style;

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
		if (color.equals("fg")) setForegroundColor(fgDefaultColor);
		else setForegroundColor(ColorOwn.forString(color, Transparency.FOREGROUND)); // if fgColor is not a valid string null will be set
	}

	public final void setForegroundColor(ColorOwn color) {
		if (color == null) color = ColorOwn.DEFAULT_FOREGROUND;
		else style.setFgColor(color);
	}

	public final void setBackgroundColor(String color) {
		if (color.equals("bg")) setBackgroundColor(bgDefaultColor);
		else setBackgroundColor(ColorOwn.forString(color, Transparency.BACKGROUND));
	}

	public final void setBackgroundColor(ColorOwn color) {
		if (color == null) color = ColorOwn.DEFAULT_BACKGROUND;
		else style.setBgColor(color);
	}

	public void resetColorSettings() {
		setForegroundColor("fg");
		setBackgroundColor("bg");
	}

	public final void setFontSize(float fontSize) {
		style.setFontSize(fontSize);
	}

	public final void setFontSize(String fontSize) {
		if (fontSize != null) {
			try {
				setFontSize(Float.valueOf(fontSize));
			} catch (NumberFormatException e) {/*do nothing*/}
		}
	}
	
	public final void setLineType(LineType type) {
		style.setLineType(type);
	}

	public final void setLineType(String type) {
		for (LineType lt : LineType.values()) {
			if (lt.getValue().equals(type)) {
				style.setLineType(lt);
			}
		}

		if (LineType.BOLD.getValue().equals(type)) style.setLineThickness(2.0f);
	}
	
	public final void setLineThickness(float lineThickness) {
		style.setLineThickness(lineThickness);
	}
	
	public void resetStyle() {
		resetColorSettings();
		style.setFontSize(getDefaultFontSize());
		style.setLineType(LineType.SOLID);
		style.setLineThickness(1.0f);
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

	public float getDistanceHorizontalBorderToText() {
		return 3;
	}
	
	/*
	 * HELPER METHODS
	 */

	protected abstract DimensionDouble textDimension(String string);
	protected abstract float getDefaultFontSize();
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
		drawLine(new PointDouble(x1, y1), new PointDouble(x2, y2));
	}
	public void drawLine(Collection<PointDouble> points) {
		drawLine(points.toArray(new PointDouble[points.size()]));
	}
	public void print(String text, double x, double y, AlignHorizontal align) {
		print(text, new PointDouble(x,y), align);
	}
	public abstract void drawArcOpen(float x, float y, float width, float height, float start, float extent);
	public abstract void drawArcChord(float x, float y, float width, float height, float start, float extent);
	public abstract void drawArcPie(float x, float y, float width, float height, float start, float extent);
	public abstract void drawCircle(double x, double y, double radius);
	public abstract void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2);
	public abstract void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2);
	public abstract void drawEllipse(float x, float y, float width, float height);
	public abstract void drawLine(PointDouble ... points);
	public abstract void drawRectangle(double x, double y, double width, double height);
	public abstract void drawRectangleRound(float x, float y, float width, float height, float arcw, float arch);
	public abstract void print(String text, PointDouble point, AlignHorizontal align);
}
