package com.baselet.control.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baselet.diagram.draw.helper.StyleException;

public enum LineType implements RegexValueHolder {
	SOLID("-"), DASHED("."), DOTTED(".."), DOUBLE("="), DOUBLE_DASHED(":"), DOUBLE_DOTTED("::");

	private String value;

	public static final List<LineType> LT_LIST = Collections.unmodifiableList(Arrays.asList(LineType.SOLID, LineType.DASHED, LineType.DOTTED));

	private LineType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static LineType fromString(String text) {
		for (LineType lineType : LineType.values()) {
			if (lineType.getValue().equalsIgnoreCase(text)) {
				return lineType;
			}
		}
		throw new StyleException("Allowed values for LineType: -, ., .., =, :, ::");
	}

	public String getReadableText() {
		return name().toLowerCase();
	}

	@Override
	public String getRegexValue() {
		return value.replaceAll("\\.", "\\\\.");
	}

}