package com.umlet.element.experimental.settings.facets;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class DefaultGlobalFacet implements Facet {

	public enum ElementStyleEnum {AUTORESIZE, RESIZE, NORESIZE, WORDWRAP}
	
	public enum GlobalSetting {
		FOREGROUND_COLOR("fg", "red", "foreground color string (blue,...) or code (#0A37D3,...)"),
		BACKGROUND_COLOR("bg", "red", "background color string (green,...) or code (#3c7a00,...)"),
		LINE_TYPE("lt", new String[] {LineType.DASHED.getValue(), "dashed lines"}, new String[] {LineType.DOTTED.getValue(), "dotted lines"}, new String[] {LineType.BOLD.getValue(), "bold lines"}),
		LINE_THICKNESS("lth", "1.0", "thickness of lines (1.5, 2.0, ...)"),
		FONT_SIZE("fontsize", "12", "font size (12.5, 10.3,...)"),
		LAYER("layer", "0", "elements on higher layers are shown on top of others, default is 0 (-5, 0, 3,...)"),
		ELEMENT_STYLE("elementstyle",
				new String[] {ElementStyleEnum.AUTORESIZE.toString(), "resizes element as text grows"},
				new String[] {ElementStyleEnum.WORDWRAP.toString(), "wrap lines at the end of the line"},
				new String[] {ElementStyleEnum.NORESIZE.toString(), "disable manual resizing"}),
		VERTICAL_ALIGN("valign",
				new String[] {AlignVertical.TOP.toString(), "vertical text alignment"},
				new String[] {AlignVertical.CENTER.toString(), "vertical text alignment"},
				new String[] {AlignVertical.BOTTOM.toString(), "vertical text alignment"}),
		HORIZONTAL_ALIGN("halign",
				new String[] {AlignHorizontal.LEFT.toString(), "horizontal text alignment"},
				new String[] {AlignHorizontal.CENTER.toString(), "horizontal text alignment"},
				new String[] {AlignHorizontal.RIGHT.toString(), "horizontal text alignment"})
				;

		public static final String SEPARATOR = "=";
		
		private String key;
		private List<AutocompletionText> autocompletionValues = new ArrayList<AutocompletionText>();

		private String value;

		GlobalSetting(String key, String value, String info) {
			this.key = key.toLowerCase();
			this.value = value;
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
		
		public String getValue() {
			return value;
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
				} else if (key.equalsIgnoreCase(GlobalSetting.HORIZONTAL_ALIGN.toString())) {
					propConfig.sethAlignGlobally(AlignHorizontal.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalSetting.VERTICAL_ALIGN.toString())) {
					propConfig.setvAlignGlobally(AlignVertical.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalSetting.ELEMENT_STYLE.toString())) {
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
