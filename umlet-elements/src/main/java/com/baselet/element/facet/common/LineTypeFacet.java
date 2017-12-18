package com.baselet.element.facet.common;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.enums.LineType;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class LineTypeFacet extends FirstRunKeyValueFacet {

	public static final LineTypeFacet INSTANCE = new LineTypeFacet();

	private LineTypeFacet() {}

	@Override
	public KeyValue getKeyValue() {
		List<ValueInfo> lts = new ArrayList<KeyValueFacet.ValueInfo>();
		for (LineType lt : LineType.LT_LIST) {
			lts.add(new ValueInfo(lt.getValue(), lt.getReadableText() + " lines"));
		}
		return new KeyValue("lt", lts);
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		LineType lt = null;
		for (LineType s : LineType.LT_LIST) {
			if (s.getValue().equals(value)) {
				lt = s;
			}
		}
		if (lt == null) {
			throw new RuntimeException(); // will be translated to usage message
		}
		state.getDrawer().setLineType(lt);
	}

}
