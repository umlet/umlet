package com.baselet.elementnew.facet.specific;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class ActiveClassFacet extends KeyValueFacet {

	public static ActiveClassFacet INSTANCE = new ActiveClassFacet();

	private ActiveClassFacet() {}

	private static enum ClassTypeEnum {
		ACTCLASS
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type", new ValueInfo(ClassTypeEnum.ACTCLASS, "make class active (double left/right border)"));
	}

	private static final int SPACING = 6;

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		ClassTypeEnum.valueOf(value.toUpperCase()); // parse the value to make sure only valid types are accepted

		state.addToHorizontalBuffer(SPACING);
		XValues xLimits = state.getXLimits(state.getyPos());
		drawer.drawLine(xLimits.getLeft(), 0, xLimits.getLeft(), state.getGridElementSize().getHeight());
		drawer.drawLine(xLimits.getRight(), 0, xLimits.getRight(), state.getGridElementSize().getHeight());
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGH; // because it changes the xlimits
	}

}
