package com.baselet.gwt.client.element;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.umlet.element.experimental.DrawHandlerInterface;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;

public class ElementFactory {

	public static GridElement create(ElementId id, Rectangle rect, String panelAttributes, String additionalPanelAttributes, final Diagram diagram) {
		final NewGridElement element = id.createAssociatedGridElement();

		DrawHandlerInterface handler = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() { }
			@Override
			public float getZoomFactor() { return 1.0f; }
			@Override
			public boolean displaceDrawingByOnePixel() { return false; }
			@Override
			public void resize(double diffw, double diffh, AlignHorizontal alignHorizontal) {
				int diffwInt = SharedUtils.realignToGrid(false, diffw, true);
				int diffhInt = SharedUtils.realignToGrid(false, diffh, true);
				
				List<Direction> directions = null;
				if (alignHorizontal == AlignHorizontal.LEFT) {
					directions = Arrays.asList(Direction.RIGHT, Direction.DOWN);
				} else if (alignHorizontal == AlignHorizontal.RIGHT) {
					diffwInt = -diffwInt;
					directions = Arrays.asList(Direction.LEFT, Direction.DOWN);
				}else if (alignHorizontal == AlignHorizontal.CENTER) {
					diffwInt = SharedUtils.realignToGrid(false, diffwInt/2, true) * 2;
					directions = Arrays.asList(Direction.RIGHT, Direction.LEFT, Direction.DOWN);
				}
				element.drag(directions, diffwInt, diffhInt, new Point(0,0), false, true, diagram.getRelations());
			}
		};

		element.init(rect, panelAttributes, additionalPanelAttributes, new ComponentGwt(element), handler);
		element.setPanelAttributes(panelAttributes);
		return element;
	}

	public static GridElement create(GridElement src, final Diagram targetDiagram) {
		return create(src.getId(), src.getRectangle().copy(), src.getPanelAttributes(), src.getAdditionalAttributes(), targetDiagram);
	}

}
