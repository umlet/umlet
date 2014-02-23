package com.umlet.element.experimental.facets.base;

import java.util.Arrays;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class ActionType extends AbstractGlobalKeyValueFacet {
	
	private static final int DEPTH = 20;

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
		asd(ActionTypeEnum.valueOf(value.toUpperCase()), drawer, propConfig);
	}

	private void asd(final ActionTypeEnum actionType, final BaseDrawHandler drawer, final PropertiesConfig propConfig) {
		propConfig.drawDelayed(new Runnable() {
			@Override
			public void run() {
				Dimension s = propConfig.getGridElementSize();
				if (actionType == ActionTypeEnum.ACTION) {
					drawer.drawEllipse(0, 0, s.width, s.height);
				} else if (actionType == ActionTypeEnum.SEND_SIGNAL) {
					drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-DEPTH, 0), p(s.width-1, s.height/2), p(s.width-DEPTH, s.height-1), p(0, s.height-1), p(0, 0)));
				} else if (actionType == ActionTypeEnum.RECEIVE_SIGNAL) {
					drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-1, 0), p(s.width-1, s.height-1), p(0, s.height-1), p(DEPTH, s.height/2), p(0, 0)));
				} else if (actionType == ActionTypeEnum.TIMER) {

				}
			}

			private PointDouble p(double x, double y) {
				return new PointDouble(x, y);
			}
		});
	}

}
