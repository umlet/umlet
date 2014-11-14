package com.umlet.elementnew;

import java.util.Collections;
import java.util.List;

import com.baselet.control.Main;
import com.baselet.control.enums.ElementId;
import com.baselet.control.geom.Rectangle;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.elementnew.ElementFactory;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.relation.Relation;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.umlet.element.OldGridElement;

public class ElementFactorySwing extends ElementFactory {

	/**
	 * uses no reflection, to avoid complications with GWT
	 */
	public static NewGridElement create(ElementId id, Rectangle bounds, String panelAttributes, String additionalAttributes, DiagramHandler handler) {
		final NewGridElement returnObj = createAssociatedGridElement(id);

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
			public boolean isInitialized() {
				return Main.getHandlerForElement(returnObj) != null;
			}

			@Override
			public StickableMap getStickableMap() {
				DiagramHandler h = Main.getHandlerForElement(returnObj);
				List<Relation> stickables = h.getDrawPanel().getStickables(Collections.<GridElement> emptyList());
				return Stickables.getStickingPointsWhichAreConnectedToStickingPolygon(returnObj.generateStickingBorder(returnObj.getRectangle()), stickables, h.getGridSize());
			}
		};

		returnObj.init(bounds, panelAttributes, additionalAttributes, component, panel);
		handler.setHandlerAndInitListeners(returnObj);
		return returnObj;
	}

	public static GridElement createCopy(GridElement old) {
		if (old instanceof OldGridElement) {
			return ((OldGridElement) old).cloneFromMe();
		}
		else {
			return create(old.getId(), old.getRectangle().copy(), old.getPanelAttributes(), old.getAdditionalAttributes(), Main.getHandlerForElement(old));
		}
	}
}
