package com.baselet.element.relation.facet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.LineDescriptionBasePositionEnum;

public class LineDescriptionBasePositionFacet extends FirstRunKeyValueFacet {

	public static final LineDescriptionBasePositionFacet INSTANCE = new LineDescriptionBasePositionFacet();

	@Override
	public KeyValue getKeyValue() {
		List<ValueInfo> cs = new ArrayList<ValueInfo>();
		cs.add(new ValueInfo("l", "middle text is positioned left to the line"));
		cs.add(new ValueInfo("r", "middle text is positioned right to the line"));
		cs.add(new ValueInfo("u", "middle text is positioned up the line"));
		cs.add(new ValueInfo("d", "middle text is positioned down the line"));
		cs.add(new ValueInfo("ru", "middle text is positioned right and up the line"));
		cs.add(new ValueInfo("rd", "middle text is positioned right and down the line"));
		cs.add(new ValueInfo("lu", "middle text is positioned left and up the line"));
		cs.add(new ValueInfo("ld", "middle text is positioned left and down the line"));
		return new KeyValue("cbase", cs);
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		List<LineDescriptionBasePositionEnum> descriptionBasePositionEnums = state.getOrInitFacetResponse(LineDescriptionBasePositionFacet.class, new ArrayList<LineDescriptionBasePositionEnum>());
		for (int i = 0; i < value.length(); i++) {
			String basePositionCommand = String.valueOf(value.charAt(i));
			LineDescriptionBasePositionEnum basePositionEnum = LineDescriptionBasePositionEnum.fromString(basePositionCommand);
			validateBasePositionValues(descriptionBasePositionEnums, basePositionEnum);
		}
	}

	private void validateBasePositionValues(List<LineDescriptionBasePositionEnum> descriptionBasePositionEnums, LineDescriptionBasePositionEnum basePositionEnum) {
		if (!Arrays.asList(LineDescriptionBasePositionEnum.values()).contains(basePositionEnum)) {
			throw new StyleException("Allowed values: r l u d");
		}

		if (descriptionBasePositionEnums.contains(basePositionEnum)) {
			throw new StyleException("Only unique base pos values allowed");
		}
		else {
			descriptionBasePositionEnums.add(basePositionEnum);
		}

		if (descriptionBasePositionEnums.size() > 2) {
			throw new StyleException("Max amount of base position values is 2");
		}

		if (descriptionBasePositionEnums.contains(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_LEFT) && descriptionBasePositionEnums.contains(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_RIGHT)) {
			throw new StyleException("Cannot combine right and left");
		}

		if (descriptionBasePositionEnums.contains(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_UP) && descriptionBasePositionEnums.contains(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_DOWN)) {
			throw new StyleException("Cannot combine up and down");
		}
	}

}
