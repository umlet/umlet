package com.baselet.element.elementnew.plot.parser;

public class KeyValue {

	private String key;
	private String value;
	private int line;
	private boolean used;

	public KeyValue(String key, String value, int line) {
		super();
		this.key = key;
		this.value = value;
		this.line = line;
		used = false;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	@Override
	public String toString() {
		return key + "\t-> " + value + " (line " + line + ")";
	}

}
