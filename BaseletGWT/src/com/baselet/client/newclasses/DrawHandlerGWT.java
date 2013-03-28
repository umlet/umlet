package com.baselet.client.newclasses;

import com.baselet.client.copy.control.enumerations.AlignHorizontal;
import com.baselet.client.copy.diagram.draw.BaseDrawHandler;
import com.baselet.client.copy.diagram.draw.DrawFunction;
import com.baselet.client.copy.diagram.draw.geom.DimensionFloat;
import com.baselet.client.copy.diagram.draw.helper.ColorOwn;
import com.baselet.client.copy.diagram.draw.helper.Style;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;

public class DrawHandlerGWT extends BaseDrawHandler {

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
	public void drawEllipse(float x, float y, float radiusX, float radiusY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawLine(final float x1, final float y1, final float x2, final float y2) {
		final Style styleAtDrawingCall = style.cloneFromMe();
		addDrawable(new DrawFunction() {
			@Override
			public void run() {
				Context2d ctx = canvas.getContext2d();
				drawLineHelper(ctx, styleAtDrawingCall.getFgColor(), x1, y1, x2, y2);
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
				ctx.fillRect(x + 0.5, y + 0.5, width, height);
				// Shapes Foreground
				ctx.setFillStyle(Converter.convert(styleAtDrawingCall.getFgColor()));
				ctx.rect(x + 0.5, y + 0.5, width, height);
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
	
	private void drawLineHelper(Context2d context, ColorOwn color, float x1, float y1, float x2, float y2) {
		context.beginPath();
		context.setFillStyle(Converter.convert(color));
		context.moveTo(x1 + 0.5, y2 + 0.5); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
		context.lineTo(x2 + 0.5, y2 + 0.5);
		context.stroke();
	}
}
