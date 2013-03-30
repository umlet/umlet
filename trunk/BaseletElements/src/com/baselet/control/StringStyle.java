package com.baselet.control;

import java.util.HashSet;
import java.util.Set;

import com.baselet.control.enumerations.FormatLabels;

public class StringStyle {
	private final Set<FormatLabels> format;
	private final String stringWithoutMarkup;

	public StringStyle(Set<FormatLabels> format, String stringWithoutMarkup) {
		super();
		this.format = format;
		this.stringWithoutMarkup = stringWithoutMarkup;
	}

	public static StringStyle analyseStyle(String s) {
		Set<FormatLabels> format = new HashSet<FormatLabels>();

		if (s != null && !s.isEmpty()) {
			// As long as any text format applies the loop continues
			boolean matchFound = true;
			while (matchFound) {
				matchFound = false;
				for (FormatLabels formatLabel : FormatLabels.values()) {
					String markup = formatLabel.getValue();
					if (s.startsWith(markup) && s.endsWith(markup) && s.length() > markup.length()*2) {
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
}
