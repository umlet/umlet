package com.baselet.gwt.client.jsinterop;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.CanvasElement;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name = "canvas2pdf.PdfContext", namespace = JsPackage.GLOBAL)
public class PdfContext {
	Object fillStyle;
	Object textAlign;
	Object strokeStyle;
	Object lineWidth;
	String font;
	public BlobStream stream;

	int width;
	int height;

	public PdfContext(BlobStream blobstream) {}

	@JsOverlay
	public final void setFillStyle(FillStrokeStyle fillStyle) {
		this.fillStyle = fillStyle;
	}

	public native void translate(double x, double y);

	public native void drawImage(CanvasElement image, double dx, double dy);

	public native void fillRect(double x, double y, double w, double h);

	public native void clearRect(double x, double y, double w, double h);

	public native void end();

	public native String getSerializedSvg(boolean replaceNamedEntities);

	public native void setTransform(int i, int i1, int i2, int i3, int i4, int i5);

	public native void scale(double scalingFactor, double scalingFactor1);

	@JsOverlay
	public final String getFont() {
		return this.font;
	}

	public native TextMetrics measureText(String text);

	@JsOverlay
	public final void setFont(String font) {
		this.font = font;
	}

	public native void save();

	public native void beginPath();

	public native void moveTo(double x, double y);

	public native void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise);

	public native void closePath();

	public native void restore();

	public native void fill();

	public native void stroke();

	public native void arc(double x, double y, double radius, double startAngle, double endAngle);

	@JsOverlay
	public final void setStrokeStyle(FillStrokeStyle strokeStyle) {
		this.strokeStyle = strokeStyle;
	}

	@JsOverlay
	public final void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public native void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y);

	@JsOverlay
	public final void setTextAlign(Context2d.TextAlign ctxAlign) {
		this.textAlign = ctxAlign.getValue();
	}

	public native void fillText(String text, Double x, Double y);

	public native void rect(double x, double y, int width, int height);

	public native void lineTo(double x, double y);

	public native void quadraticCurveTo(double cpx, double cpy, double x, double y);

	@JsOverlay
	public final int getWidth() {
		return width;
	}

	@JsOverlay
	public final void setWidth(int width) {
		this.width = width;
	}

	@JsOverlay
	public final int getHeight() {
		return height;
	}
	@JsOverlay
	public final void setHeight(int height) {
		this.height = height;
	}
}
