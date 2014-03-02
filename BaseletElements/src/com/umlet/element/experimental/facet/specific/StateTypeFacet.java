package com.umlet.element.experimental.facet.specific;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.diagram.draw.helper.Style;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.AbstractGlobalKeyValueFacet;

public class StateTypeFacet extends AbstractGlobalKeyValueFacet {

	public static StateTypeFacet INSTANCE = new StateTypeFacet();
	private StateTypeFacet() {}

	private enum StateTypeEnum {INITIAL, FINAL, FLOW_FINAL, TERMINATION, DECISION}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type", 
				new ValueInfo(StateTypeEnum.INITIAL, "an initial state"),
				new ValueInfo(StateTypeEnum.FINAL, "a final state for the activity"),
				new ValueInfo(StateTypeEnum.FLOW_FINAL, "a final state for a flow"),
				new ValueInfo(StateTypeEnum.TERMINATION, "a termination state"),
				new ValueInfo(StateTypeEnum.DECISION, "a decision"));
	}

	@Override
	public void handleValue(final String value, final BaseDrawHandler drawer, final PropertiesConfig propConfig) {
		StateTypeEnum type = StateTypeEnum.valueOf(value.toUpperCase());
		Dimension s = propConfig.getGridElementSize();
		final double w = s.getWidth();
		final double h = s.getHeight();
		if (type == StateTypeEnum.INITIAL) {
			drawBlackEllipse(drawer, w, h, 0);
		} else if (type == StateTypeEnum.FINAL) {
			drawer.drawEllipse(0, 0, w, h);
			drawBlackEllipse(drawer, w, h, Math.max(w, h)/5);
		} else if (type == StateTypeEnum.FLOW_FINAL) {
			drawer.drawEllipse(0, 0, w, h);
			double yPos = h / 6;
			double lowerY = h-yPos;
			XValues upperXVal = XValues.createForEllipse(yPos, h, w);
			XValues lowerXVal = XValues.createForEllipse(lowerY, h, w);
			drawer.drawLine(upperXVal.getLeft(), yPos, lowerXVal.getRight(), lowerY);
			drawer.drawLine(lowerXVal.getLeft(), lowerY, upperXVal.getRight(), yPos);
		} else if (type == StateTypeEnum.TERMINATION) {
			drawer.drawLine(0, 0, s.getWidth(), s.getHeight());
			drawer.drawLine(s.getWidth(), 0, 0, s.getHeight());
		} else if (type == StateTypeEnum.DECISION) {
			drawDecision(drawer, w, h);
		}
		propConfig.putFacetResponse(StateTypeFacet.class, true);
	}

	public static void drawDecision(final BaseDrawHandler drawer, final double w, final double h) {
		drawer.drawLines(new PointDouble(w/2, 0), new PointDouble(w, h/2), new PointDouble(w/2, h), new PointDouble(0, h/2), new PointDouble(w/2, 0));
	}

	private void drawBlackEllipse(final BaseDrawHandler drawer, double width, double height, double radius) {
		Style style = drawer.getCurrentStyle();
		drawer.setBackgroundColor(style.getFgColor());
		drawer.drawEllipse(radius, radius, width-radius*2, height-radius*2);
		drawer.setStyle(style);
	}

}
