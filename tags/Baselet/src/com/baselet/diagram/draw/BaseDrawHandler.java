package com.baselet.diagram.draw;

import java.awt.AlphaComposite;
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
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;

public class BaseDrawHandler {
	private DiagramHandler handler;

	// Save some objects needed to draw the shapes
	private Graphics2D g2;
	private Style style;

	private Color bgColor;
	private Color fgColor;

	private int width;
	private int height;

	private boolean isSelected;

	public BaseDrawHandler(Graphics g, DiagramHandler handler, Color fgColor, Color bgColor, Dimension size, boolean isSelected) {
		this.g2 = (Graphics2D) g;
		this.handler = handler;
		this.fgColor  = fgColor;
		this.bgColor = bgColor;
		g2.setFont(handler.getFontHandler().getFont());
		g2.setColor(fgColor);
		this.style = new Style(fgColor, bgColor, (int) handler.getFontHandler().getFontSize(false));
		this.width = size.width;
		this.height = size.height;
		this.isSelected = isSelected;
	}

	private float getZoom() {
		return handler.getZoomFactor();
	}

	private void drawShape(Shape s) {
		// Shapes Background
		g2.setColor(style.getBgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getBgAlpha()));
		this.g2.fill(s);
		// Shapes Foreground
		g2.setColor(style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		g2.setStroke(Utils.getStroke(style.getLineType(), style.getLineThickness()));
		this.g2.draw(s);
	}

	private void drawFilledShape(Shape s) {
		// Shapes Background
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		g2.setColor(style.getFgColor());
		g2.fillRoundRect(s.getBounds().x, s.getBounds().y, s.getBounds().width, s.getBounds().height, s.getBounds().width, s.getBounds().height);
		// Shapes Foreground
		g2.setColor(style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		this.g2.draw(s);
	}

	private void drawText(Text t) {
		g2.setColor(style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		handler.getFontHandler().setFontSize(style.getFontSize());
		g2.setFont(handler.getFontHandler().getFont());
		handler.getFontHandler().writeText(this.g2, t.getText(), t.getX(), t.getY(), t.getHorizontalAlignment());
		handler.getFontHandler().resetFontSize();
	}

	/*
	 * TEXT METHODS
	 */

	public final void print(String text, int x, int y) {
		drawText(new Text(text, (int) (x * getZoom()), (int) (y * getZoom()), AlignHorizontal.LEFT));
	}

	public final void print(String text, int x, int y, AlignHorizontal align) {
		drawText(new Text(text, (int) (x * getZoom()), (int) (y * getZoom()), align));
	}

	public final void printLeft(String text, int y) {
		drawText(new Text(text, (int) handler.getFontHandler().getDistanceBetweenTexts(), (int) (y * getZoom()), AlignHorizontal.LEFT));
	}

	public final void printRight(String text, int y) {
		drawText(new Text(text, (int) (width - handler.getFontHandler().getDistanceBetweenTexts()), (int) (y * getZoom()) + 20, AlignHorizontal.RIGHT));
	}

	public final void printCenter(String text, int y) { drawText(new Text(text, width / 2, (int) (y * getZoom()), AlignHorizontal.CENTER));
	}

	public final int textHeight() {
		return textDimension("dummy").height;
	}

	public final int textWidth(String text) {
		return textDimension(text).width;
	}

	private final Dimension textDimension(String text) {
		boolean specialFontSize = (style.getFontSize() != (int) handler.getFontHandler().getFontSize(false));
		if (specialFontSize) {
			handler.getFontHandler().setFontSize(style.getFontSize());
		}
		Dimension returnVal = handler.getFontHandler().getTextSize(text, false);
		if (specialFontSize) {
			handler.getFontHandler().resetFontSize();
		}
		return returnVal;
	}

	/*
	 * DRAW METHODS
	 */
	public final void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		drawShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.OPEN));
	}

	public final void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		drawShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.CHORD));
	}

	public final void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		drawShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.PIE));
	}

	public final void drawCircle(int x, int y, int radius) {
		drawFilledShape(new Ellipse2D.Float((int) ((x - radius) * getZoom()), (int) ((y - radius) * getZoom()), (int) (radius * 2 * getZoom()), (int) (radius * 2 * getZoom())));
	}

	public final void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		drawShape(new CubicCurve2D.Float(x1 * getZoom(), y1 * getZoom(), ctrlx1 * getZoom(), ctrly1 * getZoom(), ctrlx2 * getZoom(), ctrly2 * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	public final void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		drawShape(new QuadCurve2D.Float(x1 * getZoom(), y1 * getZoom(), ctrlx * getZoom(), ctrly * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	public final void drawEllipse(int x, int y, int radiusX, int radiusY) {
		drawShape(new Ellipse2D.Float((int) ((x - radiusX) * getZoom()), (int) ((y - radiusY) * getZoom()), (int) (radiusX * 2 * getZoom()), (int) (radiusY * 2 * getZoom())));
	}

	public final void drawLine(int x1, int y1, int x2, int y2) {
		drawShape(new Line2D.Float((int) (x1 * getZoom()), (int) (y1 * getZoom()), (int) (x2 * getZoom()), (int) (y2 * getZoom())));
	}

	public final void drawLineHorizontal(int y) {
		drawShape(new Line2D.Float((int) (0 * getZoom()), (int) (y * getZoom()), handler.realignToGrid(false, width, true), (int) (y * getZoom())));
	}

	public final void drawLineVertical(int x) {
		drawShape(new Line2D.Float((int) (x * getZoom()), (int) (0 * getZoom()), (int) (x * getZoom()), handler.realignToGrid(false, height, true)));
	}

	public final void drawPolygon(Polygon polygon) {
		for (int i = 0; i < polygon.xpoints.length; i++) {
			polygon.xpoints[i] *= getZoom();
		}
		for (int i = 0; i < polygon.ypoints.length; i++) {
			polygon.ypoints[i] *= getZoom();
		}
		drawShape(polygon);
	}

	public final void drawRectangle(int x, int y, int width, int height) {
		drawShape(new Rectangle((int) (x * getZoom()), (int) (y * getZoom()), (int) (width * getZoom()), (int) (height * getZoom())));
	}

	public final void drawRectangleRound(int x, int y, int width, int height, float arcw, float arch) {
		drawShape(new RoundRectangle2D.Float((int) (x * getZoom()), (int) (y * getZoom()), (int) (width * getZoom()), (int) (height * getZoom()), arcw * getZoom(), arch * getZoom()));
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
		if (isSelected) style.setFgColor(Constants.DEFAULT_SELECTED_COLOR);
		else if (color == null) style.setFgColor(Constants.DEFAULT_FOREGROUND_COLOR);
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

	public final void setFontSize(int fontSize) {
		style.setFontSize(fontSize);
	}
}
