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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.LineType;
import com.baselet.control.DimensionFloat;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.standalone.ZoomListener;

public class BaseDrawHandler {
	private DiagramHandler handler;

	// Save some objects needed to draw the shapes
	private Graphics2D g2;
	private Style style;

	private Color bgColor;
	private Color fgColor;

	private float width;
	private float height;

	private boolean isSelected;

	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();

	private boolean wordWrap = false;

	public BaseDrawHandler() {
		this.fgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		this.bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
	}

	public BaseDrawHandler(Graphics g, DiagramHandler handler, Color fgColor, Color bgColor, Dimension size, boolean isSelected) {
		this.fgColor = fgColor;
		this.bgColor= bgColor;
		setHandler(handler);
		setGraphics(g);
		setIsSelected(isSelected);
		setSize(size);
	}

	public void setHandler(DiagramHandler handler) {
		this.handler = handler;
		this.style = new Style(fgColor, bgColor, handler.getFontHandler().getFontSize(false));
	}

	public void setGraphics(Graphics g) {
		this.g2 = (Graphics2D) g;
		g2.setFont(handler.getFontHandler().getFont());
		g2.setColor(fgColor);
	}

	public void setIsSelected(boolean isSelected) {
		if (isSelected) setForegroundColor(Constants.DEFAULT_SELECTED_COLOR);
		this.isSelected = isSelected;
	}

	public void setSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
	}

	private float getZoom() {
		return handler.getZoomFactor();
	}

	private void addShape(Shape s) {
		drawables.add(new Drawable(style.cloneFromMe(), s));
	}

	private void addText(Text t) {
		drawables.add(new Drawable(style.cloneFromMe(), t));
	}

	public void drawAll() {
		for (Drawable d : drawables) {
			if (d.getShape() != null) {
				drawShape(d.getStyle(), d.getShape());
			}
			if (d.getText() != null) {
				drawText(d.getStyle(), d.getText());
			}
		}
	}

	public void clearCache() {
		drawables.clear();
	}

	private void drawShape(Style style, Shape s) {
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

	private void drawText(Style style, Text t) {
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

	public final float print(String text, float x, float y, AlignHorizontal align) {
		return printHelper(text, x, y, align);
	}

	public final float print(String text, float y, AlignHorizontal align) {
		float x;
		if (align == AlignHorizontal.LEFT) {
			x = handler.getFontHandler().getDistanceBetweenTexts(false);
		} else if (align == AlignHorizontal.CENTER) {
			x = width / 2;
		} else /*if (align == AlignHorizontal.RIGHT)*/ {
			x = (int) (width - handler.getFontHandler().getDistanceBetweenTexts(false));
		}
		return printHelper(text, x, y, align);
	}

	public final float printLeft(String text, float y) {
		return print(text, y, AlignHorizontal.LEFT);
	}

	public final float printRight(String text, float y) {
		return print(text, y, AlignHorizontal.RIGHT);
	}

	public final float printCenter(String text, float y) {
		return print(text, y, AlignHorizontal.CENTER);
	}

	private final float printHelper(String text, float x, float y, AlignHorizontal align) {
		float outY = y;
		List<String> list = wordWrap ? Utils.splitString(text, width, handler) : Arrays.asList(new String[] {text});
		for (String s : list) {
			addText(new Text(s, x * getZoom(), outY * getZoom(), align));
			outY += textHeightWithSpace();
		}
		return outY - y;
	}

	public final float textHeight() {
		return textDimension("dummy").getHeight();
	}

	public final float textHeightWithSpace() {
		return textHeight() + 2;
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
		addShape(new Line2D.Float(0 * getZoom(), y * getZoom(), handler.realignToGrid(false, width, true), y * getZoom()));
	}

	public final void drawLineVertical(float x) {
		addShape(new Line2D.Float(x * getZoom(), 0 * getZoom(), x * getZoom(), handler.realignToGrid(false, height, true)));
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

	public final void setFontSize(float fontSize) {
		style.setFontSize(fontSize);
	}

	public final void setLineType(LineType type) {
		style.setLineType(type);
	}

	public final void setLineType(String type) {
		if (".".equals(type)) style.setLineType(LineType.DASHED);
		else style.setLineType(LineType.SOLID);
	}

	public final void setWordWrap(boolean wordWrap) {
		this.wordWrap = wordWrap;
	}
}
