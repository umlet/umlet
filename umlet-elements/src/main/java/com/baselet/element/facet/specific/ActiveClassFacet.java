package com.baselet.element.facet.specific;

import java.util.List;
import java.util.Locale;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

/**
 * must be in first-run because it manipulates the left buffer which is used by second-run facets
 * must handle values in parsingFinished when drawer-setup is finished
 */
public class ActiveClassFacet extends FirstRunKeyValueFacet {

	public static final ActiveClassFacet INSTANCE = new ActiveClassFacet();

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
	public void handleValue(String value, PropertiesParserState state) {
		// only act if parsing is finished to make sure DrawHandler-Setup is finished
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		if (!handledLines.isEmpty()) {
			ClassTypeEnum.valueOf(extractValue(handledLines.get(0)).toUpperCase(Locale.ENGLISH)); // parse the value to make sure only valid types are accepted

			state.getBuffer().addToLeftAndRight(SPACING);
			XValues xLimits = state.getXLimits(state.getTextPrintPosition());
			DrawHandler drawer = state.getDrawer();
			drawer.drawLine(xLimits.getLeft(), state.getBuffer().getTop(), xLimits.getLeft(), state.getGridElementSize().getHeight());
			drawer.drawLine(xLimits.getRight(), state.getBuffer().getTop(), xLimits.getRight(), state.getGridElementSize().getHeight());
		}
	}

	@Override
	public Priority getPriority() {
		return Priority.LOW; // must be after template class to work
	}

}
