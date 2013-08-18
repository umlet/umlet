package com.umlet.element.experimental.facets;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class DefaultGlobalFacet extends GlobalStatelessFacet {

	public enum GlobalSetting {
		FOREGROUND_COLOR("fg", "red", "foreground color string (blue,...) or code (#0A37D3,...)"),
		BACKGROUND_COLOR("bg", "red", "background color string (green,...) or code (#3c7a00,...)"),
		LINE_TYPE("lt", new String[] {LineType.DASHED.getValue(), "dashed lines"}, new String[] {LineType.DOTTED.getValue(), "dotted lines"}, new String[] {LineType.BOLD.getValue(), "bold lines"}),
		LINE_THICKNESS("lth", "1.0", "thickness of lines (1.5, 2.0, ...)"),
		FONT_SIZE("fontsize", "12", "font size (12.5, 10.3,...)"),
		LAYER("layer", "0", "higher layers are shown on top of lowers (-5, 0(=default), 3,...)")
		;

		private String key;
		private List<AutocompletionText> autocompletionValues = new ArrayList<AutocompletionText>();

		private String value;

		GlobalSetting(String key, String value, String info) {
			this.key = key.toLowerCase();
			this.value = value;
			this.autocompletionValues.add(new AutocompletionText(key.toLowerCase() + SEP + value.toLowerCase(), info));
		}

		GlobalSetting(String key, String[] ... valueInfoPairs) {
			this.key = key.toLowerCase();
			for (String[] valueInfoPair : valueInfoPairs) {
				this.autocompletionValues.add(new AutocompletionText(key.toLowerCase() + SEP + valueInfoPair[0].toLowerCase(), valueInfoPair[1]));
			}
		}

		@Override
		public String toString() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

	@Override
	public boolean checkStart(String line) {
		boolean startsWithEnumKey = false;
		for (GlobalSetting s : GlobalSetting.values()) {
			if (line.startsWith(s + SEP)) startsWithEnumKey = true;
		}
		return startsWithEnumKey;
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		String[] split = line.split(SEP, 2);
		if (split.length > 1) {
			try {
				String key = split[0];
				String value = split[1].toUpperCase();
				if (key.equalsIgnoreCase(GlobalSetting.FOREGROUND_COLOR.toString())) {
					drawer.setForegroundColor(value);
				} else if (key.equalsIgnoreCase(GlobalSetting.BACKGROUND_COLOR.toString())) {
					drawer.setBackgroundColor(value);
				} else if (key.equalsIgnoreCase(GlobalSetting.LINE_TYPE.toString())) {
					drawer.setLineType(value);
				}  else if (key.equalsIgnoreCase(GlobalSetting.LINE_THICKNESS.toString())) {
					drawer.setLineThickness(Float.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalSetting.FONT_SIZE.toString())) {
					drawer.setFontSize(value);
				} else if (key.equalsIgnoreCase(GlobalSetting.LAYER.toString())) {
					propConfig.setLayer(value);
				}
			} catch (Exception e) {/*any exception results in no action*/}
		}
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		for (GlobalSetting s : GlobalSetting.values()) {
			returnList.addAll(s.autocompletionValues);
		}
		return returnList.toArray(new AutocompletionText[returnList.size()]);
	}
	
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
