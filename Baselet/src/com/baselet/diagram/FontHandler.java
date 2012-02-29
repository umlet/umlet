package com.baselet.diagram;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;


public class FontHandler {

	/**** TEXT FORMATTING LABELS ****/
	public static class FormatLabels {
		public static final String UNDERLINE = "_";
		public static final String BOLD = "*";
		public static final String ITALIC = "/";
	}

	private DiagramHandler handler;
	private Integer fontSize;
	private Integer diagramDefaultSize = null; // if "fontsize=..." is uncommented this variable is set

	private String diagramDefaultFontFamily = null;
	private FontRenderContext fontrenderContext = new FontRenderContext(null, true, true);

	public FontHandler(DiagramHandler handler) {
		this.handler = handler;
	}

	public void setFontSize(Integer fontsize) {
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

	public void setDiagramDefaultFontSize(Integer diagramDefaultSize) {
		if (Constants.fontSizeList.contains(diagramDefaultSize)) this.diagramDefaultSize = diagramDefaultSize;
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
		Integer returnFontSize;
		if (fontSize == null) {
			if (diagramDefaultSize != null) returnFontSize = diagramDefaultSize;
			else returnFontSize = Constants.defaultFontsize;
		}
		else returnFontSize = fontSize;

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

	public Dimension getTextSize(String s) {
		return getTextSize(s, true);
	}

	public Dimension getTextSize(String s, boolean applyZoom) {
		if (s == null) return null;
		if (s.length() == 0) return new Dimension(0, 0);
		TextLayout tl = new TextLayout(s, getFont(applyZoom), fontrenderContext);
		return new Dimension((int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
	}

	public int getTextWidth(String s) {
		return getTextWidth(s, true);
	}

	public int getTextWidth(String s, boolean applyZoom) {
		if (s == null) return 0;
		return (int) this.getTextSize(s, applyZoom).getWidth();
	}

	public void writeText(Graphics2D g2, String s, int x, int y, AlignHorizontal align) {
		writeText(g2, s, x, y, align, true);
	}

	public void writeText(Graphics2D g2, String s, int x, int y, AlignHorizontal align, boolean applyZoom) {
		for (String line : s.split("\n")) {
			this.write(g2, line, x, y, align, applyZoom);
			y += g2.getFontMetrics().getHeight();
		}
	}

	private void write(Graphics2D g2, String stringWithFormatLabels, int x, int y, AlignHorizontal align, boolean applyZoom) {
		if (stringWithFormatLabels == null || stringWithFormatLabels.isEmpty()) return;
		float fontSize = getFontSize(applyZoom);
		FormattedFont formattedFont = new FormattedFont(stringWithFormatLabels, fontSize, getFont(applyZoom), g2.getFontRenderContext());
		this.fontrenderContext = g2.getFontRenderContext(); //TODO workaround to make sure getTextSize works without a graphics object
		
		if (align == AlignHorizontal.CENTER) x = (int) (x - formattedFont.getWidth() / 2);
		else if (align == AlignHorizontal.RIGHT) x = (int) (x - formattedFont.getWidth());

		g2.drawString(formattedFont.getAttributedCharacterIterator(), x, y);
	}

}
