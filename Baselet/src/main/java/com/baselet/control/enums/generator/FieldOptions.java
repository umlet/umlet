package com.baselet.control.enums.generator;

public enum FieldOptions {
	ALL("all"), NONE("none"), PUBLIC("public only");
	private final String label;

	private FieldOptions(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static FieldOptions getEnum(String text) {
		for (FieldOptions value : values()) {
			if (value.toString().equals(text)) {
				return value;
			}
		}
		return null;
	}
}