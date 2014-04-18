package com.baselet.control.enumerations;

public enum LineType implements ValueHolder {
	SOLID("-"), DASHED("."), DOTTED(".."), DOUBLE("="), DOUBLE_DASHED(":"), DOUBLE_DOTTED("::");

	private String value;

	private LineType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}