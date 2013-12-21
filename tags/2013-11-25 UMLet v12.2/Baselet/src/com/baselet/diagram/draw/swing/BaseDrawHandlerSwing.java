package com.baselet.diagram.draw.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.diagram.draw.helper.Text;

public class BaseDrawHandlerSwing extends BaseDrawHandler {

	private Graphics2D g2;

	protected DiagramHandler handler;
	
	private boolean translate; //is used because pdf and svg export cut lines if they are drawn at (0,0)

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

	private double getZoom() {
		return handler.getZoomFactor();
	}

	@Override
	public DimensionDouble textDimension(String text) {
		boolean specialFontSize = (style.getFontSize() != getDefaultFontSize());
		if (specialFontSize) {
			handler.getFontHandler().setFontSize(style.getFontSize());
		}
		DimensionDouble returnVal = handler.getFontHandler().getTextSize(text, false);
		if (specialFontSize) {
			handler.getFontHandler().resetFontSize();
		}
		return returnVal;
	}

	@Override
	public double getDefaultFontSize() {
		return handler.getFontHandler().getFontSize(false);
	}

	/**
	 * exists to apply all facet operations without real drawing (eg: necessary to calculate space which is needed for autoresize)
	 */
	@Override
	public PseudoDrawHandlerSwing getPseudoDrawHandler() {
		PseudoDrawHandlerSwing counter = new PseudoDrawHandlerSwing();
		counter.setHandler(handler);
		counter.setStyle(style); // set style to make sure fontsize (and therefore calls like this.textHeight()) work as intended
		return counter;
	}

	/*
	 * DRAW METHODS
	 */
//	@Override
//	public void drawArcOpen(double x, double y, double width, double height, double start, double extent) {
//		addShape(new Arc2D.Double(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.OPEN));
//	}
//
//	@Override
//	public void drawArcChord(double x, double y, double width, double height, double start, double extent) {
//		addShape(new Arc2D.Double(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.CHORD));
//	}

	@Override
	public void drawArcPie(double x, double y, double width, double height, double start, double extent) {
		addShape(new Arc2D.Double(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), start, extent, Arc2D.PIE));
	}

	@Override
	public void drawCircle(double x, double y, double radius) {
		addShape(new Ellipse2D.Double((x - radius) * getZoom(), (y - radius) * getZoom(), radius * 2 * getZoom(), radius * 2 * getZoom()));
	}

//	@Override
//	public void drawCurveCubic(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
//		addShape(new CubicCurve2D.Double(x1 * getZoom(), y1 * getZoom(), ctrlx1 * getZoom(), ctrly1 * getZoom(), ctrlx2 * getZoom(), ctrly2 * getZoom(), x2 * getZoom(), y2 * getZoom()));
//	}
//
//	@Override
//	public void drawCurveQuad(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
//		addShape(new QuadCurve2D.Double(x1 * getZoom(), y1 * getZoom(), ctrlx * getZoom(), ctrly * getZoom(), x2 * getZoom(), y2 * getZoom()));
//	}

	@Override
	public void drawEllipse(double x, double y, double width, double height) {
		addShape(new Ellipse2D.Double(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom()));
	}

	@Override
	public void drawLines(PointDouble ... points) {
		if (points.length > 0) {
			Path2D.Double path = new Path2D.Double();
			boolean first = true;
			for (PointDouble p : points) {
				if (first) {
					path.moveTo(Double.valueOf(p.getX() * getZoom()), Double.valueOf(p.getY() * getZoom()));
					first = false;
				} else {
					path.lineTo(Double.valueOf(p.getX() * getZoom()), Double.valueOf(p.getY() * getZoom()));
				}
			}
			// only fill if first point == lastpoint
			boolean fillShape = points[0].equals(points[points.length-1]);
			addShape(path, fillShape);
		}
	}

	@Override
	public void drawRectangle(double x, double y, double width, double height) {
		addShape(new Rectangle.Double(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom()));
	}

//	@Override
//	public void drawRectangleRound(double x, double y, double width, double height, double arcw, double arch) {
//		addShape(new RoundRectangle2D.Double(x * getZoom(), y * getZoom(), width * getZoom(), height * getZoom(), arcw * getZoom(), arch * getZoom()));
//	}

	@Override
	public void print(String text, PointDouble point, AlignHorizontal align) {
		addText(new Text(text, point.x * getZoom(), point.y * getZoom(), align));
	}
	
	protected void addShape(final Shape s) {
		addShape(s, true);
	}

	protected void addShape(final Shape s, final boolean fillShape) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				drawShape(styleAtDrawingCall, s, fillShape);
			}
		});
	}

	private void drawShape(Style style, Shape s, boolean fillShape) {
		if (fillShape) {
			// Shapes Background
			g2.setColor(Converter.convert(style.getBgColor()));
			g2.fill(s);
		}
		// Shapes Foreground
		ColorOwn colOwn = getOverlay().getFgColor() != null ? getOverlay().getFgColor() : style.getFgColor();
		g2.setColor(Converter.convert(colOwn));
		g2.setStroke(Utils.getStroke(style.getLineType(), (float) style.getLineThickness()));
		if (translate) {
			double xTranslation = s.getBounds().x == 0 ? Constants.EXPORT_DISPLACEMENT : 0;
			double yTranslation = s.getBounds().y == 0 ? Constants.EXPORT_DISPLACEMENT : 0;
			g2.translate(xTranslation, yTranslation);
		}
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
	
	public void setTranslate(boolean translate) {
		this.translate = translate;
	}
}
