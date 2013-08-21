package com.umlet.element.experimental.facets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public abstract class KeyValueGlobalStatelessFacet extends GlobalStatelessFacet {
	
	public static class KeyValue {
		private String key;
		private List<ValueInfo> valueInfos;
		
		public KeyValue(String key, String value, String info) {
			super();
			this.key = key.toLowerCase();
			this.valueInfos = Arrays.asList(new ValueInfo(value, info));
		}
		
		public KeyValue(String key, ValueInfo ... valueInfos) {
			super();
			this.key = key;
			this.valueInfos = Arrays.asList(valueInfos);
		}

		public String getKey() {
			return key;
		}

		public List<ValueInfo> getValueInfos() {
			return valueInfos;
		}
		
	}

	public static class ValueInfo {
		String value;
		String info;
		
		public ValueInfo(String value, String info) {
			super();
			this.value = value.toLowerCase();
			this.info = info;
		}
		public String getValue() {
			return value;
		}
		public String getInfo() {
			return info;
		}
		
	}

	public abstract KeyValue getKeyValue();

	public abstract void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig);
	
	@Override
	public boolean checkStart(String line) {
		return line.startsWith(getKeyWithSep());
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		String value = line.substring(getKeyWithSep().length());
		handleValue(value, drawer, propConfig);
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		for (ValueInfo valueInfo : getKeyValue().getValueInfos()) {
			returnList.add(new AutocompletionText(getKeyWithSep() + valueInfo.getValue(), valueInfo.getInfo()));
		}
		return returnList;
	}

	private String getKeyWithSep() {
		return getKeyValue().key + SEP;
	}
	
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
