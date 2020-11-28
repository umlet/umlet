package com.baselet.element.relation.helper;

public enum LineDescriptionBasePositionEnum {
	MESSAGE_MIDDLE_UP("u"), MESSAGE_MIDDLE_DOWN("d"), MESSAGE_MIDDLE_RIGHT("r"), MESSAGE_MIDDLE_LEFT("l");

	private String key;

	LineDescriptionBasePositionEnum(String key) {
		this.key = key;
	}

	public static LineDescriptionBasePositionEnum fromString(String text) {
		for (LineDescriptionBasePositionEnum baseDescriptionEnum : LineDescriptionBasePositionEnum.values()) {
			if (baseDescriptionEnum.key.equalsIgnoreCase(text)) {
				return baseDescriptionEnum;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

}