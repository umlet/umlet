package com.baselet.diagram;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.enums.FormatLabels;

public class FormattedFont {

	private static Integer underline;
	private static Float bold;
	private static Float italic;

	private final String string;
	private final AttributedString atrString;

	private final FontRenderContext fontRenderContext;

	private final TextLayout textLayout;

	public FormattedFont(String stringWithFormatLabels, double fontSize, Font font, FontRenderContext fontRenderContext) {
		this(StringStyle.analyzeFormatLabels(stringWithFormatLabels), fontSize, font, fontRenderContext);
	}

	public FormattedFont(StringStyle text, double fontSize, Font font, FontRenderContext fontRenderContext) {
		setFormat(text);
		string = text.getStringWithoutMarkup();

		atrString = new AttributedString(string);
		this.fontRenderContext = fontRenderContext;

		atrString.addAttribute(TextAttribute.FAMILY, font.getFamily());
		atrString.addAttribute(TextAttribute.SIZE, fontSize);
		atrString.addAttribute(TextAttribute.WEIGHT, bold);
		atrString.addAttribute(TextAttribute.POSTURE, italic);
		atrString.addAttribute(TextAttribute.UNDERLINE, underline, 0, string.length());

		textLayout = new TextLayout(getAttributedCharacterIterator(), fontRenderContext);
	}

	public String getString() {
		return string;
	}

	public FontRenderContext getFontRenderContext() {
		return fontRenderContext;
	}

	public AttributedCharacterIterator getAttributedCharacterIterator() {
		return atrString.getIterator();
	}

	private static void setFormat(StringStyle style) {
		if (style.getFormat().contains(FormatLabels.UNDERLINE)) {
			underline = TextAttribute.UNDERLINE_ON;
		}
		else {
			underline = -1; // UNDERLINE_OFF
		}

		if (style.getFormat().contains(FormatLabels.BOLD)) {
			bold = TextAttribute.WEIGHT_BOLD;
		}
		else {
			bold = TextAttribute.WEIGHT_REGULAR;
		}

		if (style.getFormat().contains(FormatLabels.ITALIC)) {
			italic = TextAttribute.POSTURE_OBLIQUE;
		}
		else {
			italic = TextAttribute.POSTURE_REGULAR;
		}
	}

	public double getWidth() {
		return textLayout.getVisibleAdvance();
	}

	public double getHeight() {
		return textLayout.getBounds().getHeight();
	}

	public DimensionDouble getDimensions() {
		return new DimensionDouble(getWidth(), getHeight());
	}
}
