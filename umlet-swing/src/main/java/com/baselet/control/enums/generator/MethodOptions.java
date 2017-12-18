package com.baselet.control.enums.generator;

public enum MethodOptions {
	ALL("all"), NONE("none"), PUBLIC("public only");
	private final String label;

	private MethodOptions(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static MethodOptions getEnum(String text) {
		for (MethodOptions value : values()) {
			if (value.toString().equals(text)) {
				return value;
			}
		}
		return null;
	}
}
