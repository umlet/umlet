package com.baselet.gwt.client.jsinterop;

import com.baselet.gwt.client.text.Font;
import com.baselet.gwt.client.util.Base64;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.CanvasElement;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * This class is a Wrapper for PDFKit: https://pdfkit.org/
 * Conversions partly adapted from: https://github.com/joshua-gould/canvas2pdf
 */
@JsType(isNative = true, name = "PDFDocument", namespace = JsPackage.GLOBAL)
public class PdfContext {
	@JsOverlay
	private static final int[] UNICODE_TO_REPLACE = new int[] { 9650, 9658, 9660, 9668 }; // u25B2, u25BA, u25BC, u25C4

	private String textAlign;
	private Font textFont;

	public BlobStream stream;

	private JavaScriptObject fontNormal;
	private JavaScriptObject fontBold;
	private JavaScriptObject fontItalic;
	private String lastFontDataNormal;
	private String lastFontDataBold;
	private String lastFontDataItalic;

	public PdfContext() {}

	public PdfContext(PdfOptions pdfOptions) {}

	@JsOverlay
	public final void setFillStyle(FillStrokeStyle fillStyle) {
		String rgba = fillStyle.toString();
		Color color = convertColor(rgba);
		fillColor(color.c, color.a);
	}

	@JsOverlay
	public final void fillRect(double x, double y, double w, double h) {
		this.rect(x, y, (int) w, (int) h);
		this.fill();
	}

	@JsOverlay
	public final void setTransform(int m11, int m12, int m21, int m22, int dx, int dy) {
		transform(m11, m12, m21, m22, dx, dy);
	}

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

		if (textFont.getFontStyle() == null) {
			if (lastFontDataNormal == null || !lastFontDataNormal.equals(FontData.fontNormal)) {
				lastFontDataNormal = FontData.fontNormal;
				fontNormal = Buffer.from(Base64.decode(FontData.fontNormal));
			}

			font(fontNormal, textFont.getFontSize());
		}
		else {
			switch (textFont.getFontStyle()) {
				case BOLD:
					if (lastFontDataBold == null || !lastFontDataBold.equals(FontData.fontBold)) {
						lastFontDataBold = FontData.fontBold;
						fontBold = Buffer.from(Base64.decode(FontData.fontBold));
					}
					font(fontBold, textFont.getFontSize());
					break;
				case ITALIC:
					if (lastFontDataItalic == null || !lastFontDataItalic.equals(FontData.fontItalic)) {
						lastFontDataItalic = FontData.fontItalic;
						fontItalic = Buffer.from(Base64.decode(FontData.fontItalic));
					}
					font(fontItalic, textFont.getFontSize());
					break;
				default:
					if (lastFontDataNormal == null || !lastFontDataNormal.equals(FontData.fontNormal)) {
						lastFontDataNormal = FontData.fontNormal;
						fontNormal = Buffer.from(Base64.decode(FontData.fontBold));
					}
					font(fontNormal, textFont.getFontSize());
					break;
			}
		}
		this.textFont.setFontName(FontData.fontName);
	}

	@JsOverlay
	public final void beginPath() {
		// Do nothing
	}

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

	@JsOverlay
	public final void setTextAlign(Context2d.TextAlign ctxAlign) {
		this.textAlign = ctxAlign.getValue();
	}

	@JsOverlay
	public final void fillText(String text, Double x, Double y) {
		x = this.adjustTextX(text, x);
		y = this.adjustTextY(text, y);

		// Drawing occurrences of specified unicode characters with the standard font
		char[] chars = text.toCharArray();
		int start = 0;
		boolean replacedOnce = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			for (int checkUnicode : UNICODE_TO_REPLACE) {
				if ((int) c == checkUnicode) {
					String textUntilChar = text.substring(start, i);
					if (!replacedOnce) {
						this.text(textUntilChar, x, y, Option.create(false, false, true));
					}
					else {
						this.text(textUntilChar, Option.create(false, false, true));
					}
					replacedOnce = true;
					JavaScriptObject font;
					JavaScriptObject oldFont;
					if (textFont.getFontStyle() != null) {
						switch (textFont.getFontStyle()) {
							case ITALIC:
								font = Buffer.from(Base64.decode(FontData.backupFontItalic));
								oldFont = fontItalic;
								break;
							case BOLD:
								font = Buffer.from(Base64.decode(FontData.backupFontBold));
								oldFont = fontBold;
								break;
							default:
								font = Buffer.from(Base64.decode(FontData.backupFontNormal));
								oldFont = fontNormal;
						}
					}
					else {
						font = Buffer.from(Base64.decode(FontData.backupFontNormal));
						oldFont = fontNormal;
					}
					font(font, textFont.getFontSize());
					this.text(String.valueOf(c), Option.create(false, false, true));
					font(oldFont, textFont.getFontSize());
					start = i + 1;
				}
			}
		}
		if (!replacedOnce) {
			this.text(text, x, y, Option.create(false, false, true));
		}
	}

	@JsOverlay
	public final double adjustTextX(String text, double x) {
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

	public native BlobStream pipe(BlobStream blobStream);

	public native void translate(double x, double y);

	public native void drawImage(CanvasElement image, double dx, double dy);

	public native void clearRect(double x, double y, double w, double h);

	public native void end();

	public native String getSerializedSvg(boolean replaceNamedEntities);

	public native void scale(double scalingFactor, double scalingFactor1);

	public native void save();

	public native void moveTo(double x, double y);

	public native void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise);

	public native void closePath();

	public native void restore();

	public native void fill();

	public native void stroke();

	public native void arc(double x, double y, double radius, double startAngle, double endAngle);

	public native void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y);

	public native void rect(double x, double y, int width, int height);

	public native void lineTo(double x, double y);

	public native void quadraticCurveTo(double cpx, double cpy, double x, double y);

	public native int widthOfString(String text);

	public native void font(String fontName, double fontSize);

	public native void font(JavaScriptObject fontSource, double fontSize);

	public native void text(String text, Double x, Double y, Option option);

	public native void text(String text, Option option);

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
