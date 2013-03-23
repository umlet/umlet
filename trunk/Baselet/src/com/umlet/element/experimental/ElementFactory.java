package com.umlet.element.experimental;

import java.awt.Component;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.swing.BaseDrawHandlerSwing;
import com.baselet.element.GridElement;
import com.umlet.element.experimental.uml.Class;
import com.umlet.element.experimental.uml.UseCase;

public class ElementFactory {

	public static NewGridElement create(String idString, Rectangle bounds, String panelAttributes, DiagramHandler handler) {
		return create(ElementId.valueOf(idString), bounds, panelAttributes, handler);
	}
	/**
	 * uses no reflection, to avoid complications with GWT
	 */
	public static NewGridElement create(ElementId id, Rectangle bounds, String panelAttributes, DiagramHandler handler) {
		final NewGridElement returnObj;
		if (id == Class.ID) returnObj = new Class();
		else if (id == UseCase.ID) returnObj = new UseCase();
		else throw new RuntimeException("Unknown class id: " + id);
		
		NewGridElementJComponent component = new NewGridElementJComponent(new BaseDrawHandlerSwing(), new BaseDrawHandlerSwing(), returnObj);
		
		DrawHandlerInterface panel = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() {
				Main.getElementHandlerMapping().get(returnObj).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
			}
			@Override
			public void updateLayer(NewGridElement newGridElement) {
				Main.getElementHandlerMapping().get(returnObj).getDrawPanel().setLayer((Component) returnObj.getComponent(), returnObj.getLayer());
			}
			@Override
			public float getZoomFactor() {
				return Main.getElementHandlerMapping().get(returnObj).getZoomFactor();
			}
			@Override
			public boolean displaceDrawingByOnePixel() {
				return Utils.displaceDrawingByOnePixel();
			}
		};

		returnObj.init(bounds, panelAttributes, component, panel);
		Main.getElementHandlerMapping().put(returnObj, handler);
		Main.getElementHandlerMapping().get(returnObj).setHandlerAndInitListeners(returnObj);
		return returnObj;
	}
	public static GridElement clone(NewGridElement old) {
		return create(old.getId(), old.getRectangle(), old.getPanelAttributes(), Main.getElementHandlerMapping().get(old));
	}
}
