package com.umlet.element.experimental.settings.text;

import java.util.HashMap;
import java.util.Vector;

import com.baselet.control.Utils;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.SettingKey;

public class GlobalSettings {

	public static final String SEPARATOR = "=";

	private HashMap<String, String> settings = new HashMap<String, String>();

	public GlobalSettings(Vector<String> propertiesText) {
		for (String line : propertiesText) {
			if (line.contains(SEPARATOR)) {
				String[] split = line.split(SEPARATOR, 2);
				settings.put(split[0], split[1]);
			}
		}
	}

	public String updateSetting(SettingKey key, String newValue, String panelText) {
		String newState = "";
		for (String line : Utils.decomposeStringsWithComments(panelText)) {
			if (!line.startsWith(key.getKey())) newState += line + "\n";
		}
		newState = newState.substring(0, newState.length()-1); //remove last linebreak
		if (newValue != null) newState += "\n" + key.getKey() + SEPARATOR + newValue; // null will not be added as a value
		return newState;
	}

	public String getSetting(SettingKey key) {
		return settings.get(key.toString());
	}

	public Float getSettingFloat(SettingKey key) {
		Float returnValue = null;
		String value = settings.get(key.toString());
		if (value != null) {
			try {
				returnValue = Float.valueOf(value);
			} catch (NumberFormatException e) {/*do nothing; returnValue stays null*/}
		}
		return returnValue;
	}

	private static String filterRegex;
	static {
		filterRegex = "(";
		for (SettingKey key : SettingKey.values()) {
			filterRegex = filterRegex + "(" + key + SEPARATOR + ")|";
		}
		filterRegex += "(//)).*";
	}
	
	public static String getFilterRegex() {
		return filterRegex;
	}
	
}
