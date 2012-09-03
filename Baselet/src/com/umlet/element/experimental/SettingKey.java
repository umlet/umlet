package com.umlet.element.experimental;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;

public enum SettingKey {
	ForegroundColor("fg", "red"),
	BackgroundColor("bg", "#0A37D3"),
	LineType("lt", ".", "..", "*"),
	ElementStyle("elementstyle", com.baselet.control.Constants.ElementStyle.values()),
	FontSize("fontsize", "12.5"),
	VerticalAlign("valign", AlignVertical.values()),
	HorizontalAlign("halign", AlignHorizontal.values());

	private String key;
	private Object[] autocompletionValues;

	SettingKey(String key, String ... autocompletionValues) {
		this.key = key;
		this.autocompletionValues = autocompletionValues;
	}

	SettingKey(String key, Object[] autocompletionValues) {
		this.key = key;
		this.autocompletionValues = autocompletionValues;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}

	public Object[] autocompletionValues() {
		return autocompletionValues;
	}
}