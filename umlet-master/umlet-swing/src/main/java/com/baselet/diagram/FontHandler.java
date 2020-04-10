package com.baselet.diagram;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.config.Config;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.AlignHorizontal;

public class FontHandler {

	private final DiagramHandler handler;
	private Double fontSize;
	private Double diagramDefaultSize = null; // if "fontsize=..." is uncommented this variable is set

	private String diagramDefaultFontFamily = null;
	private FontRenderContext fontrenderContext = new FontRenderContext(null, true, true);

	public FontHandler(DiagramHandler handler) {
		this.handler = handler;
	}

	public void setFontSize(Double fontsize) {
		fontSize = fontsize;
	}

	public void setDiagramDefaultFontFamily(String fontfamily) {
		if (Constants.fontFamilyList.contains(fontfamily)) {
			diagramDefaultFontFamily = fontfamily;
		}
	}

	public void resetDiagramDefaultFontFamily() {
		diagramDefaultFontFamily = null;
	}

	private String getDiagramDefaultFontFamily() {
		String returnFontFamily;
		if (diagramDefaultFontFamily != null) {
			returnFontFamily = diagramDefaultFontFamily;
		}
		else {
			returnFontFamily = Config.getInstance().getDefaultFontFamily();
		}

		return returnFontFamily;
	}

	public void setDiagramDefaultFontSize(Double diagramDefaultSize) {
		this.diagramDefaultSize = diagramDefaultSize;
	}

	public void resetFontSize() {
		fontSize = null;
	}

	public void resetDiagramDefaultFontSize() {
		diagramDefaultSize = null;
	}

	public double getFontSize() {
		return getFontSize(true);
	}

	public double getFontSize(boolean applyZoom) {
		Double returnFontSize;
		if (fontSize != null) {
			returnFontSize = fontSize;
		}
		else if (diagramDefaultSize != null) {
			returnFontSize = diagramDefaultSize;
		}
		else {
			returnFontSize = Double.valueOf(Config.getInstance().getDefaultFontsize());
		}

		if (applyZoom) {
			return returnFontSize * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		}
		else {
			return returnFontSize;
		}
	}

	public Font getFont() {
		return getFont(true);
	}

	public Font getFont(boolean applyZoom) {
		return new Font(getDiagramDefaultFontFamily(), Font.PLAIN, (int) getFontSize(applyZoom));
	}

	public double getDistanceBetweenTexts() {
		return getDistanceBetweenTexts(true);
	}

	public double getDistanceBetweenTexts(boolean applyZoom) {
		return getFontSize(applyZoom) / 4;
	}

	public DimensionDouble getTextSize(String stringWithFormatLabels) {
		return getTextSize(stringWithFormatLabels, true);
	}

	public DimensionDouble getTextSize(String stringWithFormatLabels, boolean applyZoom) {
		if (stringWithFormatLabels.isEmpty()) {
			return new DimensionDouble(0, 0);
		}
		return new FormattedFont(stringWithFormatLabels, getFontSize(applyZoom), getFont(applyZoom), fontrenderContext).getDimensions();
	}

	public DimensionDouble getTextSize(StringStyle singleLine, boolean applyZoom) {
		if (singleLine.getStringWithoutMarkup().isEmpty()) {
			return new DimensionDouble(0, 0);
		}
		return new FormattedFont(singleLine, getFontSize(applyZoom), getFont(applyZoom), fontrenderContext).getDimensions();
	}

	public double getTextWidth(String s) {
		return getTextWidth(s, true);
	}

	public double getTextWidth(String s, boolean applyZoom) {
		if (s == null) {
			return 0;
		}
		return this.getTextSize(s, applyZoom).getWidth();
	}

	public void writeText(Graphics2D g2, String s, double x, double y, AlignHorizontal align) {
		writeText(g2, s, x, y, align, true);
	}

	public void writeText(Graphics2D g2, String s, double x, double y, AlignHorizontal align, boolean applyZoom) {
		for (String line : s.split("\n", -1)) {
			write(g2, StringStyle.analyzeFormatLabels(line), x, y, align, applyZoom);
			y += g2.getFontMetrics().getHeight();
		}
	}

	public void writeText(Graphics2D g2, StringStyle[] lines, double x, double y, AlignHorizontal align) {
		writeText(g2, lines, x, y, align, true);
	}

	public void writeText(Graphics2D g2, StringStyle[] lines, double x, double y, AlignHorizontal align, boolean applyZoom) {
		for (StringStyle line : lines) {
			write(g2, line, x, y, align, applyZoom);
			y += g2.getFontMetrics().getHeight();
		}
	}

	private void write(Graphics2D g2, StringStyle singleLine, double x, double y, AlignHorizontal align, boolean applyZoom) {
		if (singleLine == null || singleLine.getStringWithoutMarkup().isEmpty()) {
			return;
		}
		double fontSize = getFontSize(applyZoom);
		FormattedFont formattedFont = new FormattedFont(singleLine, fontSize, getFont(applyZoom), g2.getFontRenderContext());
		fontrenderContext = g2.getFontRenderContext(); // TODO workaround to make sure getTextSize works without a graphics object

		if (align == AlignHorizontal.CENTER) {
			x = (int) (x - formattedFont.getWidth() / 2);
		}
		else if (align == AlignHorizontal.RIGHT) {
			x = (int) (x - formattedFont.getWidth());
		}

		g2.drawString(formattedFont.getAttributedCharacterIterator(), (float) x, (float) y);
	}

	public static DimensionDouble getTextSizeStatic(FormattedFont formattedFont) {
		return new DimensionDouble(formattedFont.getWidth(), formattedFont.getHeight());
	}

}
