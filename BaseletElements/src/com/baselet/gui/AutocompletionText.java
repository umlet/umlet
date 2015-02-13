package com.baselet.gui;

public class AutocompletionText {
	private String text;
	private final String info;
	private String base64Img;

	public AutocompletionText(String text, String info) {
		super();
		this.text = text;
		this.info = info;
	}

	public AutocompletionText(String text, String info, String base64Img) {
		super();
		this.text = text;
		this.info = info;
		this.base64Img = base64Img;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getInfo() {
		return info;
	}

	public String getHtmlInfo() {
		String baseText = getText() + " <span style='font-style:italic;color:gray'>" + getInfo() + "</span>";
		if (base64Img != null) {
			baseText += " <img src='data:image/gif;base64," + base64Img + "'>";
		}
		return baseText;
	}

}
