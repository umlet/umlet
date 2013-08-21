package com.umlet.element.experimental.facets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public abstract class AbstractKeyValueFacet extends AbstractFacet {

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

		private String getKey() {
			return key;
		}

		private List<ValueInfo> getValueInfos() {
			return valueInfos;
		}
	}

	public static class ValueInfo {
		private Object value;
		private String info;
		private String base64Img;

		public ValueInfo(Object value, String info) {
			this(value, info, null);
		}
		
		public ValueInfo(Object value, String info, String base64Img) {
			super();
			this.value = value;
			this.info = info;
			this.base64Img = base64Img;
		}
		private Object getValue() {
			return value;
		}
		private String getInfo() {
			return info;
		}
		private String getBase64Img() {
			return base64Img;
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
			returnList.add(new AutocompletionText(getKeyWithSep() + valueInfo.getValue().toString().toLowerCase(), valueInfo.getInfo(), valueInfo.getBase64Img()));
		}
		return returnList;
	}

	private String getKeyWithSep() {
		return getKeyValue().getKey() + SEP;
	}

	public Priority getPriority() {
		return Priority.HIGH;
	}

}
