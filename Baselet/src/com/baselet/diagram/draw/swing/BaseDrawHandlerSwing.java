package com.baselet.diagram.draw.swing;

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

import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.geom.DimensionFloat;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.diagram.draw.helper.Text;

public class BaseDrawHandlerSwing extends BaseDrawHandler {

	private Graphics2D g2;

	protected DiagramHandler handler;

	public BaseDrawHandlerSwing() {
		super();
	}

	public BaseDrawHandlerSwing(Graphics g, DiagramHandler handler, ColorOwn fgColor, ColorOwn bgColor) {
		super();
		setFgDefaultColor(fgColor);
		setBgDefaultColor(bgColor);
		setHandler(handler);
		setGraphics(g);
	}
	
	public void setHandler(DiagramHandler handler) {
		this.handler = handler;
		this.style = new Style();
		resetStyle();
	}

	public void setGraphics(Graphics g) {
		this.g2 = (Graphics2D) g;
	}

	private float getZoom() {
		return handler.getZoomFactor();
	}

	@Override
	public float getDistanceBetweenTexts() {
		return handler.getFontHandler().getDistanceBetweenTexts(false);
	}

	@Override
	public DimensionFloat textDimension(String text) {
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
	public float getDefaultFontSize() {
		return handler.getFontHandler().getFontSize(false);
	}

	/**
	 * exists to apply all facet operations without real drawing (eg: necessary to calculate space which is needed for autoresize)
	 */
	public PseudoDrawHandlerSwing getPseudoDrawHandler() {
		PseudoDrawHandlerSwing counter = new PseudoDrawHandlerSwing();
		counter.setHandler(handler);
		return counter;
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
	public void drawEllipse(float x, float y, float width, float height) {
		addShape(new Ellipse2D.Float(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom()));
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		addShape(new Line2D.Float(x1 * getZoom(), y1 * getZoom(), x2 * getZoom(), y2 * getZoom()));
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
				drawShape(styleAtDrawingCall, s);
			}
		});
	}

	private void drawShape(Style style, Shape s) {
		// Shapes Background
		g2.setColor(Converter.convert(style.getBgColor()));
		g2.fill(s);
		// Shapes Foreground
		ColorOwn colOwn = getOverlay().getFgColor() != null ? getOverlay().getFgColor() : style.getFgColor();
		g2.setColor(Converter.convert(colOwn));
		g2.setStroke(Utils.getStroke(style.getLineType(), style.getLineThickness()));
		g2.draw(s);
	}

	protected void addText(final Text t) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				drawText(styleAtDrawingCall, t);
			}
		});
	}

	private void drawText(Style style, Text t) {
		ColorOwn col = getOverlay().getFgColor() != null ? getOverlay().getFgColor() : style.getFgColor();
		g2.setColor(Converter.convert(col));
		handler.getFontHandler().setFontSize(style.getFontSize());
		g2.setFont(handler.getFontHandler().getFont());
		handler.getFontHandler().writeText(g2, t.getText(), t.getX(), t.getY(), t.getHorizontalAlignment());
		handler.getFontHandler().resetFontSize();

	}
}
