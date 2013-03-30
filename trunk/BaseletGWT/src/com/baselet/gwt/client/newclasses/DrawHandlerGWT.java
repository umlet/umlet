package com.baselet.gwt.client.newclasses;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.geom.DimensionFloat;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.CanvasElement;

public class DrawHandlerGWT extends BaseDrawHandler {
	
	private float HALF_PX = 0.5f;

	private static final int DEFAULT_FONTSIZE = 12;
	Canvas canvas;

	public DrawHandlerGWT(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public float getDistanceBetweenTexts() { // TODO copied from swing but should use real font size
		return getDefaultFontSize() / 4;
	}

	@Override
	protected DimensionFloat textDimension(String string) { // TODO height is not implemented at the moment - impl infos at http://www.html5canvastutorials.com/tutorials/html5-canvas-text-metrics/
		DimensionFloat dim = new DimensionFloat((float) canvas.getContext2d().measureText(string).getWidth(), getDefaultFontSize());
		return dim;
	}

	@Override
	protected float getDefaultFontSize() {
		return DEFAULT_FONTSIZE;
	}

	@Override
	public BaseDrawHandler getPseudoDrawHandler() {
		return new PseudoDrawHandlerGWT(canvas);
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
	public void drawCircle(float x, float y, float radius) {
		// TODO Auto-generated method stub

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
				drawEllipseHelper(canvas.getContext2d(), x+HALF_PX, y+HALF_PX, width, height);
			}
		});
	}

	@Override
	public void drawLine(final float x1, final float y1, final float x2, final float y2) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				Context2d ctx = canvas.getContext2d();
				ctx.beginPath();
				ctx.setFillStyle(Converter.convert(styleAtDrawingCall.getFgColor()));
				ctx.setLineWidth(style.getLineThickness());
				setLineDash(ctx, style.getLineType(), style.getLineThickness());
				ctx.moveTo(x1 + HALF_PX, y1 + HALF_PX); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
				ctx.lineTo(x2 + HALF_PX, y2 + HALF_PX);
				ctx.stroke();
			}
		});
	}

	@Override
	public void drawRectangle(final float x, final float y, final float width, final float height) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				// Shapes Background
				Context2d ctx = canvas.getContext2d();
				ctx.setFillStyle(Converter.convert(styleAtDrawingCall.getBgColor()));
				ctx.fillRect(x + HALF_PX, y + HALF_PX, width, height);
				// Shapes Foreground
				ctx.setFillStyle(Converter.convert(styleAtDrawingCall.getFgColor()));
				ctx.setLineWidth(style.getLineThickness());
				setLineDash(ctx, style.getLineType(), style.getLineThickness());
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
	public void print(final String text, final float x, final float y, AlignHorizontal align) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				Context2d ctx = canvas.getContext2d();
				ctx.setFillStyle(Converter.convert(styleAtDrawingCall.getFgColor()));
				ctx.fillText(text, x, y);
				//TODO set other font properties
			}
		});
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}

	public void clearCanvas() {
		CanvasElement el = canvas.getCanvasElement();
		canvas.getContext2d().clearRect(0, 0, el.getWidth(), el.getWidth());
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
		ctx.closePath();
		ctx.stroke();
	}

	private void setLineDash(Context2d ctx, LineType lineType, float lineThickness) {
		if (lineType == LineType.DASHED) { // large linethickness values need longer dashes
			setLineDash(ctx, 6 * Math.max(1, lineThickness/2));
		}
		if (lineType == LineType.DOTTED) { // minimum must be 2, otherwise the dotting is not really visible
			setLineDash(ctx, Math.max(2, lineThickness));
		}
	}

	/**
	 * Chrome supports setLineDash()
	 * Firefox supports mozDash()
	 * Internet Explorer is not supported
	 */
	public final native void setLineDash(Context2d ctx, float dash) /*-{
	    if (ctx.setLineDash !== undefined) ctx.setLineDash([dash]);
		if (ctx.mozDash !== undefined) ctx.mozDash = [dash];
  	}-*/;

}
