package com.baselet.elementnew.facet.relation;

import java.util.HashMap;
import java.util.Map;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class DescriptionPositionFacet extends KeyValueFacet {

	private static final int MAX_DISP = 200;

	public static final String POS = "pos";

	public static DescriptionPositionFacet INSTANCE_START = new DescriptionPositionFacet(LineDescriptionFacet.MESSAGE_START_KEY);
	public static DescriptionPositionFacet INSTANCE_END = new DescriptionPositionFacet(LineDescriptionFacet.MESSAGE_END_KEY);

	private final String lineDesc;

	public DescriptionPositionFacet(String lineDesc) {
		super();
		this.lineDesc = lineDesc;
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(lineDesc + POS, false, "-5,7", "comma separated integers as displacement of " + lineDesc + " text (first=horizontal, second=vertical)");
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		try {
			Map<String, Point> displacements = state.getOrInitFacetResponse(DescriptionPositionFacet.class, new HashMap<String, Point>());
			String[] split = value.split(",");
			int x = Integer.valueOf(split[0]);
			int y = Integer.valueOf(split[1]);
			if (x > MAX_DISP || y > MAX_DISP) {
				throw new StyleException("max allowed displacement value is " + MAX_DISP);
			}
			displacements.put(lineDesc, new Point(x, y));
		} catch (Exception e) {
			if (e instanceof StyleException) {
				throw (StyleException) e;
			}
			throw new StyleException("value must be <integer>,<integer>");
		}

	}

}
