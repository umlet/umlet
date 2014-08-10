package com.baselet.elementnew.facet.relation;

import java.util.HashMap;
import java.util.Map;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class DescriptionPositionFacet extends KeyValueFacet {

	public static final String X_KEY = "x";
	public static final String Y_KEY = "y";

	public static DescriptionPositionFacet INSTANCE_X1 = new DescriptionPositionFacet(LineDescriptionFacet.MESSAGE_START_KEY, X_KEY, "horizontal");
	public static DescriptionPositionFacet INSTANCE_Y1 = new DescriptionPositionFacet(LineDescriptionFacet.MESSAGE_START_KEY, Y_KEY, "vertical");
	public static DescriptionPositionFacet INSTANCE_X2 = new DescriptionPositionFacet(LineDescriptionFacet.MESSAGE_END_KEY, X_KEY, "horizontal");
	public static DescriptionPositionFacet INSTANCE_Y2 = new DescriptionPositionFacet(LineDescriptionFacet.MESSAGE_END_KEY, Y_KEY, "vertical");

	private final String lineDesc;
	private final String coordinate;
	private final String desc;

	public DescriptionPositionFacet(String lineDesc, String coordinate, String desc) {
		super();
		this.lineDesc = lineDesc;
		this.coordinate = coordinate;
		this.desc = desc;
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(lineDesc + coordinate, false, "-5", desc + " displacement of " + lineDesc + " text as integer (-5, 12, ...)");
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		try {
			Integer intVal = Integer.valueOf(value);
			Map<String, Integer> displacements = state.getOrInitFacetResponse(DescriptionPositionFacet.class, new HashMap<String, Integer>());
			displacements.put(lineDesc + coordinate, intVal);
		} catch (NumberFormatException e) {
			throw new StyleException("value must be an integer");
		}

	}

}
