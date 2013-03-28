package com.baselet.client.copy.control.enumerations;

public enum LineType {
	SOLID("-"), DASHED("."), DOTTED(".."), DOUBLE("="), DOUBLE_DASHED(":"), DOUBLE_DOTTED("::"),
	BOLD("*") /*not really a linetype but a combination of linetype and thickness; TODO perhaps refactor*/;

	private String value;

	private LineType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}