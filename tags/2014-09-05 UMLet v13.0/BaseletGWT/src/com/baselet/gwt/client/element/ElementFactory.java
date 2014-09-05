package com.baselet.gwt.client.element;

import com.baselet.control.SharedConstants;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.elementnew.DrawHandlerInterface;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;

public class ElementFactory {

	public static GridElement create(ElementId id, Rectangle rect, String panelAttributes, String additionalPanelAttributes, final Diagram diagram) {
		final NewGridElement element = id.createAssociatedGridElement();

		DrawHandlerInterface handler = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() {}

			@Override
			public int getGridSize() {
				return SharedConstants.DEFAULT_GRID_SIZE; // GWT doesnt use own zoom implementation but relies on browser zoom
			}

			@Override
			public boolean displaceDrawingByOnePixel() {
				return false;
			}

			@Override
			public boolean isInitialized() {
				return true; // GWT initializes elements at once, therefore it's always initialized
			}

			@Override
			public StickableMap getStickableMap() {
				return diagram.getStickables(element);
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
