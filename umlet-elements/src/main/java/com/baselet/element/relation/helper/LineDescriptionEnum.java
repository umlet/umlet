package com.baselet.element.relation.helper;

import com.baselet.element.facet.KeyValueFacet;

public enum LineDescriptionEnum {
	MESSAGE_START("m1", 1, true),
	MESSAGE_END("m2", 2, false),
	ROLE_START("r1", 3, true),
	ROLE_END("r2", 4, false),
	MESSAGE_MIDDLE("", 5, null) // Important: MESSAGE_MIDDLE must be the last because it can have multiple lines with indexes 5,6,7,8,... AND it matches as default if no other enum entry is valid
	;

	private final String key;
	private final int index;
	private final Boolean start;

	private LineDescriptionEnum(String key, int index, Boolean start) {
		this.key = key;
		this.index = index;
		this.start = start;
	}

	public int getIndex() {
		return index;
	}

	public String getKey() {
		return key;
	}

	public Boolean isStart() {
		return start;
	}

	public static LineDescriptionEnum forString(String line) {
		for (LineDescriptionEnum ld : LineDescriptionEnum.values()) {
			if (line.startsWith(ld.key + KeyValueFacet.SEP)) {
				return ld;
			}
		}
		return LineDescriptionEnum.MESSAGE_MIDDLE;
	}

	public boolean isMessageStartOrEnd() {
		return this == LineDescriptionEnum.MESSAGE_START || this == LineDescriptionEnum.MESSAGE_END;
	}

	public boolean isRoleStartOrEnd() {
		return this == LineDescriptionEnum.ROLE_START || this == LineDescriptionEnum.ROLE_END;
	}

}