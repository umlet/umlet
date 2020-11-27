package com.baselet.gwt.client.view;

import com.baselet.control.StringStyle;
import com.baselet.control.enums.FormatLabels;
import com.baselet.gwt.client.jsinterop.FontData;
import com.baselet.gwt.client.text.Font;
import com.google.gwt.canvas.dom.client.*;
import com.google.gwt.dom.client.CanvasElement;

public class Context2dGwtWrapper implements Context2dWrapper {
	private Context2d context2d;

	public Context2dGwtWrapper(Context2d context2d) {
		this.context2d = context2d;
	}

	@Override
	public void setFillStyle(FillStrokeStyle fillStyle) {
		context2d.setFillStyle(fillStyle);
	}

	@Override
	public void translate(double x, double y) {
		context2d.translate(x, y);
	}

	@Override
	public void drawImage(CanvasElement image, double dx, double dy) {
		context2d.drawImage(image, dx, dy);
	}

	@Override
	public void fillRect(double x, double y, double w, double h) {
		context2d.fillRect(x, y, w, h);
	}

	@Override
	public void clearRect(double x, double y, double w, double h) {
		context2d.clearRect(x, y, w, h);
	}

	@Override
	public void setTransform(int m11, int m12, int m21, int m22, int dx, int dy) {
		context2d.setTransform(m11, m12, m21, m22, dx, dy);
	}

	@Override
	public void scale(double x, double y) {
		context2d.scale(x, y);
	}

	@Override
	public Font getFont() {
		String htmlFont = context2d.getFont();
		Font font = new Font();
		if (htmlFont.contains("bold")) {
			font.setFontStyle(FormatLabels.BOLD);
		}
		if (htmlFont.contains("italic")) {
			font.setFontStyle(FormatLabels.ITALIC);
		}

		// The pixel size should be the only number inside the string
		font.setFontSize(Double.parseDouble(htmlFont.replaceAll("\\D+", "")));
		font.setFontName(htmlFont.substring(htmlFont.indexOf(" ") + 1));

		return font;
	}

	@Override
	public double measureText(String text) {
		return context2d.measureText(text).getWidth();
	}

	@Override
	public void setFont(double fontSize, StringStyle stringStyle) {
		String htmlStyle = "";
		if (stringStyle.getFormat().contains(FormatLabels.BOLD)) {
			htmlStyle += " bold";
		}
		if (stringStyle.getFormat().contains(FormatLabels.ITALIC)) {
			htmlStyle += " italic";
		}
		context2d.setFont(htmlStyle + " " + fontSize + "px '" + FontData.fontName + "', sans-serif");
	}

	@Override
	public void setFont(Font font) {
		context2d.setFont(font.getFontStyle() == null ? "" : font.getFontStyle() + " " + font.getFontSize() + "px " + font.getFontName());
	}

	@Override
	public void save() {
		context2d.save();
	}

	@Override
	public void beginPath() {
		context2d.beginPath();
	}

	@Override
	public void moveTo(double x, double y) {
		context2d.moveTo(x, y);
	}

	@Override
	public void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise) {
		context2d.arc(x, y, radius, startAngle, endAngle, anticlockwise);
	}

	@Override
	public void closePath() {
		context2d.closePath();
	}

	@Override
	public void restore() {
		context2d.restore();
	}

	@Override
	public void fill() {
		context2d.fill();
	}

	@Override
	public void stroke() {
		context2d.stroke();
	}

	@Override
	public void arc(double x, double y, double radius, double startAngle, double endAngle) {
		context2d.arc(x, y, radius, startAngle, endAngle);
	}

	@Override
	public void setStrokeStyle(FillStrokeStyle strokeStyle) {
		context2d.setStrokeStyle(strokeStyle);
	}

	@Override
	public void setLineWidth(double lineWidth) {
		context2d.setLineWidth(lineWidth);
	}

	@Override
	public void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y) {
		context2d.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
	}

	@Override
	public void setTextAlign(Context2d.TextAlign ctxAlign) {
		context2d.setTextAlign(ctxAlign);
	}

	@Override
	public void fillText(String text, Double x, Double y) {
		context2d.fillText(text, x, y);
	}

	@Override
	public void rect(double x, double y, int width, int height) {
		context2d.rect(x, y, width, height);
	}

	@Override
	public void lineTo(double x, double y) {
		context2d.lineTo(x, y);
	}

	@Override
	public void quadraticCurveTo(double cpx, double cpy, double x, double y) {
		context2d.quadraticCurveTo(cpx, cpy, x, y);
	}

	/**
	 * Chrome and Firefox 33+ support setLineDash()
	 * Older Firefox version support only mozDash()
	 */
	@Override
	public final native void setLineDash(double dash) /*-{
		var ctx = this.@com.baselet.gwt.client.view.Context2dGwtWrapper::context2d;
		if (ctx.setLineDash !== undefined) {
			if (dash != 0) {
				ctx.setLineDash([ dash ]);
			} else {
			ctx.setLineDash([]); // Firefox 33+ on Linux dont show solid lines if ctx.setLineDash([0]) is used, therefore use empty array which works on every browser
			}
		} else if (ctx.mozDash !== undefined) {
			if (dash != 0) {
				ctx.mozDash = [ dash ];
			} else { // default is null
				ctx.mozDash = null;
			}
		} else if (dash != 0) { // if another line than a solid one should be set and the browser doesn't support it throw an Exception
			throw new Exception();
		}
	}-*/;
}
