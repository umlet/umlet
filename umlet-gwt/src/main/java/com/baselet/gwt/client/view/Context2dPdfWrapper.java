package com.baselet.gwt.client.view;

import com.baselet.control.StringStyle;
import com.baselet.control.enums.FormatLabels;
import com.baselet.gwt.client.jsinterop.PdfContext;
import com.baselet.gwt.client.text.Font;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.dom.client.CanvasElement;

public class Context2dPdfWrapper implements Context2dWrapper {

	private final PdfContext pdfContext;

	public Context2dPdfWrapper(PdfContext pdfContext) {
		this.pdfContext = pdfContext;
	}

	@Override
	public void setFillStyle(FillStrokeStyle fillStyle) {
		pdfContext.setFillStyle(fillStyle);
	}

	@Override
	public void translate(double x, double y) {
		pdfContext.translate(x, y);
	}

	@Override
	public void drawImage(CanvasElement image, double dx, double dy) {
		pdfContext.drawImage(image, dx, dy);
	}

	@Override
	public void fillRect(double x, double y, double w, double h) {
		pdfContext.fillRect(x, y, w, h);
	}

	@Override
	public void clearRect(double x, double y, double w, double h) {
		pdfContext.clearRect(x, y, w, h);
	}

	@Override
	public void setTransform(int m11, int m12, int m21, int m22, int dx, int dy) {
		pdfContext.setTransform(m11, m12, m21, m22, dx, dy);
	}

	@Override
	public void scale(double x, double y) {
		pdfContext.scale(x, y);
	}

	@Override
	public Font getFont() {
		return pdfContext.getFont();
	}

	@Override
	public double measureText(String text) {
		return pdfContext.measureText(text);
	}

	@Override
	public void setFont(double fontSize, StringStyle stringStyle) {
		Font font = new Font();

		FormatLabels fontStyle = null;
		if (stringStyle.getFormat().contains(FormatLabels.BOLD)) {
			fontStyle = FormatLabels.BOLD;
		}
		if (stringStyle.getFormat().contains(FormatLabels.ITALIC)) {
			fontStyle = FormatLabels.ITALIC;
		}
		font.setFontStyle(fontStyle);
		font.setFontSize(fontSize);
		pdfContext.setFont(font);
	}

	@Override
	public void setFont(Font font) {
		pdfContext.setFont(font);
	}

	@Override
	public void save() {
		pdfContext.save();
	}

	@Override
	public void beginPath() {
		pdfContext.beginPath();
	}

	@Override
	public void moveTo(double x, double y) {
		pdfContext.moveTo(x, y);
	}

	@Override
	public void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise) {
		pdfContext.arc(x, y, radius, startAngle, endAngle, anticlockwise);
	}

	@Override
	public void closePath() {
		pdfContext.closePath();
	}

	@Override
	public void restore() {
		pdfContext.restore();
	}

	@Override
	public void fill() {
		pdfContext.fill();
	}

	@Override
	public void stroke() {
		pdfContext.stroke();
	}

	@Override
	public void arc(double x, double y, double radius, double startAngle, double endAngle) {
		pdfContext.arc(x, y, radius, startAngle, endAngle);
	}

	@Override
	public void setStrokeStyle(FillStrokeStyle strokeStyle) {
		pdfContext.setStrokeStyle(strokeStyle);
	}

	@Override
	public void setLineWidth(double lineWidth) {
		pdfContext.setLineWidth(lineWidth);
	}

	@Override
	public void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {
		pdfContext.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
	}

	@Override
	public void setTextAlign(Context2d.TextAlign ctxAlign) {
		pdfContext.setTextAlign(ctxAlign);
	}

	@Override
	public void fillText(String text, Double x, Double y) {
		pdfContext.fillText(text, x, y);
	}

	@Override
	public void rect(double x, double y, int width, int height) {
		pdfContext.rect(x, y, width, height);
	}

	@Override
	public void lineTo(double x, double y) {
		pdfContext.lineTo(x, y);
	}

	@Override
	public void quadraticCurveTo(double cpx, double cpy, double x, double y) {
		pdfContext.quadraticCurveTo(cpx, cpy, x, y);
	}

	@Override
	public void setLineDash(double dash) {
		pdfContext.setLineDash(dash);
	}

	public void fillAndStroke() {
		pdfContext.fillAndStroke();
	}
}
