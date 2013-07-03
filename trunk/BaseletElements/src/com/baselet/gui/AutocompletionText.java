package com.baselet.gui;

public class AutocompletionText {
	private String text;
	private String info;
	private boolean global;

	public AutocompletionText(String text, String info, boolean global) {
		super();
		this.text = text;
		this.info = info;
		this.global = global;
	}

	public String getText() {
		return text;
	}

	public String getInfo() {
		return info;
	}

	public boolean isGlobal() {
		return global;
	}
	
}
