package com.baselet.gui;

public class AutocompletionText {
	private String text;
	private String info;
	private boolean global;

	public AutocompletionText(String text, String info) {
		super();
		this.text = text;
		this.info = info;
	}

	public String getText() {
		return text;
	}

	public String getInfo() {
		return info;
	}
	
	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isGlobal() {
		return global;
	}
	
}
