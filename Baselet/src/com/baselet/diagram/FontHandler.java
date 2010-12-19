package com.baselet.diagram;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

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

	public FontHandler(DiagramHandler handler) {
		this.handler = handler;
	}

	public void setFontSize(Integer fontsize) {
		this.fontSize = fontsize;
	}

	public void setDiagramDefaultFontSize(Integer diagramDefaultSize) {
		this.diagramDefaultSize = cutAtMinMax(diagramDefaultSize);
	}

	private Integer cutAtMinMax(Integer diagramDefaultSize) {
		if (diagramDefaultSize == null) return null;
		else return Math.max(Math.min(diagramDefaultSize, Constants.FONT_MAX), Constants.FONT_MIN);
	}

	public void resetFontSize() {
		fontSize = null;
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
		return new Font(Constants.FONT, Font.PLAIN, (int) getFontSize(applyZoom));
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

	public Font makeFontPlain(Font f) {
		return new Font(f.getName(), Font.PLAIN, f.getSize());
	}

	public Font makeFontBold(Font f) {
		return new Font(f.getName(), Font.BOLD, f.getSize());
	}

	public Font makeFontItalic(Font f) {
		return new Font(f.getName(), Font.ITALIC, f.getSize());
	}

	public Font makeFontBoldItalic(Font f) {
		return new Font(f.getName(), Font.BOLD | Font.ITALIC, f.getSize());
	}

	public void write(Graphics2D g2, String s, int x, int y, AlignHorizontal align, AlignVertical valign) {
		if (s == null) return;

		boolean underline = false;
		boolean bold = false;
		boolean italic = false;

		String checkedString = checkStringForValidFormatsAndRemoveTheirLabels(s);
		if (checkedString.charAt(0) == '1') underline = true;
		if (checkedString.charAt(1) == '1') bold = true;
		if (checkedString.charAt(2) == '1') italic = true;
		s = checkedString.substring(3);

		if (bold && italic) g2.setFont(makeFontBoldItalic(g2.getFont()));
		else if (bold) g2.setFont(makeFontBold(g2.getFont()));
		else if (italic) g2.setFont(makeFontItalic(g2.getFont()));
		else g2.setFont(makeFontPlain(g2.getFont()));

		s = s.replaceAll("<<", "\u00AB");
		s = s.replaceAll(">>", "\u00BB");

		if (align == AlignHorizontal.CENTER) x = x - getTextWidth(s) / 2;
		else if (align == AlignHorizontal.RIGHT) x = x - getTextWidth(s);

		if (valign == AlignVertical.CENTER) y = y + getTextHeight(s) / 2;
		else if (valign == AlignVertical.TOP) y = y + getTextHeight(s);

		g2.drawString(s, x, y);
		if (underline) {
			TextLayout l = new TextLayout(s, g2.getFont(), Constants.FRC);
			Rectangle2D r2d = l.getBounds();
			if (bold) {
				g2.drawLine(x, y + (int) getDistanceBetweenTexts() / 2, x + (int) r2d.getWidth(), y + (int) getDistanceBetweenTexts() / 2);
			}
			else {
				g2.drawLine(x, y + (int) getDistanceBetweenTexts() / 2, x + (int) r2d.getWidth(), y + (int) getDistanceBetweenTexts() / 2);
			}
		}
		if (italic || bold) makeFontPlain(g2.getFont());
	}

	/**
	 * Checks the String for formats and returns the String without the valid format labels and with a starting
	 * sequence of numbers which describe the valid format labels
	 * The first number shows if the text is underlined, the second bold, the third italic
	 */
	public static String checkStringForValidFormatsAndRemoveTheirLabels(String s) {
		boolean underline = false;
		boolean bold = false;
		boolean italic = false;

		/*
		 * NOT USED NOW
		 * int spaceBefore = 0;
		 * int spaceAfter = 0;
		 * //Before scanning for text format we remove spaces
		 * while (s.startsWith(" ")) {
		 * spaceBefore++;
		 * s = s.substring(1);
		 * }
		 * while (s.endsWith(" ")) {
		 * spaceAfter++;
		 * s = s.substring(0, s.length()-1);
		 * }
		 */

		// As long as any text format applies the loop continues (any format type is only allowed once)
		while (true) {
			if (!underline && s.startsWith(FontHandler.FormatLabels.UNDERLINE) && s.endsWith(FontHandler.FormatLabels.UNDERLINE) && (s.length() > 2)) {
				underline = true;
				s = s.substring(1, s.length() - 1);
				continue;
			}
			if (!bold && s.startsWith(FontHandler.FormatLabels.BOLD) && s.endsWith(FontHandler.FormatLabels.BOLD) && (s.length() > 2)) {
				bold = true;
				s = s.substring(1, s.length() - 1);
				continue;
			}
			if (!italic && s.startsWith(FontHandler.FormatLabels.ITALIC) && s.endsWith(FontHandler.FormatLabels.ITALIC) && (s.length() > 2)) {
				italic = true;
				s = s.substring(1, s.length() - 1);
				continue;
			}
			break;
		}

		/*
		 * NOT USED NOW
		 * //After scanning we add the spaces again to the string
		 * for (int i = 0; i < spaceBefore; spaceBefore--) {
		 * spaceBefore--;
		 * s = " " + s;
		 * }
		 * for (int i = 0; i < spaceAfter; spaceAfter--) {
		 * spaceAfter--;
		 * s = s + " ";
		 * }
		 */

		// The returning String starts with a number sequence to show which labels applies.
		// Warning: The first added digit is the last of the sequence after adding all digits, so we must add the last at first and the first at last
		if (italic) s = "1" + s;
		else s = "0" + s;
		if (bold) s = "1" + s;
		else s = "0" + s;
		if (underline) s = "1" + s;
		else s = "0" + s;

		return s;
	}

	public void writeText(Graphics2D g2, String s, int x, int y, boolean center) {
		if (center) this.writeText(g2, s, x, y, AlignHorizontal.CENTER, AlignVertical.BOTTOM);
		else this.writeText(g2, s, x, y, AlignHorizontal.LEFT, AlignVertical.BOTTOM);
	}

	public void writeText(Graphics2D g2, String s, int x, int y, AlignHorizontal align, AlignVertical valign) {
		this.write(g2, s, x, y, align, valign);
	}

}
