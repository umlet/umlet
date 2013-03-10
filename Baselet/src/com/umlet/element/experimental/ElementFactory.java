package com.umlet.element.experimental;

import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.swing.BaseDrawHandlerSwing;
import com.baselet.element.Rectangle;
import com.umlet.element.experimental.uml.Class;
import com.umlet.element.experimental.uml.UseCase;

public class ElementFactory {
	public static enum ElementId {
		UMLClass, UMLUseCase;
	}

	public static NewGridElement create(String idString, Rectangle bounds, String panelAttributes, DiagramHandler handler) {
		return create(ElementId.valueOf(idString), bounds, panelAttributes, handler);
	}
	/**
	 * uses no reflection, to avoid complications with GWT
	 * @param string2 
	 * @param string 
	 * @param rectangle 
	 */
	public static NewGridElement create(ElementId id, Rectangle bounds, String panelAttributes, DiagramHandler handler) {
		NewGridElement returnObj;
		if (id == Class.ID) returnObj = new Class();
		else if (id == UseCase.ID) returnObj = new UseCase();
		else throw new RuntimeException("Unknown class id: " + id);
		
		NewGridElementJComponent component = new NewGridElementJComponent(new BaseDrawHandlerSwing(), new BaseDrawHandlerSwing(), returnObj);
		returnObj.init(bounds, panelAttributes, component, handler);
		return returnObj;
	}
}
