package com.baselet.diagram;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import com.baselet.diagram.FontHandler.FormatLabels;

public class FormattedFont {

	private static Integer underline;
	private static Float bold;
	private static Float italic;
	
	private String string;
	private AttributedString atrString;
	
	private TextLayout textLayout;
	
	public FormattedFont(String text, float fontSize, Font font, FontRenderContext fontRenderContext) {
		string = setFormatAndRemoveLabels(text);

		string = string.replaceAll("<<", "\u00AB");
		string = string.replaceAll(">>", "\u00BB");

		atrString = new AttributedString(string);

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
	
	public AttributedCharacterIterator getAttributedCharacterIterator() {
		return atrString.getIterator();
	}

	private static String setFormatAndRemoveLabels(String s) {
		underline = -1; // UNDERLINE_OFF
		bold = TextAttribute.WEIGHT_REGULAR;
		italic = TextAttribute.POSTURE_REGULAR;

		if (s == null || s.isEmpty()) return s;

		// As long as any text format applies the loop continues (any format type is only allowed once)
		while (true) {
			if (s.startsWith(FormatLabels.UNDERLINE) && s.endsWith(FormatLabels.UNDERLINE) && (s.length() > 2)) {
				underline = TextAttribute.UNDERLINE_ON;
				s = s.substring(1, s.length() - 1);
			}
			else if (s.startsWith(FormatLabels.BOLD) && s.endsWith(FormatLabels.BOLD) && (s.length() > 2)) {
				bold = TextAttribute.WEIGHT_BOLD;
				s = s.substring(1, s.length() - 1);
			}
			else if (s.startsWith(FormatLabels.ITALIC) && s.endsWith(FormatLabels.ITALIC) && (s.length() > 2)) {
				italic = TextAttribute.POSTURE_OBLIQUE;
				s = s.substring(1, s.length() - 1);
			}
			else break;
		}

		return s;
	}

	public double getWidth() {
		return textLayout.getBounds().getWidth();
	}

	public double getHeight() {
		return textLayout.getBounds().getHeight();
	}
}
