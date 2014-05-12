package com.baselet.control.enumerations;

public enum LineType implements RegexValueHolder {
	SOLID("-"), DASHED("."), DOTTED(".."), DOUBLE("="), DOUBLE_DASHED(":"), DOUBLE_DOTTED("::");

	private String value;

	private LineType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getRegexValue() {
		return value.replaceAll("\\.", "\\\\.");
	}

}