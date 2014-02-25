package com.baselet.gwt.client.element;

import org.apache.log4j.Logger;

import com.baselet.control.StringStyle;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.FormatLabels;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.gwt.client.Converter;
import com.baselet.gwt.client.Notification;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;

public class BaseDrawHandlerGWT extends BaseDrawHandler {

	private double HALF_PX = 0.5f;

	private static final Logger log = Logger.getLogger(BaseDrawHandlerGWT.class);

	private Canvas canvas;
	private Context2d ctx;

	public BaseDrawHandlerGWT(Canvas canvas) {
		this.canvas = canvas;
		this.ctx = canvas.getContext2d();
	}

	@Override
	protected DimensionDouble textDimension(String string) {
		ctxSetFont(style.getFontSize(), StringStyle.analyseStyle(string));
		DimensionDouble dim = new DimensionDouble((double) ctx.measureText(string).getWidth(), style.getFontSize());
		return dim;
	}

	@Override
	protected double getDefaultFontSize() {
		return 12;
	}

	@Override
	public PseudoDrawHandlerGWT getPseudoDrawHandler() {
		PseudoDrawHandlerGWT pseudo = new PseudoDrawHandlerGWT(canvas);
		pseudo.setStyle(style.cloneFromMe()); // set style to make sure fontsize (and therefore calls like this.textHeight()) work as intended
		return pseudo;
	}

