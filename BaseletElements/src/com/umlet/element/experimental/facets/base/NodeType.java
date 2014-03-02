package com.umlet.element.experimental.facets.base;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.diagram.draw.helper.Style;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.AbstractGlobalKeyValueFacet;

public class NodeType extends AbstractGlobalKeyValueFacet {

	public static NodeType INSTANCE = new NodeType();
	private NodeType() {}

	private enum NodeTypeEnum {INITIAL, FINAL, FLOW_FINAL, TERMINATION, DECISION}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type", 
				new ValueInfo(NodeTypeEnum.INITIAL, "an initial node"),
				new ValueInfo(NodeTypeEnum.FINAL, "a final node for the activity"),
				new ValueInfo(NodeTypeEnum.FLOW_FINAL, "a final node for a flow"),
				new ValueInfo(NodeTypeEnum.TERMINATION, "a termination node"),
				new ValueInfo(NodeTypeEnum.DECISION, "a decision node"));
	}

	@Override
	public void handleValue(final String value, final BaseDrawHandler drawer, final PropertiesConfig propConfig) {
		NodeTypeEnum type = NodeTypeEnum.valueOf(value.toUpperCase());
		Dimension s = propConfig.getGridElementSize();
		final double w = s.getWidth();
		final double h = s.getHeight();
		if (type == NodeTypeEnum.INITIAL) {
			drawBlackEllipse(drawer, w, h, 0);
		} else if (type == NodeTypeEnum.FINAL) {
			drawer.drawEllipse(0, 0, w, h);
			drawBlackEllipse(drawer, w, h, Math.max(w, h)/5);
		} else if (type == NodeTypeEnum.FLOW_FINAL) {
			drawer.drawEllipse(0, 0, w, h);
			double yPos = h / 6;
			double lowerY = h-yPos;
			XValues upperXVal = XValues.createForEllipse(yPos, h, w);
			XValues lowerXVal = XValues.createForEllipse(lowerY, h, w);
			drawer.drawLine(upperXVal.getLeft(), yPos, lowerXVal.getRight(), lowerY);
			drawer.drawLine(lowerXVal.getLeft(), lowerY, upperXVal.getRight(), yPos);
		} else if (type == NodeTypeEnum.TERMINATION) {
			drawer.drawLine(0, 0, s.getWidth(), s.getHeight());
			drawer.drawLine(s.getWidth(), 0, 0, s.getHeight());
		} else if (type == NodeTypeEnum.DECISION) {
			drawer.drawLines(new PointDouble(w/2, 0), new PointDouble(w, h/2), new PointDouble(w/2, h), new PointDouble(0, h/2), new PointDouble(w/2, 0));
		}
		propConfig.putFacetResponse(NodeType.class, true);
	}

	private void drawBlackEllipse(final BaseDrawHandler drawer, double width, double height, double radius) {
		Style style = drawer.getCurrentStyle();
		drawer.setBackgroundColor(style.getFgColor());
		drawer.drawEllipse(radius, radius, width-radius*2, height-radius*2);
		drawer.setStyle(style);
	}

}
