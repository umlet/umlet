package com.baselet.gwt.client.jsinterop;

import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.dom.client.CanvasElement;

public class Context2dPdfWrapper implements Context2dWrapper {

	private static final CustomLogger logger = CustomLoggerFactory.getLogger(Context2dPdfWrapper.class);

	private final PdfContext pdfContext;
	private final RealPdfContext realPdfContext;
	private final SvgContext svgContext;

	public SvgContext getSvgContext() {
		return svgContext;
	}

	public Context2dPdfWrapper(PdfContext pdfContext, SvgContext svgContext, RealPdfContext realPdfContext) {
		this.pdfContext = pdfContext;
		this.svgContext = svgContext;
		this.realPdfContext = realPdfContext;
	}

	@Override
	public void setFillStyle(FillStrokeStyle fillStyle) {
		realPdfContext.setFillStyle(fillStyle);
	}

	@Override
	public void translate(double x, double y) {
		realPdfContext.translate(x, y);
	}

	@Override
	public void drawImage(CanvasElement image, double dx, double dy) {
		realPdfContext.drawImage(image, dx, dy);
	}

	@Override
	public void fillRect(double x, double y, double w, double h) {
		realPdfContext.fillRect(x, y, w, h);
	}

	@Override
	public void clearRect(double x, double y, double w, double h) {
		realPdfContext.clearRect(x, y, w, h);
	}

	@Override
	public void setTransform(int m11, int m12, int m21, int m22, int dx, int dy) {
		realPdfContext.setTransform(m11, m12, m21, m22, dx, dy);
	}

	@Override
	public void scale(double scalingFactor, double scalingFactor1) {
		realPdfContext.scale(scalingFactor, scalingFactor);
	}

	@Override
	public String getFont() {
		return realPdfContext.getFont();
	}

	@Override
	public double measureText(String text) {
		return realPdfContext.measureText(text);
	}

	@Override
	public void setFont(String font) {
		realPdfContext.setFont(font);
	}

	@Override
	public void save() {
		realPdfContext.save();
	}

	@Override
	public void beginPath() {
		realPdfContext.beginPath();
	}

	@Override
	public void moveTo(double x, double y) {
		realPdfContext.moveTo(x, y);
	}

	@Override
	public void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise) {
		realPdfContext.arc(x, y, radius, startAngle, endAngle, anticlockwise);
	}

	@Override
	public void closePath() {
		realPdfContext.closePath();
	}

	@Override
	public void restore() {
		realPdfContext.restore();
	}

	@Override
	public void fill() {
		realPdfContext.fill();
	}

	@Override
	public void stroke() {
		realPdfContext.stroke();
	}

	@Override
	public void arc(double x, double y, double radius, double startAngle, double endAngle) {
		realPdfContext.arc(x, y, radius, startAngle, endAngle);
	}

	@Override
	public void setStrokeStyle(FillStrokeStyle strokeStyle) {
		realPdfContext.setStrokeStyle(strokeStyle);
	}

	@Override
	public void setLineWidth(double lineWidth) {
		realPdfContext.setLineWidth(lineWidth);
	}

	@Override
	public void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {
		realPdfContext.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
	}

	@Override
	public void setTextAlign(Context2d.TextAlign ctxAlign) {
		realPdfContext.setTextAlign(ctxAlign);
	}

	@Override
	public void fillText(String text, Double x, Double y) {
		realPdfContext.fillText(text, x, y);
	}

	@Override
	public void rect(double x, double y, int width, int height) {
		realPdfContext.rect(x, y, width, height);
	}

	@Override
	public void lineTo(double x, double y) {
		realPdfContext.lineTo(x, y);
	}

	@Override
	public void quadraticCurveTo(double cpx, double cpy, double x, double y) {
		realPdfContext.quadraticCurveTo(cpx, cpy, x, y);
	}

	@Override
	public void setLineDash(double dash) {
		realPdfContext.setLineDash(dash);
	}

	public void fillAndStroke() {
		realPdfContext.fillAndStroke();
	}
}
