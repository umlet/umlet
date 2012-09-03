package com.baselet.gui;

public class AutocompletionText {
	private String text;
	private String info;
	
	public AutocompletionText(String text, String info) {
		super();
		this.text = text;
		this.info = info;
	}

	public AutocompletionText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String getInfo() {
		return info;
	}
	
}