	@Override
	public void drawArcPie(final double x, final double y, final double width, final double height, final double start, final double extent) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() { //start = alles bisherige, extends = currentval
			@Override
			public void run() {
				setStyle(ctx, styleAtDrawingCall);
				
			    double centerX = x+ width/2 + HALF_PX;
			    double centerY = y + height/2 + HALF_PX;
			    ctx.beginPath();
			    ctx.moveTo(centerX, centerY);
			    ctx.arc(centerX, centerY, width / 2, -Math.toRadians(start), -Math.toRadians(start + extent), true);
			    ctx.closePath();
				ctx.fill();
				ctx.stroke();
			}
		});
	}

	@Override
	public void drawCircle(final double x, final double y, final double radius) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				setStyle(ctx, styleAtDrawingCall);
				ctx.beginPath();
				ctx.arc(x + HALF_PX, y + HALF_PX, radius, 0, 2*Math.PI);
				ctx.fill();
				ctx.stroke();
			}
		});
	}

	@Override
	public void drawEllipse(final double x, final double y, final double width, final double height) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				setStyle(ctx, styleAtDrawingCall);
				drawEllipseHelper(ctx, x+HALF_PX, y+HALF_PX, width, height);
			}
		});
	}

	@Override
	public void drawLines(final PointDouble ... points) {
		if (points.length > 1) {
			final Style styleAtDrawingCall = style.cloneFromMe();
			addDrawable(new DrawFunction() {
				@Override
				public void run() {
					setStyle(ctx, styleAtDrawingCall);
					drawLineHelper(points);
				}
			});
		}
	}

	@Override
	public void drawRectangle(final double x, final double y, final double width, final double height) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				setStyle(ctx, styleAtDrawingCall);
				ctx.fillRect(x + HALF_PX, y + HALF_PX, width, height);
				ctx.beginPath();
				ctx.rect(x + HALF_PX, y + HALF_PX, width, height);
				ctx.stroke();
			}
		});
	}
	
	@Override
	public void drawRectangleRound(final double x, final double y, final double width, final double height, final double radius) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				setStyle(ctx, styleAtDrawingCall);
				drawRoundRectHelper(ctx, x + HALF_PX, y + HALF_PX, width + HALF_PX, height + HALF_PX, radius);
			}
		});
	}


	@Override
	public void print(final String text, final PointDouble point, final AlignHorizontal align) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				PointDouble pToDraw = point.copy(); // must use copy to avoid modification of initial point for future drawings (testcase: set invalid key-value setting to get the error message (which has 2 lines) and then click multiple times on the element and anywhere else (to trigger redraw))
				ColorOwn fgColor = getOverlay().getFgColor() != null ? getOverlay().getFgColor() : styleAtDrawingCall.getFgColor();
				ctx.setFillStyle(Converter.convert(fgColor));
				for (String line : text.split("\n")) {
					drawTextHelper(line, pToDraw, align, styleAtDrawingCall.getFontSize());
					pToDraw.move(0, textHeight());
				}
			}
		});
	}

	private void drawTextHelper(final String text, PointDouble p, AlignHorizontal align, double fontSize) {
		StringStyle stringStyle = StringStyle.analyseStyle(text);

		ctxSetFont(fontSize, stringStyle);

		String textToDraw = stringStyle.getStringWithoutMarkup();
		if (textToDraw == null || textToDraw.isEmpty()) {
			return; // if nothing should be drawn return (some browsers like Opera have problems with ctx.fillText calls on empty strings)
		}

		if (stringStyle.getFormat().contains(FormatLabels.UNDERLINE)) {
			ctx.setLineWidth(1.0f);
			setLineDash(ctx, LineType.SOLID, 1.0f);
			double textWidth = textWidth(textToDraw);
			switch (align) {
				case LEFT:
					drawLineHelper(p, new PointDouble(p.x + textWidth, p.y)); break;
				case CENTER:
					drawLineHelper(new PointDouble(p.x - textWidth/2, p.y), new PointDouble(p.x + textWidth/2, p.y)); break;
				case RIGHT:
					drawLineHelper(new PointDouble(p.x - textWidth, p.y), p); break;
			}
		}

		ctxSetTextAlign(align);
		ctx.fillText(textToDraw, p.x, p.y);
	}

	private void ctxSetFont(double fontSize, StringStyle stringStyle) {
		String htmlStyle = "";
		if (stringStyle.getFormat().contains(FormatLabels.BOLD)) {
			htmlStyle += " bold";
		}
		if (stringStyle.getFormat().contains(FormatLabels.ITALIC)) {
			htmlStyle += " italic";
		}
		ctx.setFont(htmlStyle + " " + fontSize + "px sans-serif");
	}

	private void ctxSetTextAlign(AlignHorizontal align) {
		TextAlign ctxAlign = null;
		switch (align) {
			case LEFT:
				ctxAlign = TextAlign.LEFT; break;
			case CENTER:
				ctxAlign = TextAlign.CENTER; break;
			case RIGHT:
				ctxAlign = TextAlign.RIGHT; break;
		}
		ctx.setTextAlign(ctxAlign);
	}

	/**
	 * based on http://stackoverflow.com/questions/2172798/how-to-draw-an-oval-in-html5-canvas/2173084#2173084
	 */
	private static void drawEllipseHelper(Context2d ctx, double x, double y, double w, double h) {
		double kappa = .5522848f;
		double ox = (w / 2) * kappa; // control point offset horizontal
		double oy = (h / 2) * kappa; // control point offset vertical
		double xe = x + w;           // x-end
		double ye = y + h;           // y-end
		double xm = x + w / 2;       // x-middle
		double ym = y + h / 2;       // y-middle

		ctx.beginPath();
		ctx.moveTo(x, ym);
		ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
		ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
		ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
		ctx.fill();
		ctx.stroke();
	}

	/**
	 * based on http://js-bits.blogspot.co.at/2010/07/canvas-rounded-corner-rectangles.html
	 */
	private static void drawRoundRectHelper(Context2d ctx, final double x, final double y, final double width, final double height, final double radius) {
		ctx.beginPath();
		ctx.moveTo(x + radius, y);
		ctx.lineTo(width - radius, y);
		ctx.quadraticCurveTo(width, y, width, y + radius);
		ctx.lineTo(width, height - radius);
		ctx.quadraticCurveTo(width, height, width - radius, height);
		ctx.lineTo(x + radius, height);
		ctx.quadraticCurveTo(x, height, x, height - radius);
		ctx.lineTo(x, y + radius);
		ctx.quadraticCurveTo(x, y, x + radius, y);
	    ctx.closePath();
		ctx.fill();
		ctx.stroke();
	}

	private void drawLineHelper(PointDouble ... points) {
		ctx.beginPath();
		boolean first = true;
		for (PointDouble point : points) {
			if (first) {
				ctx.moveTo(point.x + HALF_PX, point.y + HALF_PX); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
				first = false;
			}
			ctx.lineTo(point.x + HALF_PX, point.y + HALF_PX);
		}
		if (points[0].equals(points[points.length-1])) {
			ctx.fill(); // only fill if first point == lastpoint
		}
		ctx.stroke();
	}

	private void setStyle(Context2d ctx, Style style) {
		if (style.getBgColor() != null) {
			ctx.setFillStyle(Converter.convert(style.getBgColor()));
		}
		ColorOwn fgColor = getOverlay().getFgColor() != null ? getOverlay().getFgColor() : style.getFgColor();
		if (fgColor != null) {
			ctx.setStrokeStyle(Converter.convert(fgColor));
		}
		ctx.setLineWidth(style.getLineThickness());
		setLineDash(ctx, style.getLineType(), style.getLineThickness());

	}

	private void setLineDash(Context2d ctx, LineType lineType, double lineThickness) {
		try {
			switch (lineType) {
				case DASHED: // large linethickness values need longer dashes
					setLineDash(ctx, 6 * Math.max(1, lineThickness/2));
					break;
				case DOTTED: // minimum must be 2, otherwise the dotting is not really visible
					setLineDash(ctx, Math.max(2, lineThickness));
					break;
				default: // default is a solid line
					setLineDash(ctx, 0);
			}
		} catch (Exception e) {
			log.debug("No browser support for dashed lines", e);
			Notification.showFeatureNotSupported("Dashed and dotted lines are shown as solid lines<br/>To correctly display them, please use Firefox or Chrome", true);
		}
	}

	/**
	 * Chrome supports setLineDash()
	 * Firefox supports mozDash()
	 */
	public final native void setLineDash(Context2d ctx, double dash) /*-{
	    if (ctx.setLineDash !== undefined) {
	    	ctx.setLineDash([dash]);
	    } else if (ctx.mozDash !== undefined) {
			if (dash != 0) {
				ctx.mozDash = [dash];
			} else { // default is null
				ctx.mozDash = null;
			}
		} else if (dash != 0) { // if another line than a solid one should be set and the browser doesn't support it throw an Exception
			throw new Exception();
	    }
  	}-*/;

}
