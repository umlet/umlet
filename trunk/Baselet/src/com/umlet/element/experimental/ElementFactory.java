package com.umlet.element.experimental;

import java.awt.Component;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Resize;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;

public class ElementFactory {

	public static NewGridElement create(String idString, Rectangle bounds, String panelAttributes, DiagramHandler handler) {
		return create(ElementId.valueOf(idString), bounds, panelAttributes, handler);
	}
	/**
	 * uses no reflection, to avoid complications with GWT
	 */
	public static NewGridElement create(ElementId id, Rectangle bounds, String panelAttributes, DiagramHandler handler) {
		final NewGridElement returnObj = id.createAssociatedGridElement();
		
		NewGridElementJComponent component = new NewGridElementJComponent(returnObj);
		
		DrawHandlerInterface panel = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() {
				Main.getHandlerForElement(returnObj).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
			}
			@Override
			public void updateLayer(NewGridElement newGridElement) {
				Main.getHandlerForElement(returnObj).getDrawPanel().setLayer((Component) returnObj.getComponent(), returnObj.getLayer());
			}
			@Override
			public float getZoomFactor() {
				return Main.getHandlerForElement(returnObj).getZoomFactor();
			}
			@Override
			public boolean displaceDrawingByOnePixel() {
				return Utils.displaceDrawingByOnePixel();
			}
			@Override
			public GridElement clone(GridElement gridElement) {
				NewGridElement old = (NewGridElement) gridElement;
				return create(old.getId(), old.getRectangle(), old.getPanelAttributes(), Main.getHandlerForElement(old));
			}
			@Override
			public void Resize(GridElement element, float diffw, float diffh) {
				float diffwInCurrentZoom = diffw * getZoomFactor();
				float diffhInCurrentZoom = diffh * getZoomFactor();
				int diffwRealigned = Main.getHandlerForElement(element).realignToGrid(false, diffwInCurrentZoom, true);
				int diffhRealigned = Main.getHandlerForElement(element).realignToGrid(false, diffhInCurrentZoom, true);
				// use resize command to move sticked relations correctly with the element
				new Resize(element, 0, 0, diffwRealigned, diffhRealigned).execute(Main.getHandlerForElement(element));
			}
		};

		returnObj.init(bounds, panelAttributes, component, panel);
		handler.setHandlerAndInitListeners(returnObj);
		return returnObj;
	}
}
