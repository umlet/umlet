package com.baselet.diagram.draw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;

import com.baselet.control.Constants;
import com.baselet.control.DimensionFloat;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.Dimension;

public class BaseDrawHandlerSwing extends BaseDrawHandler {

	// Save some objects needed to draw the shapes
	protected Graphics2D g2;

	public BaseDrawHandlerSwing() {
		this.fgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		this.bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
	}

	public BaseDrawHandlerSwing(Graphics g, DiagramHandler handler, Color fgColor, Color bgColor, Dimension size) {
		this.fgColor = fgColor;
		this.bgColor= bgColor;
		setHandler(handler);
		setGraphics(g);
		setSize(size);
	}

	@Override
	public void setGraphics(Graphics g) {
		this.g2 = (Graphics2D) g;
		g2.setFont(handler.getFontHandler().getFont());
		g2.setColor(fgColor);
	}

	private float getZoom() {
		return handler.getZoomFactor();
	}
	
	@Override
	public float getDistanceBetweenTexts() {
		return handler.getFontHandler().getDistanceBetweenTexts(false);
	}

	@Override DimensionFloat textDimension(String text) {
		boolean specialFontSize = (style.getFontSize() != getDefaultFontSize());
		if (specialFontSize) {
			handler.getFontHandler().setFontSize(style.getFontSize());
		}
		DimensionFloat returnVal = handler.getFontHandler().getTextSize(text, false);
		if (specialFontSize) {
			handler.getFontHandler().resetFontSize();
		}
		return returnVal;
	}

	@Override
	float getDefaultFontSize() {
		return handler.getFontHandler().getFontSize(false);
	}

	/*
	 * DRAW METHODS
	 */
	@Override
	public void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		addShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.OPEN));
	}

	@Override
	public void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		addShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.CHORD));
	}

	@Override
	public void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		addShape(new Arc2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.PIE));
	}

	@Override
	public void drawCircle(float x, float y, float radius) {
		addShape(new Ellipse2D.Float((x - radius) * getZoom(), (y - radius) * getZoom(), radius * 2 * getZoom(), radius * 2 * getZoom()));
	}

	@Override
	public void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		addShape(new CubicCurve2D.Float(x1 * getZoom(), y1 * getZoom(), ctrlx1 * getZoom(), ctrly1 * getZoom(), ctrlx2 * getZoom(), ctrly2 * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	@Override
	public void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		addShape(new QuadCurve2D.Float(x1 * getZoom(), y1 * getZoom(), ctrlx * getZoom(), ctrly * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	@Override
	public void drawEllipse(float x, float y, float radiusX, float radiusY) {
		addShape(new Ellipse2D.Float((x - radiusX) * getZoom(), (y - radiusY) * getZoom(), radiusX * 2 * getZoom(), radiusY * 2 * getZoom()));
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		addShape(new Line2D.Float(x1 * getZoom(), y1 * getZoom(), x2 * getZoom(), y2 * getZoom()));
	}

	@Override
	public void drawLineHorizontal(float y) {
		addShape(new Line2D.Float(0, y * getZoom(), handler.realignToGrid(false, width * getZoom(), true), y * getZoom()));
	}

	@Override
	public void drawLineVertical(float x) {
		addShape(new Line2D.Float(x * getZoom(), 0, x * getZoom(), handler.realignToGrid(false, height, true)));
	}

	@Override
	public void drawRectangle(float x, float y, float width, float height) {
		addShape(new Rectangle.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom()));
	}

	@Override
	public void drawRectangleRound(float x, float y, float width, float height, float arcw, float arch) {
		addShape(new RoundRectangle2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), arcw * getZoom(), arch * getZoom()));
	}

	@Override
	public void print(String text, float x, float y, AlignHorizontal align) {
		addText(new Text(text, x * getZoom(), y * getZoom(), align));
	}

	protected void addShape(final Shape s) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				drawShape(styleAtDrawingCall, s, g2);
			}
		});
	}

	private void drawShape(Style style, Shape s, Graphics2D g2) {
		// Shapes Background
		g2.setColor(style.getBgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getBgAlpha()));
		g2.fill(s);
		// Shapes Foreground
		g2.setColor(getOverlay().getFgColor() != null ? getOverlay().getFgColor() : style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		g2.setStroke(Utils.getStroke(style.getLineType(), style.getLineThickness()));
		g2.draw(s);
	}

	protected void addText(final Text t) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				drawText(styleAtDrawingCall, t, g2, handler);
			}
		});
	}

	private void drawText(Style style, Text t, Graphics2D g2, DiagramHandler handler) {
		g2.setColor(getOverlay().getFgColor() != null ? getOverlay().getFgColor() : style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		handler.getFontHandler().setFontSize(style.getFontSize());
		g2.setFont(handler.getFontHandler().getFont());
		handler.getFontHandler().writeText(g2, t.getText(), t.getX(), t.getY(), t.getHorizontalAlignment());
		handler.getFontHandler().resetFontSize();

	}
	/*
	 * DEPRECATED PRINT METHODS (ONLY USED FROM CUSTOM ELEMENTS AND PLOTGRID)
	 */
	
	@Deprecated
	public final void printLeft(String text, float y) {
		print(text, y, AlignHorizontal.LEFT);
	}
	
	@Deprecated
	public final void printRight(String text, float y) {
		print(text, y, AlignHorizontal.RIGHT);
	}

	@Deprecated
	public final void printCenter(String text, float y) {
		print(text, y, AlignHorizontal.CENTER);
	}

	private final void print(String text, float y, AlignHorizontal align) {
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
}
