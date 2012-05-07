package com.baselet.diagram;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;


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
		TextLayout tl = new TextLayout(s, getFont(applyZoom), Constants.FRC);
		return new Dimension((int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
	}

	public int getTextWidth(String s) {
		return getTextWidth(s, true);
	}

	public int getTextWidth(String s, boolean applyZoom) {
		if (s == null) return 0;
		return (int) this.getTextSize(s, applyZoom).getWidth();
	}

	public int getTextHeight(String s) {
		return getTextHeight(s, true);
	}

	public int getTextHeight(String s, boolean applyZoom) {
		if (s == null) return 0;
		else return (int) this.getTextSize(s, applyZoom).getHeight();
	}

	public void writeText(Graphics2D g2, String s, int x, int y, boolean center) {
		if (center) this.writeText(g2, s, x, y, AlignHorizontal.CENTER, AlignVertical.BOTTOM);
		else this.writeText(g2, s, x, y, AlignHorizontal.LEFT, AlignVertical.BOTTOM);
	}

	public void writeText(Graphics2D g2, String s, int x, int y, AlignHorizontal align, AlignVertical valign) {
		writeText(g2, s, x, y, align, valign, true);
	}

	public void writeText(Graphics2D g2, String s, int x, int y, AlignHorizontal align, AlignVertical valign, boolean applyZoom) {
		for (String line : s.split("\n")) {
			this.write(g2, line, x, y, align, valign, applyZoom);
			y += g2.getFontMetrics().getHeight();
		}
	}

	private static boolean underline;
	private static boolean bold;
	private static boolean italic;
	private void write(Graphics2D g2, String stringWithFormatLabels, int x, int y, AlignHorizontal align, AlignVertical valign, boolean applyZoom) {
		String s = setFormatAndRemoveLabels(stringWithFormatLabels);
		if (s == null || s.isEmpty()) return;

		s = s.replaceAll("<<", "\u00AB");
		s = s.replaceAll(">>", "\u00BB");

		AttributedString as = new AttributedString(s);

		as.addAttribute(TextAttribute.SIZE, getFontSize(applyZoom));
		as.addAttribute(TextAttribute.FAMILY, getDiagramDefaultFontFamily());
		if (bold) as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		if (italic) as.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		if (underline) as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, s.length());

		if (align == AlignHorizontal.CENTER) x = x - getTextWidth(s, applyZoom) / 2;
		else if (align == AlignHorizontal.RIGHT) x = x - getTextWidth(s, applyZoom);

		if (valign == AlignVertical.CENTER) y = y + getTextHeight(s, applyZoom) / 2;
		else if (valign == AlignVertical.TOP) y = y + getTextHeight(s, applyZoom);

		g2.drawString(as.getIterator(), x, y);
	}

	private static String setFormatAndRemoveLabels(String s) {
		underline = false;
		bold = false;
		italic = false;

		if (s == null || s.isEmpty()) return s;

		// As long as any text format applies the loop continues (any format type is only allowed once)
		while (true) {
			if (!underline && s.startsWith(FormatLabels.UNDERLINE) && s.endsWith(FormatLabels.UNDERLINE) && (s.length() > 2)) {
				underline = true;
				s = s.substring(1, s.length() - 1);
			}
			else if (!bold && s.startsWith(FormatLabels.BOLD) && s.endsWith(FormatLabels.BOLD) && (s.length() > 2)) {
				bold = true;
				s = s.substring(1, s.length() - 1);
			}
			else if (!italic && s.startsWith(FormatLabels.ITALIC) && s.endsWith(FormatLabels.ITALIC) && (s.length() > 2)) {
				italic = true;
				s = s.substring(1, s.length() - 1);
			}
			else break;
		}

		return s;
	}

}
