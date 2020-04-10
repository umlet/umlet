package com.baselet.diagram.draw.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.element.interfaces.GridElement;

public class DrawHandlerSwing extends DrawHandler {

	private Graphics2D g2;

	protected DiagramHandler handler;

	private boolean translate; // is used because pdf and svg export cut lines if they are drawn at (0,0)

	private final GridElement gridElement;

	public DrawHandlerSwing(GridElement gridElement) {
		super();
		this.gridElement = gridElement;
	}

	/**
	 * Java Swing JComponents have a width of w, but only w-1 pixels are drawable
	 * Therefore to draw a rectangle around the whole element, you must call g.drawRect(0,0,w-1,h-1)
	 * To avoid this displacement pixel at every draw method, this method ensures you can never draw outside of the right component border
	 * x is also important for width drawings which don't start at 0 (e.g. Deployment "3-dimensional" Rectangle)
	 */
	private double inBorderHorizontal(double width, double x) {
		return Math.min(gridElement.getRectangle().getWidth() - x - 1, width);
	}

	/**
	 * same as above but for vertical points
	 */
	private double inBorderVertical(double height, double y) {
		return Math.min(gridElement.getRectangle().getHeight() - y - 1, height);
	}

	public void setHandler(DiagramHandler handler) {
		this.handler = handler;
		style = new Style();
		resetStyle();
	}

	public void setGraphics(Graphics g) {
		g2 = (Graphics2D) g;
	}

	private double getZoom() {
		return handler.getZoomFactor();
	}

	@Override
	public DimensionDouble textDimensionHelper(StringStyle singleLine) {
		boolean specialFontSize = style.getFontSize() != getDefaultFontSize();
		if (specialFontSize) {
			handler.getFontHandler().setFontSize(style.getFontSize());
		}
		DimensionDouble returnVal = handler.getFontHandler().getTextSize(singleLine, false);
		if (specialFontSize) {
			handler.getFontHandler().resetFontSize();
		}
		return returnVal;
	}

	@Override
	public double getDefaultFontSize() {
		return handler.getFontHandler().getFontSize(false);
	}

	/* DRAW METHODS */
	@Override
	public void drawArc(double x, double y, double width, double height, double start, double extent, boolean open) {
		double xZoomed = x * getZoom() + HALF_PX;
		double yZoomed = y * getZoom() + HALF_PX;
		int arcType = open ? Arc2D.OPEN : Arc2D.PIE;
		addShape(new Arc2D.Double(xZoomed, yZoomed, inBorderHorizontal(width * getZoom(), xZoomed), inBorderVertical(height * getZoom(), yZoomed), start, extent, arcType));
	}

	@Override
	public void drawCircle(double x, double y, double radius) {
		double widthAndHeight = radius * 2;
		drawEllipse(x - radius, y - radius, widthAndHeight, widthAndHeight);
	}

	@Override
	public void drawEllipse(double x, double y, double width, double height) {
		double xZoomed = x * getZoom() + HALF_PX;
		double yZoomed = y * getZoom() + HALF_PX;
		addShape(new Ellipse2D.Double(xZoomed, yZoomed, inBorderHorizontal(width * getZoom(), xZoomed), inBorderVertical(height * getZoom(), yZoomed)));
	}

	@Override
	public void drawLines(PointDouble... points) {
		if (points.length > 0) {
			Path2D.Double path = new Path2D.Double();
			boolean first = true;
			boolean fillShape = false;
			int lastIdx = points.length - 1;
			for (int i = 0; i < points.length; i++) {
				PointDouble p = points[i];
				Double x = inBorderHorizontal(Double.valueOf(p.getX() * getZoom() + HALF_PX), 0);
				Double y = inBorderVertical(Double.valueOf(p.getY() * getZoom() + HALF_PX), 0);
				if (first) {
					path.moveTo(x, y);
					first = false;
				}
				// if this is the last point AND the first and last points are equal, close the path and fill the shape
				else if (i == lastIdx && points[0].equals(points[lastIdx])) {
					path.closePath();
					fillShape = true;
				}
				else {
					path.lineTo(x, y);
				}
			}
			addShape(path, fillShape);
		}
	}

	@Override
	public void drawRectangle(double x, double y, double width, double height) {
		double xZoomed = x * getZoom() + HALF_PX;
		double yZoomed = y * getZoom() + HALF_PX;
		addShape(new Rectangle.Double(xZoomed, yZoomed, inBorderHorizontal(width * getZoom(), xZoomed), inBorderVertical(height * getZoom(), yZoomed)));
	}

	@Override
	public void drawRectangleRound(double x, double y, double width, double height, double radius) {
		double rad = radius * 2 * getZoom();
		double xZoomed = x * getZoom() + HALF_PX;
		double yZoomed = y * getZoom() + HALF_PX;
		addShape(new RoundRectangle2D.Double(xZoomed, yZoomed, inBorderHorizontal(width * getZoom(), xZoomed), inBorderVertical(height * getZoom(), yZoomed), rad, rad));
	}

	@Override
	public void printHelper(StringStyle[] text, PointDouble point, AlignHorizontal align) {
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
			g2.setColor(Converter.convert(style.getBackgroundColor()));
			g2.fill(s);
		}
		if (translate) {
			double xTranslation = s.getBounds().x == 0 ? Constants.EXPORT_DISPLACEMENT : 0;
			double yTranslation = s.getBounds().y == 0 ? Constants.EXPORT_DISPLACEMENT : 0;
			g2.translate(xTranslation, yTranslation);
		}
		if (style.getLineWidth() > 0) {
			// Shapes Foreground
			ColorOwn colOwn = getOverlay().getForegroundColor() != null ? getOverlay().getForegroundColor() : style.getForegroundColor();
			g2.setColor(Converter.convert(colOwn));
			g2.setStroke(Utils.getStroke(style.getLineType(), (float) style.getLineWidth()));
			g2.draw(s);
		}
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
		ColorOwn col = getOverlay().getForegroundColor() != null ? getOverlay().getForegroundColor() : style.getForegroundColor();
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
