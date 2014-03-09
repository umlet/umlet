package com.baselet.elementnew.facet.specific;

import java.util.Arrays;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.KeyValueFacet;

public class ActionTypeFacet extends KeyValueFacet {
	
	public static ActionTypeFacet INSTANCE = new ActionTypeFacet();
	private ActionTypeFacet() {}

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
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width-depth(s), 0), p(s.width, s.height/2), p(s.width-depth(s), s.height), p(0, s.height), p(0, 0)));
		} else if (type == ActionTypeEnum.RECEIVE_SIGNAL) {
			propConfig.addToLeftBuffer(depth(s));
			drawer.drawLines(Arrays.asList(p(0, 0), p(s.width, 0), p(s.width, s.height), p(0, s.height), p(depth(s), s.height/2), p(0, 0)));
		}
		propConfig.setFacetResponse(ActionTypeFacet.class, true);
	}

	public static void drawAction(final BaseDrawHandler drawer, Dimension s) {
		int radius = Math.min(20, Math.min(s.width, s.height)/5);
		drawer.drawRectangleRound(0, 0, s.width, s.height, radius);
	}

	private double depth(Dimension s) {
		return s.width/5;
	}

	private PointDouble p(double x, double y) {
		return new PointDouble(x, y);
	}

}
