package com.umlet.element.experimental.settings.facets;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class DefaultGlobalFacet implements Facet {

	public enum GlobalSetting {
		ForegroundColor("fg", "set foreground color via string or code", "red"),
		BackgroundColor("bg", "set background color via string or code", "#0A37D3"),
		LineType("lt", "set line type", ".", "..", "*"),
		FontSize("fontsize", "set font size", "12.5"),
		ElementStyle("elementstyle", com.baselet.control.Constants.ElementStyle.values()),
		VerticalAlign("valign", AlignVertical.values()),
		HorizontalAlign("halign", AlignHorizontal.values());
		;

		public static final String SEPARATOR = "=";
		
		private String key;
		private String info;
		private Object[] autocompletionValues;

		GlobalSetting(String key, String info, String ... autocompletionValues) {
			this.key = key;
			this.info = info;
			this.autocompletionValues = autocompletionValues;
		}

		GlobalSetting(String key, Object[] autocompletionValues) {
			this.key = key;
			this.autocompletionValues = autocompletionValues;
		}

		@Override
		public String toString() {
			return key;
		}

		public static AutocompletionText[] getAutocompletion() {
			List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
			for (GlobalSetting s : GlobalSetting.values()) {
				for (Object o : s.autocompletionValues) {
					returnList.add(new AutocompletionText(s.key + SEPARATOR + o.toString().toLowerCase(), s.info));
				}
			}
			return returnList.toArray(new AutocompletionText[returnList.size()]);
		}
	}

	public boolean checkStart(String line) {
		boolean startsWithEnumKey = false;
		for (GlobalSetting s : GlobalSetting.values()) {
			if (line.startsWith(s + GlobalSetting.SEPARATOR)) startsWithEnumKey = true;
		}
		return startsWithEnumKey;
	}

	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		String[] split = line.split(GlobalSetting.SEPARATOR, 2);
		if (split.length > 1) {
			try {
				String key = split[0];
				String value = split[1].toUpperCase();
				if (key.equals(GlobalSetting.ForegroundColor.toString())) {
					drawer.setForegroundColor(value);
				} else if (key.equals(GlobalSetting.BackgroundColor.toString())) {
					drawer.setBackground(value, Constants.ALPHA_MIDDLE_TRANSPARENCY);
				} else if (key.equals(GlobalSetting.LineType.toString())) {
					drawer.setLineType(value);
				} else if (key.equals(GlobalSetting.FontSize.toString())) {
					drawer.setFontSize(value);
				} else if (key.equals(GlobalSetting.HorizontalAlign.toString())) {
					propConfig.sethAlignGlobally(AlignHorizontal.valueOf(value));
				} else if (key.equals(GlobalSetting.VerticalAlign.toString())) {
					propConfig.setvAlignGlobally(AlignVertical.valueOf(value));
				} else if (key.equals(GlobalSetting.ElementStyle.toString())) {
					propConfig.setElementStyle(com.baselet.control.Constants.ElementStyle.valueOf(value));
				}
			} catch (Exception e) {/*any exception results in no action*/}
		}
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return GlobalSetting.getAutocompletion();
	}

}
