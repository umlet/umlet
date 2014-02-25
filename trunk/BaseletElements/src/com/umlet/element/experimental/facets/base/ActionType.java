package com.umlet.element.experimental.facets.base;

import java.util.Arrays;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class ActionType extends AbstractGlobalKeyValueFacet {
	
	public static ActionType INSTANCE = new ActionType();
	private ActionType() {}

	private enum ActionTypeEnum {ACTION, SEND_SIGNAL, RECEIVE_SIGNAL}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type", 
				new ValueInfo(ActionTypeEnum.ACTION, "an action"),
				new ValueInfo(ActionTypeEnum.SEND_SIGNAL, "an action which sends a signal"),
				new ValueInfo(ActionTypeEnum.RECEIVE_SIGNAL, "an action which receives a signal"));
	}

	@Override
	public void handleValue(final String value, final BaseDrawHandler drawer, final PropertiesConfig propConfig) {
		ActionTypeEnum type = ActionTypeEnum.valueOf(value.toUpperCase());
		Dimension s = propConfig.getGridElementSize();
		if (type == ActionTypeEnum.ACTION) {
			drawAction(drawer, s);
		} else if (type == ActionTypeEnum.SEND_SIGNAL) {
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-depth(s), 0), p(s.width-1, s.height/2), p(s.width-depth(s), s.height-1), p(0, s.height-1), p(0, 0)));
		} else if (type == ActionTypeEnum.RECEIVE_SIGNAL) {
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-1, 0), p(s.width-1, s.height-1), p(0, s.height-1), p(depth(s), s.height/2), p(0, 0)));
		}
		propConfig.putFacetResponse(ActionType.class, true);
	}

	public static void drawAction(final BaseDrawHandler drawer, Dimension s) {
		drawer.drawRectangleRound(0, 0, s.width-1, s.height-1, Math.min(s.width, s.height)/5);
	}

	private double depth(Dimension s) {
		return s.width/5;
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}
