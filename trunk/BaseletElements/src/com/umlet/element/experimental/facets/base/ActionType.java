package com.umlet.element.experimental.facets.base;

import java.util.Arrays;

import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class ActionType extends AbstractGlobalKeyValueFacet {
	
	private static final int CLOCK_DIM = 40;

	public static ActionType INSTANCE = new ActionType();
	private ActionType() {}

	public enum ActionTypeEnum {ACTION, SEND_SIGNAL, RECEIVE_SIGNAL, TIMER}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type", 
				new ValueInfo(ActionTypeEnum.ACTION, "an action"),
				new ValueInfo(ActionTypeEnum.SEND_SIGNAL, "an action which sends a signal"),
				new ValueInfo(ActionTypeEnum.RECEIVE_SIGNAL, "an action which receives a signal"),
				new ValueInfo(ActionTypeEnum.TIMER, "a time event for periodic output"));
	}

	@Override
	public void handleValue(final String value, final BaseDrawHandler drawer, final PropertiesConfig propConfig) {
		ActionTypeEnum actionType = ActionTypeEnum.valueOf(value.toUpperCase());
		Dimension s = propConfig.getGridElementSize();
		if (actionType == ActionTypeEnum.ACTION) {
			drawer.drawRectangleRound(0, 0, s.width-1, s.height-1, Math.min(s.width, s.height)/5);
		} else if (actionType == ActionTypeEnum.SEND_SIGNAL) {
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-depth(s), 0), p(s.width-1, s.height/2), p(s.width-depth(s), s.height-1), p(0, s.height-1), p(0, 0)));
		} else if (actionType == ActionTypeEnum.RECEIVE_SIGNAL) {
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-1, 0), p(s.width-1, s.height-1), p(0, s.height-1), p(depth(s), s.height/2), p(0, 0)));
		} else if (actionType == ActionTypeEnum.TIMER) {
			int xClock = (s.width-CLOCK_DIM)/2;
			int x2Clock = xClock+CLOCK_DIM;
			drawer.drawLines(Arrays.asList(p(xClock, 0), p(x2Clock, CLOCK_DIM), p(xClock, CLOCK_DIM), p(x2Clock, 0), p(xClock, 0)));
			propConfig.addToYPos(CLOCK_DIM);
			propConfig.setvAlign(AlignVertical.TOP);
		}
	
	}

	private double depth(Dimension s) {
		return s.width/5;
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}
