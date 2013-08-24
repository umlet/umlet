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
	private AttributedString atrString;
	
	private TextLayout textLayout;
	
	public FormattedFont(String text, double fontSize, Font font, FontRenderContext fontRenderContext) {
		string = setFormatAndRemoveLabels(text);

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
		StringStyle style = StringStyle.analyseStyle(s);
		
		if (style.getFormat().contains(FormatLabels.UNDERLINE)) {
			underline = TextAttribute.UNDERLINE_ON;
		} else {
			underline = -1; // UNDERLINE_OFF
		}
		
		if (style.getFormat().contains(FormatLabels.BOLD)) {
			bold = TextAttribute.WEIGHT_BOLD;
		} else {
			bold = TextAttribute.WEIGHT_REGULAR;
		}
		
		if (style.getFormat().contains(FormatLabels.ITALIC)) {
			italic = TextAttribute.POSTURE_OBLIQUE;
		} else {
			italic = TextAttribute.POSTURE_REGULAR;
		}
		
		return style.getStringWithoutMarkup();
	}

	public double getWidth() {
		return textLayout.getBounds().getWidth();
	}

	public double getHeight() {
		return textLayout.getBounds().getHeight();
	}
}
