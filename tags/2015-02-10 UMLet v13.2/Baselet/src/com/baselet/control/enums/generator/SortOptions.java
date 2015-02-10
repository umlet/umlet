package com.baselet.control.enums.generator;

public enum SortOptions {
	HEIGHT("by size"), PACKAGE("by package"), ALPHABET("alphabetically"), RELATIONS("with relations");
	private final String label;

	private SortOptions(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static SortOptions getEnum(String text) {
		for (SortOptions value : values()) {
			if (value.toString().equals(text)) {
				return value;
			}
		}
		return null;
	}
}
