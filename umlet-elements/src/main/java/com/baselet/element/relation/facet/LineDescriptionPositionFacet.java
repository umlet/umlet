package com.baselet.element.relation.facet;

import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.geom.Point;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.LineDescriptionEnum;

public class LineDescriptionPositionFacet extends FirstRunKeyValueFacet {

	private static final int MAX_DISP = 200;

	public static final String POS = "pos";

	public static final LineDescriptionPositionFacet INSTANCE_MESSAGE_START = new LineDescriptionPositionFacet(LineDescriptionEnum.MESSAGE_START);
	public static final LineDescriptionPositionFacet INSTANCE_MESSAGE_END = new LineDescriptionPositionFacet(LineDescriptionEnum.MESSAGE_END);
	public static final LineDescriptionPositionFacet INSTANCE_ROLE_START = new LineDescriptionPositionFacet(LineDescriptionEnum.ROLE_START);
	public static final LineDescriptionPositionFacet INSTANCE_ROLE_END = new LineDescriptionPositionFacet(LineDescriptionEnum.ROLE_END);

	private final LineDescriptionEnum lineDesc;

	public LineDescriptionPositionFacet(LineDescriptionEnum lineDesc) {
		super();
		this.lineDesc = lineDesc;
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(lineDesc.getKey() + POS, false, "-5,7", "comma separated integers as displacement of " + lineDesc + " text (first=horizontal, second=vertical)");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		try {
			Map<String, Point> displacements = state.getOrInitFacetResponse(LineDescriptionPositionFacet.class, new HashMap<String, Point>());
			String[] split = value.split(",");
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);
			if (Math.abs(x) > MAX_DISP || Math.abs(y) > MAX_DISP) {
				throw new StyleException("max allowed positive or negative displacement value is " + MAX_DISP);
			}
			displacements.put(lineDesc.getKey(), new Point(x, y));
		} catch (Exception e) {
			if (e instanceof StyleException) {
				throw (StyleException) e;
			}
			throw new StyleException("value must be <integer>,<integer>");
		}

	}

}
