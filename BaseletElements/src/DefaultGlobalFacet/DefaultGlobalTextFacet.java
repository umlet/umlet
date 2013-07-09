package DefaultGlobalFacet;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.settings.facets.GlobalStatelessFacet;

public class DefaultGlobalTextFacet extends GlobalStatelessFacet {

	public enum ElementStyleEnum {AUTORESIZE, RESIZE, NORESIZE, WORDWRAP}
	
	public enum GlobalTextSetting {
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
		
		private String key;
		private List<AutocompletionText> autocompletionValues = new ArrayList<AutocompletionText>();

		private String value;

		GlobalTextSetting(String key, String value, String info) {
			this.key = key.toLowerCase();
			this.value = value;
			this.autocompletionValues.add(new AutocompletionText(key.toLowerCase() + SEP + value.toLowerCase(), info, true));
		}

		GlobalTextSetting(String key, String[] ... valueInfoPairs) {
			this.key = key.toLowerCase();
			for (String[] valueInfoPair : valueInfoPairs) {
				this.autocompletionValues.add(new AutocompletionText(key.toLowerCase() + SEP + valueInfoPair[0].toLowerCase(), valueInfoPair[1], true));
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
		for (GlobalTextSetting s : GlobalTextSetting.values()) {
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
				if (key.equalsIgnoreCase(GlobalTextSetting.HORIZONTAL_ALIGN.toString())) {
					propConfig.sethAlignGlobally(AlignHorizontal.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalTextSetting.VERTICAL_ALIGN.toString())) {
					propConfig.setvAlignGlobally(AlignVertical.valueOf(value));
				} else if (key.equalsIgnoreCase(GlobalTextSetting.ELEMENT_STYLE.toString())) {
					propConfig.setElementStyle(ElementStyleEnum.valueOf(value));
				}
			} catch (Exception e) {/*any exception results in no action*/}
		}
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		for (GlobalTextSetting s : GlobalTextSetting.values()) {
			returnList.addAll(s.autocompletionValues);
		}
		return returnList.toArray(new AutocompletionText[returnList.size()]);
	}

}
