package com.umlet.element.experimental;

import com.baselet.control.Main;
import com.baselet.control.SharedConstants;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Resize;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.OldGridElement;
import com.baselet.elementnew.DrawHandlerInterface;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;

public class ElementFactory {

	/**
	 * uses no reflection, to avoid complications with GWT
	 */
	public static NewGridElement create(ElementId id, Rectangle bounds, String panelAttributes, String additionalAttributes, DiagramHandler handler) {
		final NewGridElement returnObj = id.createAssociatedGridElement();
		
		ComponentSwing component = new ComponentSwing(returnObj);
		
		DrawHandlerInterface panel = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() {
				Main.getHandlerForElement(returnObj).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
			}
			@Override
			public int getGridSize() {
				return Main.getHandlerForElement(returnObj).getGridSize();
			}
			@Override
			public boolean displaceDrawingByOnePixel() {
				return Utils.displaceDrawingByOnePixel();
			}
			@Override
			public void resize(double diffw, double diffh, AlignHorizontal alignHorizontal) {
				double diffwInCurrentZoom = diffw * getGridSize() / SharedConstants.DEFAULT_GRID_SIZE;
				double diffhInCurrentZoom = diffh * getGridSize() / SharedConstants.DEFAULT_GRID_SIZE;
				DiagramHandler h = Main.getHandlerForElement(returnObj);
				int diffhRealigned = h.realignToGrid(false, diffhInCurrentZoom, true);

				// use resize command to move sticked relations correctly with the element
				int xDiff = 0;
				int wDiff = 0;
				if (alignHorizontal == AlignHorizontal.LEFT) {
					wDiff = h.realignToGrid(false, diffwInCurrentZoom, true);
				} else if (alignHorizontal == AlignHorizontal.RIGHT) {
					xDiff = -h.realignToGrid(false, diffwInCurrentZoom, true);
				} else if (alignHorizontal == AlignHorizontal.CENTER) {
					wDiff = h.realignToGrid(false, diffwInCurrentZoom/2, true);
					xDiff = -h.realignToGrid(false, diffwInCurrentZoom/2, true);
				}
				new Resize(returnObj, xDiff, 0, wDiff, diffhRealigned).execute(h);
			}
			@Override
			public boolean isInitialized() {
				return Main.getHandlerForElement(returnObj) != null;
			}
		};

		returnObj.init(bounds, panelAttributes, additionalAttributes, component, panel);
		handler.setHandlerAndInitListeners(returnObj);
		return returnObj;
	}

	public static GridElement createCopy(GridElement old) {
		if (old instanceof OldGridElement) {
			return ((OldGridElement) old).CloneFromMe();
		} else {
			return create(old.getId(), old.getRectangle().copy(), old.getPanelAttributes(), old.getAdditionalAttributes(), Main.getHandlerForElement(old));
		}
	}
}
