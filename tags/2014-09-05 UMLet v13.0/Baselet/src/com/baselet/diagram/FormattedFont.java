package com.baselet.diagram;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import com.baselet.control.StringStyle;
import com.baselet.control.enumerations.FormatLabels;

public class FormattedFont {

	private static Integer underline;
	private static Float bold;
	private static Float italic;

	private String string;
	private final AttributedString atrString;

	private final FontRenderContext fontRenderContext;

	private final TextLayout textLayout;

	public FormattedFont(String text, double fontSize, Font font, FontRenderContext fontRenderContext) {
		string = setFormatAndRemoveLabels(text);

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

	public void replaceFirstAndLastSpaceWithDot() {
		if (string.startsWith(" ")) {
			string = "." + string.substring(1);
		}
		if (string.endsWith(" ")) {
			string = string.substring(string.length() - 1) + ".";
		}
	}

	public FontRenderContext getFontRenderContext() {
		return fontRenderContext;
	}

	public AttributedCharacterIterator getAttributedCharacterIterator() {
		return atrString.getIterator();
	}

	private static String setFormatAndRemoveLabels(String s) {
		StringStyle style = StringStyle.analyzeFormatLabels(s);

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

		return style.getStringWithoutMarkup();
	}

	public double getWidth() {
		return textLayout.getVisibleAdvance();
	}

	public double getHeight() {
		return textLayout.getBounds().getHeight();
	}
}
