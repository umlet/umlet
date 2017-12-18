package com.baselet.control;

import java.util.HashMap;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;

public class HandlerElementMap {

	/**
	 * Workaround to avoid storing the handler directly in the GridElement
	 * (necessary as a first step in the direction of GridElements which do not know where they are painted)
	 */
	private static HashMap<GridElement, DiagramHandler> gridElementToHandlerMapping = new HashMap<GridElement, DiagramHandler>();

	public static DiagramHandler getHandlerForElement(GridElement element) {
		return gridElementToHandlerMapping.get(element);
	}

	public static DiagramHandler setHandlerForElement(GridElement element, DiagramHandler handler) {
		return gridElementToHandlerMapping.put(element, handler);
	}

}
