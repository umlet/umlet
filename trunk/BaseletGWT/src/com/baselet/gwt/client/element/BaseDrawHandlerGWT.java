package com.baselet.gwt.client.element;

import com.baselet.control.StringStyle;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.FormatLabels;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.geom.DimensionFloat;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.gwt.client.Converter;
import com.baselet.gwt.client.Notification;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class BaseDrawHandlerGWT extends BaseDrawHandler {

	private float HALF_PX = 0.5f;

	private static final int DEFAULT_FONTSIZE = 12;
	Canvas canvas;
	Context2d ctx;

	public BaseDrawHandlerGWT(Canvas canvas) {
		this.canvas = canvas;
		this.ctx = canvas.getContext2d();
	}

	@Override
	public float getDistanceBetweenTexts() { // TODO copied from swing but should use real font size
		return getDefaultFontSize() / 4;
	}

	//TOD
	@Override
	protected DimensionFloat textDimension(String string) { // TODO height is not implemented at the moment - impl infos at http://www.html5canvastutorials.com/tutorials/html5-canvas-text-metrics/
		ctxSetFont(style.getFontSize(), StringStyle.analyseStyle(string));
		DimensionFloat dim = new DimensionFloat((float) ctx.measureText(string).getWidth(), style.getFontSize());
		return dim;
	}

	@Override
	protected float getDefaultFontSize() {
		return DEFAULT_FONTSIZE;
	}

	@Override
	public BaseDrawHandler getPseudoDrawHandler() {
		PseudoDrawHandlerGWT pseudo = new PseudoDrawHandlerGWT(canvas);
		pseudo.setStyle(style); // set style to make sure fontsize (and therefore calls like this.textHeight()) work as intended
		return pseudo;
	}

	@Override
	public void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawCircle(final float x, final float y, final float radius) {
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
	public void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawEllipse(final float x, final float y, final float width, final float height) {
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
	public void drawLine(final float x1, final float y1, final float x2, final float y2) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				setStyle(ctx, styleAtDrawingCall);
				drawLineHelper(x1, y1, x2, y2);
			}
		});
	}

	@Override
	public void drawRectangle(final float x, final float y, final float width, final float height) {
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
	public void drawRectangleRound(float x, float y, float width, float height, float arcw, float arch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(final String text, final float x, final float y, final AlignHorizontal align) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				ColorOwn fgColor = getOverlay().getFgColor() != null ? getOverlay().getFgColor() : styleAtDrawingCall.getFgColor();
				ctx.setFillStyle(Converter.convert(fgColor));
				drawTextHelper(text, x, y, align, styleAtDrawingCall.getFontSize());
			}
		});
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}

	public void clearCanvas() {
		CanvasElement el = canvas.getCanvasElement();
		ctx.clearRect(0, 0, el.getWidth(), el.getWidth());
	}

	private void drawTextHelper(final String text, final float x, final float y, AlignHorizontal align, float fontSize) {
		StringStyle stringStyle = StringStyle.analyseStyle(text);
		
		ctxSetFont(fontSize, stringStyle);

		String textToDraw = stringStyle.getStringWithoutMarkup();
		if (stringStyle.getFormat().contains(FormatLabels.UNDERLINE)) {
			ctx.setLineWidth(1.0f);
			setLineDash(ctx, LineType.SOLID, 1.0f);
			drawLineHelper(x, y, x + textWidth(textToDraw), y);
		}

		ctxSetTextAlign(align);
		ctx.fillText(textToDraw, x, y);
	}

	private void ctxSetFont(float fontSize, StringStyle stringStyle) {
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
	private void drawEllipseHelper(Context2d ctx, float x, float y, float w, float h) {
		float kappa = .5522848f;
		float ox = (w / 2) * kappa; // control point offset horizontal
		float oy = (h / 2) * kappa; // control point offset vertical
		float xe = x + w;           // x-end
		float ye = y + h;           // y-end
		float xm = x + w / 2;       // x-middle
		float ym = y + h / 2;       // y-middle

		ctx.beginPath();
		ctx.moveTo(x, ym);
		ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y);
		ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym);
		ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym);
		ctx.fill();
		ctx.stroke();
	}

	private void drawLineHelper(final float x1, final float y1, final float x2, final float y2) {
		ctx.beginPath();
		ctx.moveTo(x1 + HALF_PX, y1 + HALF_PX); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
		ctx.lineTo(x2 + HALF_PX, y2 + HALF_PX);
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

	private void setLineDash(Context2d ctx, LineType lineType, float lineThickness) {
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
			Notification.showFeatureNotSupported("To see dashed lines, please use Firefox or Chrome<br/>In your browser they will be shown as solid lines");
		}
	}

	/**
	 * Chrome supports setLineDash()
	 * Firefox supports mozDash()
	 */
	public final native void setLineDash(Context2d ctx, float dash) /*-{
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
