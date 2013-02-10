package com.baselet.diagram;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.DimensionFloat;
import com.baselet.control.Utils;


public class FontHandler {

	/**** TEXT FORMATTING LABELS ****/
	public static class FormatLabels {
		public static final String UNDERLINE = "_";
		public static final String BOLD = "*";
		public static final String ITALIC = "/";
	}

	private DiagramHandler handler;
	private Float fontSize;
	private Float diagramDefaultSize = null; // if "fontsize=..." is uncommented this variable is set

	private String diagramDefaultFontFamily = null;
	private FontRenderContext fontrenderContext = new FontRenderContext(null, true, true);

	public FontHandler(DiagramHandler handler) {
		this.handler = handler;
	}

	public void setFontSize(Float fontsize) {
		this.fontSize = fontsize;
	}

	public void setDiagramDefaultFontFamily(String fontfamily) {
		if (Constants.fontFamilyList.contains(fontfamily)) this.diagramDefaultFontFamily = fontfamily;
	}

	public void resetDiagramDefaultFontFamily() {
		diagramDefaultFontFamily = null;
	}

	private String getDiagramDefaultFontFamily() {
		String returnFontFamily;
		if (diagramDefaultFontFamily != null) returnFontFamily = diagramDefaultFontFamily;
		else returnFontFamily = Constants.defaultFontFamily;

		return returnFontFamily;
	}

	public void setDiagramDefaultFontSize(Float diagramDefaultSize) {
		this.diagramDefaultSize = diagramDefaultSize;
	}

	public void resetFontSize() {
		fontSize = null;
	}

	public void resetDiagramDefaultFontSize() {
		diagramDefaultSize = null;
	}

	public float getFontSize() {
		return getFontSize(true);
	}

	public float getFontSize(boolean applyZoom) {
		Float returnFontSize;
		if (diagramDefaultSize != null) returnFontSize = diagramDefaultSize;
		else if (fontSize != null) returnFontSize = fontSize;
		else returnFontSize = Float.valueOf(Constants.defaultFontsize);

		if (applyZoom) return returnFontSize * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		else return returnFontSize;
	}

	public Font getFont() {
		return getFont(true);
	}

	public Font getFont(boolean applyZoom) {
		return new Font(getDiagramDefaultFontFamily(), Font.PLAIN, (int) getFontSize(applyZoom));
	}

	public float getDistanceBetweenTexts() {
		return getDistanceBetweenTexts(true);
	}

	public float getDistanceBetweenTexts(boolean applyZoom) {
		return getFontSize(applyZoom) / 4;
	}

	public DimensionFloat getTextSize(String s) {
		return getTextSize(s, true);
	}

	public DimensionFloat getTextSize(String s, boolean applyZoom) {
		return Utils.getTextSize(s, getFont(applyZoom), fontrenderContext);
	}

	public float getTextWidth(String s) {
		return getTextWidth(s, true);
	}

	public float getTextWidth(String s, boolean applyZoom) {
		if (s == null) return 0;
		return this.getTextSize(s, applyZoom).getWidth();
	}

	public void writeText(Graphics2D g2, String s, float x, float y, AlignHorizontal align) {
		writeText(g2, s, x, y, align, true);
	}

	public void writeText(Graphics2D g2, String s, float x, float y, AlignHorizontal align, boolean applyZoom) {
		for (String line : s.split("\n")) {
			this.write(g2, line, x, y, align, applyZoom);
			y += g2.getFontMetrics().getHeight();
		}
	}

	private void write(Graphics2D g2, String stringWithFormatLabels, float x, float y, AlignHorizontal align, boolean applyZoom) {
		if (stringWithFormatLabels == null || stringWithFormatLabels.isEmpty()) return;
		float fontSize = getFontSize(applyZoom);
		FormattedFont formattedFont = new FormattedFont(stringWithFormatLabels, fontSize, getFont(applyZoom), g2.getFontRenderContext());
		this.fontrenderContext = g2.getFontRenderContext(); //TODO workaround to make sure getTextSize works without a graphics object

		if (align == AlignHorizontal.CENTER) x = (int) (x - formattedFont.getWidth() / 2);
		else if (align == AlignHorizontal.RIGHT) x = (int) (x - formattedFont.getWidth());

		g2.drawString(formattedFont.getAttributedCharacterIterator(), x, y);
	}

}
