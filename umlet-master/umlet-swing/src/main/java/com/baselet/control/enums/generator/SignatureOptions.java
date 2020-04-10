package com.baselet.control.enums.generator;

public enum SignatureOptions {
	ALL("all"), PARAMS_ONLY("parameters only"), RETURN_ONLY("return type only");
	private final String label;

	private SignatureOptions(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static SignatureOptions getEnum(String text) {
		for (SignatureOptions value : values()) {
			if (value.toString().equals(text)) {
				return value;
			}
		}
		return null;
	}
}
