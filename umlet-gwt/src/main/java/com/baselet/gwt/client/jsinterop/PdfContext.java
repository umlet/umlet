package com.baselet.gwt.client.jsinterop;

import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.text.Font;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.dom.client.CanvasElement;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name = "PDFDocument", namespace = JsPackage.GLOBAL)
public class PdfContext {
	String textAlign;
	Font textFont;
	public BlobStream stream;

	public PdfContext() {}

	public PdfContext(PdfOptions pdfOptions) {}

	public native BlobStream pipe(BlobStream blobStream);

	@JsOverlay
	public final void setFillStyle(FillStrokeStyle fillStyle) {
		String rgba = fillStyle.toString();
		Color color = convertColor(rgba);
		fillColor(color.c, color.a);
	}

	public native void translate(double x, double y);

	public native void drawImage(CanvasElement image, double dx, double dy);

	@JsOverlay
	public final void fillRect(double x, double y, double w, double h) {
		this.rect(x, y, (int) w, (int) h);
		this.fill();
	}

	public native void clearRect(double x, double y, double w, double h);

	public native void end();

	public native String getSerializedSvg(boolean replaceNamedEntities);

	@JsOverlay
	public final void setTransform(int m11, int m12, int m21, int m22, int dx, int dy) {
		transform(m11, m12, m21, m22, dx, dy);
	}

	public native void scale(double scalingFactor, double scalingFactor1);

	@JsOverlay
	public final Font getFont() {
		if (textFont == null) {
			this.textFont = new Font("Helvetica", null, 12);
		}
		return this.textFont;
	}

	@JsOverlay
	public final double measureText(String text) {
		return widthOfString(text);
	}

	@JsOverlay
	public final void setFont(Font textFont) {
		this.textFont = textFont;
		String fontName = textFont.getFontDescription();
		if (textFont.getFontStyle() != null && !textFont.getFontStyle().equals("")) {
			fontName += "-" + textFont.getFontStyle();
		}
		font(fontName, textFont.getFontSize());
	}

	public native void save();

	@JsOverlay
	public final void beginPath() {
		// Do nothing
	}

	public native void moveTo(double x, double y);

	public native void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise);

	public native void closePath();

	public native void restore();

	public native void fill();

	public native void stroke();

	public native void arc(double x, double y, double radius, double startAngle, double endAngle);

	@JsOverlay
	public final void setStrokeStyle(FillStrokeStyle strokeStyle) {
		String rgba = strokeStyle.toString();
		Color color = convertColor(rgba);
		strokeColor(color.c, color.a);
	}

	@JsOverlay
	public final void setLineWidth(double lineWidth) {
		this.lineWidth(lineWidth);
	}

	public native void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y);

	@JsOverlay
	public final void setTextAlign(Context2d.TextAlign ctxAlign) {
		this.textAlign = ctxAlign.getValue();
	}

	@JsOverlay
	public final void fillText(String text, Double x, Double y) {
		x = this.adjustTextX(text, x);
		y = this.adjustTextY(text, y);
		this.text(text, x, y, Option.create(false, false, true));
	}

	public native void rect(double x, double y, int width, int height);

	public native void lineTo(double x, double y);

	public native void quadraticCurveTo(double cpx, double cpy, double x, double y);

	public native int widthOfString(String text);

	@JsOverlay
	public final double adjustTextX(String text, double x) {
		// TODO: MAYBE FIRST && SHOULD BE || (--> FIRST IF IS IRRELEVANT)
		if (!this.textAlign.equals("start") && !this.textAlign.equals("left")) {
			double width = widthOfString(text);
			if (this.textAlign.equals("right") || this.textAlign.equals("end")) {
				x -= width;
			}
			else if (this.textAlign.equals("center")) {
				x -= (width / 2);
			}
		}
		return x;
	}

	@JsOverlay
	public final double adjustTextY(String text, double y) {
		double height = currentLineHeight(true);
		y -= (height / 2) + 1;
		return y;
	}

	@JsOverlay
	public final void setLineDash(double dash) {
		if (dash > 0) {
			dash(dash);
		}
		else {
			undash();
		}
	}

	@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
	public static class Option {
		boolean lineBreak;
		boolean stroke;
		boolean fill;

		@JsOverlay
		public static Option create(boolean lineBreak, boolean stroke, boolean fill) {
			final Option option = new Option();
			option.lineBreak = lineBreak;
			option.stroke = stroke;
			option.fill = fill;
			return option;
		}
	}

	@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
	public static class Color {
		String c;
		String a;

		@JsOverlay
		public static Color create(String colorHex, String alpha) {
			final Color color = new Color();
			color.c = colorHex;
			color.a = alpha;
			return color;
		}
	}

	@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
	public static class PdfOptions {
		int[] size;
		Margins margins;

		@JsOverlay
		public static PdfOptions create(int[] size, Margins margins) {
			final PdfOptions pdfOptions = new PdfOptions();
			assert size.length == 2;
			pdfOptions.size = size;
			pdfOptions.margins = margins;
			return pdfOptions;
		}

		@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
		public static class Margins {
			int top;
			int bottom;
			int left;
			int right;

			@JsOverlay
			public static Margins create(int top, int bottom, int left, int right) {
				final Margins margins = new Margins();
				margins.top = top;
				margins.bottom = bottom;
				margins.left = left;
				margins.right = right;
				return margins;
			}
		}
	}

	@JsOverlay
	private Color convertColor(String rgbaString) {
		String[] splits = rgbaString.split(",");
		Integer[] rgb = new Integer[3];
		double a = 1;
		for (int i = 0; i < splits.length; i++) {
			if (i == 0) {
				rgb[0] = Integer.parseInt(splits[i].substring(5).trim());
			}
			else if (i == 1) {
				rgb[1] = Integer.parseInt(splits[i].trim());
			}
			else if (i == 2) {
				rgb[2] = Integer.parseInt(splits[i].trim());
			}
			else if (i == 3) {
				a = Double.parseDouble(splits[i].substring(0, splits[i].length() - 1).trim());
			}
		}

		StringBuilder stringBuilder = new StringBuilder("#");
		for (Integer color : rgb) {
			if (color < 16) {
				stringBuilder.append("0");
			}
			stringBuilder.append(Integer.toHexString(color));
		}

		return Color.create(stringBuilder.toString(), String.valueOf(a));
	}

	public native void font(String fontName, double fontSize);

	public native void text(String text, Double x, Double y, Option option);

	public native double currentLineHeight(boolean includeGap);

	public native void fillColor(Object color, Object a);

	public native void strokeColor(Object color, Object a);

	public native void lineWidth(double lineWidth);

	public native void fillAndStroke();

	public native void dash(double dash);

	public native void undash();

	public native void transform(int m11, int m12, int m21, int m22, int dx, int dy);

	public native void fontSize(double fontSize);
}
