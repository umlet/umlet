package com.baselet.gwt.client.element;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.ElementId;
import com.baselet.element.NewGridElement;
import com.baselet.element.elementnew.ElementFactory;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;

public class ElementFactoryGwt extends ElementFactory {

	public static GridElement create(ElementId id, Rectangle rect, String panelAttributes, String additionalPanelAttributes, final Diagram diagram) {
		final NewGridElement element = createAssociatedGridElement(id);

		DrawHandlerInterface handler = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() {}

			@Override
			public int getGridSize() {
				return SharedConstants.DEFAULT_GRID_SIZE; // GWT doesnt use own zoom implementation but relies on browser zoom
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
