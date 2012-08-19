package com.umlet.language;

public enum SortOptions {
	HEIGHT("by size"), ALPHABET("alphabetically");
	private final String label;
	private SortOptions(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return label;
	}
	public static SortOptions getEnum(String text) {
		for (SortOptions value: values()) {
			if (value.toString().equals(text)) {
				return value;
			}
		}
		return null;
	}
}
