package com.baselet.control;

import java.util.HashSet;
import java.util.Set;

import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.FormatLabels;

public class StringStyle {

	public static final String ESCAPE_CHARACTER = "\\";
	private static final String TEMP_REPLACEMENT = "§$§ä%§%ü/,";

	private final Set<FormatLabels> format;
	private final String stringWithoutMarkup;

	public StringStyle(Set<FormatLabels> format, String stringWithoutMarkup) {
		super();
		this.format = format;
		this.stringWithoutMarkup = stringWithoutMarkup;
	}

	public static String replaceNotEscaped(String line) {
		line = StringStyle.replaceNotEscaped(line, SharedConstants.LEFT_QUOTATION, "\u00AB");
		line = StringStyle.replaceNotEscaped(line, SharedConstants.RIGHT_QUOTATION, "\u00BB");
		return line;
	}

	/**
	 * this method does not use "complex" regular expressions to avoid problems with compiling to GWT
	 */
	public static String replaceNotEscaped(String s, String old, String replacement) {
		s = s.replace(ESCAPE_CHARACTER + old, TEMP_REPLACEMENT);
		s = s.replace(old, replacement);
		s = s.replace(TEMP_REPLACEMENT, old);
		return s;
	}

	public static StringStyle analyzeFormatLabels(String s) {
		Set<FormatLabels> format = new HashSet<FormatLabels>();
		if (s != null && !s.isEmpty()) {
			// As long as any text format applies the loop continues
			boolean matchFound = true;
			while (matchFound) {
				matchFound = false;
				for (FormatLabels formatLabel : FormatLabels.values()) {
					String markup = formatLabel.getValue();
					if (s.startsWith(markup) && s.endsWith(markup) && s.length() > markup.length() * 2) {
						format.add(formatLabel);
						s = s.substring(markup.length(), s.length() - markup.length());
						matchFound = true;
					}
				}
			}
		}
		return new StringStyle(format, s);
	}

	public Set<FormatLabels> getFormat() {
		return format;
	}

	public String getStringWithoutMarkup() {
		return stringWithoutMarkup;
	}

	@Override
	public String toString() {
		return "StringStyle [format=" + format + ", stringWithoutMarkup=" + stringWithoutMarkup + "]";
	}

}
