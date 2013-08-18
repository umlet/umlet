package com.umlet.element.experimental;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Resize;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.plotlet.element.PlotGrid;

public class ElementFactory {

	/**
	 * uses no reflection, to avoid complications with GWT
	 */
	public static NewGridElement create(ElementId id, Rectangle bounds, String panelAttributes, String additionalAttributes, DiagramHandler handler) {
		final NewGridElement returnObj;
		if (id == ElementId.PlotGrid) returnObj = new PlotGrid();
		else returnObj = id.createAssociatedGridElement();
		
		ComponentSwing component = new ComponentSwing(returnObj);
		
		DrawHandlerInterface panel = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() {
				Main.getHandlerForElement(returnObj).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
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
			public GridElement cloneElement() {
				NewGridElement old = returnObj;
				return create(old.getId(), old.getRectangle().copy(), old.getPanelAttributes(), old.getAdditionalAttributes(), Main.getHandlerForElement(old));
			}
			@Override
			public void resize(double diffw, double diffh) {
				double diffwInCurrentZoom = diffw * getZoomFactor();
				double diffhInCurrentZoom = diffh * getZoomFactor();
				int diffwRealigned = Main.getHandlerForElement(returnObj).realignToGrid(false, diffwInCurrentZoom, true);
				int diffhRealigned = Main.getHandlerForElement(returnObj).realignToGrid(false, diffhInCurrentZoom, true);
				// use resize command to move sticked relations correctly with the element
				new Resize(returnObj, 0, 0, diffwRealigned, diffhRealigned).execute(Main.getHandlerForElement(returnObj));
			}
		};

		returnObj.init(bounds, panelAttributes, additionalAttributes, component, panel);
		handler.setHandlerAndInitListeners(returnObj);
		return returnObj;
	}

	public static NewGridElement createCopy(GridElement old) {
		return create(old.getId(), old.getRectangle().copy(), old.getPanelAttributes(), old.getAdditionalAttributes(), Main.getHandlerForElement(old));
	}
}
