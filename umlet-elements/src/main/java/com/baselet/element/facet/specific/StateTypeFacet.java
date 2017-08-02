package com.baselet.element.facet.specific;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

/**
 * must be in first-run because it manipulates the left buffer which is used by second-run facets
 * must handle values in parsingFinished when drawer-setup is finished
 */
public class StateTypeFacet extends FirstRunKeyValueFacet {

	public static final StateTypeFacet INSTANCE = new StateTypeFacet();

	private StateTypeFacet() {}

	private enum ActionTypeEnum {
		STATE, SENDER, RECEIVER
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type",
				new ValueInfo(ActionTypeEnum.STATE, "a default state"),
				new ValueInfo(ActionTypeEnum.SENDER, "an action which sends a signal"),
				new ValueInfo(ActionTypeEnum.RECEIVER, "an action which receives a signal"));
	}

	@Override
	public void handleValue(final String value, final PropertiesParserState state) {
		// only act if parsing is finished to make sure DrawHandler-Setup is finished
	}

	private void drawActionState(final DrawHandler drawer, Dimension s) {
		int radius = Math.min(20, Math.min(s.width, s.height) / 5);
		drawer.drawRectangleRound(0, 0, s.width, s.height, radius);
	}

	private double depth(Dimension s) {
		return s.width / 5.0;
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		if (handledLines.isEmpty()) {
			drawActionState(state.getDrawer(), state.getGridElementSize());
		}
		else if (handledLines.size() == 1) {
			final PropertiesParserState state1 = state;
			DrawHandler drawer = state1.getDrawer();
			ActionTypeEnum type = ActionTypeEnum.valueOf(extractValue(handledLines.get(0).toUpperCase(Locale.ENGLISH)));
			Dimension s = state1.getGridElementSize();
			if (type == ActionTypeEnum.STATE) {
				drawActionState(drawer, s);
			}
			else if (type == ActionTypeEnum.SENDER) {
				drawer.drawLines(Arrays.asList(p(0, 0), p(s.width - depth(s), 0), p(s.width, s.height / 2.0), p(s.width - depth(s), s.height), p(0, s.height), p(0, 0)));
			}
			else if (type == ActionTypeEnum.RECEIVER) {
				state1.getBuffer().addToLeft(depth(s));
				drawer.drawLines(Arrays.asList(p(0, 0), p(s.width, 0), p(s.width, s.height), p(0, s.height), p(depth(s), s.height / 2.0), p(0, 0)));
			}
		}
	}

}
