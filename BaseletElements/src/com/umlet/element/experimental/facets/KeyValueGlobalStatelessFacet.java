package com.umlet.element.experimental.facets;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public abstract class KeyValueGlobalStatelessFacet extends GlobalStatelessFacet {
	
	public static class KeyValue {
		private String key;
		private String value;
		private String autocompletion;
		
		public KeyValue(String key, String value, String autocompletion) {
			super();
			this.key = key;
			this.value = value;
			this.autocompletion = autocompletion;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public String getAutocompletion() {
			return autocompletion;
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
		handleValue(line.substring(getKeyWithSep().length()), drawer, propConfig);
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {
				new AutocompletionText(getKeyWithSep() + getKeyValue().getValue(), getKeyValue().getAutocompletion())
		};
	}

	private String getKeyWithSep() {
		return getKeyValue().getKey() + SEP;
	}
	
	public Priority getPriority() {
		return Priority.HIGH;
	}

}
