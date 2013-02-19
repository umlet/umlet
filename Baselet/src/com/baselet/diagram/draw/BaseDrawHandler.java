package com.baselet.diagram.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.LineType;
import com.baselet.control.DimensionFloat;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.FontHandler.FormatLabels;

public class BaseDrawHandler {
	protected DiagramHandler handler;

	// Save some objects needed to draw the shapes
	protected Graphics2D g2;
	protected Style style;

	protected Color bgColor;
	protected Color fgColor;

	protected float width;
	protected float height;

	private DrawableCache drawables = new DrawableCache();

	public BaseDrawHandler() {
		this.fgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		this.bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
	}

	public BaseDrawHandler(Graphics g, DiagramHandler handler, Color fgColor, Color bgColor, Dimension size) {
		this.fgColor = fgColor;
		this.bgColor= bgColor;
		setHandler(handler);
		setGraphics(g);
		setSize(size);
	}

	public void setHandler(DiagramHandler handler) {
		this.handler = handler;
		this.style = new Style();
		resetStyle();
	}

	public void setGraphics(Graphics g) {
		this.g2 = (Graphics2D) g;
		g2.setFont(handler.getFontHandler().getFont());
		g2.setColor(fgColor);
	}

	public void setSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
	}

	private float getZoom() {
		return handler.getZoomFactor();
	}

	protected void addShape(Shape s) {
		drawables.add(new Drawable(style.cloneFromMe(), s));
	}

	protected void addText(Text t) {
		drawables.add(new Drawable(style.cloneFromMe(), t));
	}

	public void drawAll() {
		drawables.drawAll(g2, handler);
	}

	public void drawAll(boolean isSelected) {
		if (isSelected) {
			drawables.setForegroundOverlay(Constants.DEFAULT_SELECTED_COLOR);
		} else {
			drawables.removeForegroundOverlay();
		}
		drawables.drawAll(g2, handler);
	}

	public void clearCache() {
		drawables.clear();
	}

	/*
	 * TEXT METHODS
	 */

	public final void print(String text, float x, float y, AlignHorizontal align) {
		addText(new Text(text, x * getZoom(), y * getZoom(), align));
	}

	public final void print(String text, float y, AlignHorizontal align) {
		float x;
		if (align == AlignHorizontal.LEFT) {
			x = handler.getFontHandler().getDistanceBetweenTexts(false);
		} else if (align == AlignHorizontal.CENTER) {
			x = width / 2;
		} else /*if (align == AlignHorizontal.RIGHT)*/ {
			x = (int) (width - handler.getFontHandler().getDistanceBetweenTexts(false));
		}
		print(text, x, y, align);
	}

	public final void printLeft(String text, float y) {
		print(text, y, AlignHorizontal.LEFT);
	}

	public final void printRight(String text, float y) {
		print(text, y, AlignHorizontal.RIGHT);
	}

	public final void printCenter(String text, float y) {
		print(text, y, AlignHorizontal.CENTER);
	}

	public final float textHeight() {
		return textDimension("dummy").getHeight();
	}

	public final float textHeightWithSpace() {
		return textHeight() + 2;
	}
	
	public final float getDistanceBetweenTexts() {
		return handler.getFontHandler().getDistanceBetweenTexts(false);
	}

	public final float textWidth(String text) {
		return textDimension(text).getWidth();
	}

	private final DimensionFloat textDimension(String text) {
		boolean specialFontSize = (style.getFontSize() != handler.getFontHandler().getFontSize(false));
		if (specialFontSize) {
			handler.getFontHandler().setFontSize(style.getFontSize());
		}
		DimensionFloat returnVal = handler.getFontHandler().getTextSize(text, false);
		if (specialFontSize) {
			handler.getFontHandler().resetFontSize();
		}
		return returnVal;
	}

	/*
	 * DRAW METHODS
	 */
	public final void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		addShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.OPEN));
	}

	public final void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		addShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.CHORD));
	}

	public final void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		addShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.PIE));
	}

	public final void drawCircle(float x, float y, float radius) {
		addShape(new Ellipse2D.Float((x - radius) * getZoom(), (y - radius) * getZoom(), radius * 2 * getZoom(), radius * 2 * getZoom()));
	}

	public final void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		addShape(new CubicCurve2D.Float(x1 * getZoom(), y1 * getZoom(), ctrlx1 * getZoom(), ctrly1 * getZoom(), ctrlx2 * getZoom(), ctrly2 * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	public final void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		addShape(new QuadCurve2D.Float(x1 * getZoom(), y1 * getZoom(), ctrlx * getZoom(), ctrly * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	public final void drawEllipse(float x, float y, float radiusX, float radiusY) {
		addShape(new Ellipse2D.Float((x - radiusX) * getZoom(), (y - radiusY) * getZoom(), radiusX * 2 * getZoom(), radiusY * 2 * getZoom()));
	}

	public final void drawLine(float x1, float y1, float x2, float y2) {
		addShape(new Line2D.Float(x1 * getZoom(), y1 * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	public final void drawLineHorizontal(float y) {
		addShape(new Line2D.Float(0, y * getZoom(), handler.realignToGrid(false, width * getZoom(), true), y * getZoom()));
	}

	public final void drawLineVertical(float x) {
		addShape(new Line2D.Float(x * getZoom(), 0, x * getZoom(), handler.realignToGrid(false, height, true)));
	}

	public final void drawPolygon(Polygon polygon) {
		for (int i = 0; i < polygon.xpoints.length; i++) {
			polygon.xpoints[i] *= getZoom();
		}
		for (int i = 0; i < polygon.ypoints.length; i++) {
			polygon.ypoints[i] *= getZoom();
		}
		addShape(polygon);
	}

	public final void drawRectangle(float x, float y, float width, float height) {
		addShape(new Rectangle.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom()));
	}

	public final void drawRectangleRound(float x, float y, float width, float height, float arcw, float arch) {
		addShape(new RoundRectangle2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), arcw * getZoom(), arch * getZoom()));
	}

	/*
	 * STYLING METHODS
	 */
	public final void setForeground(String color, float alpha) {
		setForegroundColor(color);
		setForegroundAlpha(alpha);
	}

	public final void setForeground(Color color, float alpha) {
		setForegroundColor(color);
		setForegroundAlpha(alpha);
	}

	public final void setBackground(String color, float alpha) {
		setBackgroundColor(color);
		setBackgroundAlpha(alpha);
	}

	public final void setBackground(Color color, float alpha) {
		setBackgroundColor(color);
		setBackgroundAlpha(alpha);
	}

	public final void setForegroundColor(String color) {
		if (color.equals("fg")) setForegroundColor(fgColor);
		else setForegroundColor(Utils.getColor(color)); // if fgColor is not a valid string null will be set
	}

	public final void setForegroundColor(Color color) {
		if (color == null) style.setFgColor(Constants.DEFAULT_FOREGROUND_COLOR);
		else style.setFgColor(color);
	}

	public final void setBackgroundColor(String color) {
		if (color.equals("bg")) setBackgroundColor(bgColor);
		else setBackgroundColor(Utils.getColor(color));
	}

	public final void setBackgroundColor(Color color) {
		if (color == null) style.setBgColor(Constants.DEFAULT_BACKGROUND_COLOR);
		else style.setBgColor(color);
	}

	public final void setForegroundAlpha(float alpha) {
		style.setFgAlpha(alpha);
	}

	public final void setBackgroundAlpha(float alpha) {
		style.setBgAlpha(alpha);
	}

	public void resetColorSettings() {
		setForeground("fg", Constants.ALPHA_NO_TRANSPARENCY);
		setBackground("bg", Constants.ALPHA_FULL_TRANSPARENCY);
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

		if (FormatLabels.BOLD.equals(type)) style.setLineThickness(2.0f);
	}
	
	public final void setLineThickness(float lineThickness) {
		style.setLineThickness(lineThickness);
	}

	public void resetStyle() {
		resetColorSettings();
		style.setFontSize(handler.getFontHandler().getFontSize(false));
		style.setLineType(LineType.SOLID);
		style.setLineThickness(1.0f);
	}
	
	public Style getCurrentStyle() {
		return style.cloneFromMe();
	}
	
	public void setCurrentStyle(Style style) {
		this.style = style;
	}

	/**
	 * exists to apply all facet operations without real drawing (eg: necessary to calculate space which is needed for autoresize)
	 */
	public PseudoDrawHandler getPseudoDrawHandler() {
		PseudoDrawHandler counter = new PseudoDrawHandler();
		counter.setHandler(handler);
		counter.width = this.width;
		counter.height = this.height;
		return counter;
	}
}
