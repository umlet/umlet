package com.baselet.gwt.client.copy.gui;

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

	public AutocompletionText(String text, String info) {
		this(text, info, false);
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
