package com.baselet.elementnew.facet.specific;

import java.util.Arrays;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class StateTypeFacet extends KeyValueFacet {

	public static StateTypeFacet INSTANCE = new StateTypeFacet();

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
	public void handleValue(final String value, final DrawHandler drawer, final PropertiesParserState state) {
		ActionTypeEnum type = ActionTypeEnum.valueOf(value.toUpperCase());
		Dimension s = state.getGridElementSize();
		if (type == ActionTypeEnum.STATE) {
			return; // default
		}
		else if (type == ActionTypeEnum.SENDER) {
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width - depth(s), 0), p(s.width, s.height / 2), p(s.width - depth(s), s.height), p(0, s.height), p(0, 0)));
		}
		else if (type == ActionTypeEnum.RECEIVER) {
			state.addToLeftBuffer(depth(s));
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width, 0), p(s.width, s.height), p(0, s.height), p(depth(s), s.height / 2), p(0, 0)));
		}
		state.setFacetResponse(StateTypeFacet.class, true);
	}

	public static void drawDefaultState(final DrawHandler drawer, Dimension s) {
		int radius = Math.min(20, Math.min(s.width, s.height) / 5);
		drawer.drawRectangleRound(0, 0, s.width, s.height, radius);
	}

	private double depth(Dimension s) {
		return s.width / 5;
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}
