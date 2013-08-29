package com.baselet.control.enumerations;

public enum FormatLabels {
	UNDERLINE("_"),
	BOLD(LineType.BOLD.getValue()), // uses same character as bold lines
	ITALIC("/");

	private String value;

	private FormatLabels(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
