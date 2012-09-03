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

	public enum ElementStyleEnum {AUTORESIZE, NORESIZE, WORDWRAP};
	
	public enum GlobalSetting {
		ForegroundColor("fg", "red", "foreground color string (blue,...) or code (#0A37D3,...)"),
		BackgroundColor("bg", "red", "background color string (green,...) or code (#3c7a00,...)"),
		LineType("lt", new String[] {".", "dashed lines"}, new String[] {"..", "dotted lines"}, new String[] {"*", "bold lines"}),
		FontSize("fontsize", "14", "font size float (12.5, 10.3,...)"),
		ElementStyle("elementstyle",
				new String[] {ElementStyleEnum.AUTORESIZE.toString(), "resizes element as text grows"},
				new String[] {ElementStyleEnum.WORDWRAP.toString(), "wrap lines at the end of the line"},
				new String[] {ElementStyleEnum.NORESIZE.toString(), "disable manual resizing"}),
		VerticalAlign("valign",
				new String[] {AlignVertical.TOP.toString(), "vertical text alignment"},
				new String[] {AlignVertical.CENTER.toString(), "vertical text alignment"},
				new String[] {AlignVertical.BOTTOM.toString(), "vertical text alignment"}),
		HorizontalAlign("halign",
				new String[] {AlignHorizontal.LEFT.toString(), "horizontal text alignment"},
				new String[] {AlignHorizontal.CENTER.toString(), "horizontal text alignment"},
				new String[] {AlignHorizontal.RIGHT.toString(), "horizontal text alignment"});
		;

		public static final String SEPARATOR = "=";
		
		private String key;
		private List<AutocompletionText> autocompletionValues = new ArrayList<AutocompletionText>();

		GlobalSetting(String key, String value, String info) {
			this.key = key.toLowerCase();
			this.autocompletionValues.add(new AutocompletionText(key.toLowerCase() + SEPARATOR + value.toLowerCase(), info, true));
		}

		GlobalSetting(String key, String[] ... valueInfoPairs) {
			this.key = key.toLowerCase();
			for (String[] valueInfoPair : valueInfoPairs) {
				this.autocompletionValues.add(new AutocompletionText(key.toLowerCase() + SEPARATOR + valueInfoPair[0].toLowerCase(), valueInfoPair[1], true));
			}
		}

		@Override
		public String toString() {
			return key;
		}

		public static AutocompletionText[] getAutocompletion() {
			List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
			for (GlobalSetting s : GlobalSetting.values()) {
				returnList.addAll(s.autocompletionValues);
			}
			return returnList.toArray(new AutocompletionText[returnList.size()]);
		}
	}

	@Override
	public boolean checkStart(String line) {
		boolean startsWithEnumKey = false;
		for (GlobalSetting s : GlobalSetting.values()) {
			if (line.startsWith(s + GlobalSetting.SEPARATOR)) startsWithEnumKey = true;
		}
		return startsWithEnumKey;
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		String[] split = line.split(GlobalSetting.SEPARATOR, 2);
		if (split.length > 1) {
			try {
				String key = split[0];
				String value = split[1].toUpperCase();
				if (key.equalsIgnoreCase(GlobalSetting.ForegroundColor.toString())) {
					drawer.setForegroundColor(value);
				} else if (key.equalsIgnoreCase(GlobalSetting.BackgroundColor.toString())) {
					drawer.setBackground(value, Constants.ALPHA_MIDDLE_TRANSPARENCY);
				} else if (key.equalsIgnoreCase(GlobalSetting.LineType.toString())) {
					drawer.setLineType(value);
				} else if (key.equalsIgnoreCase(GlobalSetting.FontSize.toString())) {
					drawer.setFontSize(value);
				} else if (key.equalsIgnoreCase(GlobalSetting.HorizontalAlign.toString())) {
					propConfig.sethAlignGlobally(AlignHorizontal.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalSetting.VerticalAlign.toString())) {
					propConfig.setvAlignGlobally(AlignVertical.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalSetting.ElementStyle.toString())) {
					propConfig.setElementStyle(ElementStyleEnum.valueOf(value));
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
