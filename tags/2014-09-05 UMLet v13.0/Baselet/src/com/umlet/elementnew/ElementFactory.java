package com.umlet.elementnew;

import java.util.Collections;
import java.util.List;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.OldGridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.elementnew.DrawHandlerInterface;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.element.uml.relation.Relation;

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
			return ((OldGridElement) old).CloneFromMe();
		}
		else {
			return create(old.getId(), old.getRectangle().copy(), old.getPanelAttributes(), old.getAdditionalAttributes(), Main.getHandlerForElement(old));
		}
	}
}
