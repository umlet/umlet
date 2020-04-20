package com.baselet.control.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	public String getReadableText() {
		return name().toLowerCase();
	}

	@Override
	public String getRegexValue() {
		return value.replaceAll("\\.", "\\\\.");
	}

}