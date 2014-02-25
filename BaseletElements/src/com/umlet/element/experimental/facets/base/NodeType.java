package com.umlet.element.experimental.facets.base;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.helper.Style;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class NodeType extends AbstractGlobalKeyValueFacet {

	public static NodeType INSTANCE = new NodeType();
	private NodeType() {}

	private enum NodeTypeEnum {INITIAL, FINAL, FLOW_FINAL, DECISION}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type", 
				new ValueInfo(NodeTypeEnum.INITIAL, "an initial node"),
				new ValueInfo(NodeTypeEnum.FINAL, "a final node for the activity"),
				new ValueInfo(NodeTypeEnum.FLOW_FINAL, "a final node for a flow"),
				new ValueInfo(NodeTypeEnum.DECISION, "a decision node"));
	}

	@Override
	public void handleValue(final String value, final BaseDrawHandler drawer, final PropertiesConfig propConfig) {
		NodeTypeEnum type = NodeTypeEnum.valueOf(value.toUpperCase());
		Dimension s = propConfig.getGridElementSize();
		if (type == NodeTypeEnum.INITIAL) {
			drawBlackCircle(drawer, s, s.width/2-1);
		} else if (type == NodeTypeEnum.FINAL) {
			drawCircle(drawer, s.width, s.width/2-1);
			drawBlackCircle(drawer, s, s.width/2-4);
		} else if (type == NodeTypeEnum.FLOW_FINAL) {

		} else if (type == NodeTypeEnum.DECISION) {

		}
		propConfig.putFacetResponse(NodeType.class, true);
	}

	private void drawBlackCircle(final BaseDrawHandler drawer, Dimension s, int radius) {
		Style style = drawer.getCurrentStyle();
		drawer.setBackgroundColor(style.getFgColor());
		drawCircle(drawer, s.width, radius);
		drawer.setStyle(style);
	}

	private void drawCircle(final BaseDrawHandler drawer, int p, int radius) {
		drawer.drawCircle(p/2, p/2, radius);
	}

}
