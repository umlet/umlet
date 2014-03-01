package com.umlet.element.experimental.facets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.FormatLabels;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public abstract class AbstractKeyValueFacet extends AbstractFacet {

	public static class KeyValue {
		private String key;
		private boolean allValuesListed;
		private List<ValueInfo> valueInfos;

		public KeyValue(String key, boolean allValuesListed, String value, String info) {
			super();
			this.key = key.toLowerCase();
			this.allValuesListed = allValuesListed;
			this.valueInfos = Arrays.asList(new ValueInfo(value, info));
		}

		public KeyValue(String key, ValueInfo ... valueInfos) {
			super();
			this.key = key;
			this.allValuesListed = true;
			this.valueInfos = Arrays.asList(valueInfos);
		}

		public String getKey() {
			return key;
		}

		public List<ValueInfo> getValueInfos() {
			return valueInfos;
		}

		public String getValueString() {
			StringBuilder sb = new StringBuilder();
			if (allValuesListed) {
				sb.append("Valid are: ");
			for (ValueInfo vi : valueInfos) {
				sb.append(vi.value.toString().toLowerCase()).append(",");
			}
				sb.deleteCharAt(sb.length()-1);
			} else {
				for (ValueInfo vi : valueInfos) {
				sb.append(vi.info);
				}
			}
			return sb.toString();
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
		public Object getValue() {
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
		try {
			handleValue(value, drawer, propConfig);
		} catch (Exception e) {
			log.debug("KeyValue Error", e);
			String errorMessage = getKeyValue().getValueString();
			if (e instanceof StyleException) { // self defined exceptions overwrite the default message
				errorMessage = e.getMessage();
			}
			throw new RuntimeException(FormatLabels.BOLD.getValue() + "Invalid value:" + FormatLabels.BOLD.getValue() + "\n" + getKeyWithSep() + value + "\n" + errorMessage);
		}
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		for (ValueInfo valueInfo : getKeyValue().getValueInfos()) {
			returnList.add(new AutocompletionText(getKeyWithSep() + valueInfo.getValue().toString().toLowerCase(), valueInfo.getInfo(), valueInfo.getBase64Img()));
		}
		return returnList;
	}

	public String getKeyWithSep() {
		return getKeyValue().getKey() + SEP;
	}

}
