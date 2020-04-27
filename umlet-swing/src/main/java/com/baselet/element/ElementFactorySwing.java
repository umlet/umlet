package com.baselet.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.elementnew.ElementFactory;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.relation.Relation;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;

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
				HandlerElementMap.getHandlerForElement(returnObj).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
			}

			@Override
			public int getGridSize() {
				return HandlerElementMap.getHandlerForElement(returnObj).getGridSize();
			}

			@Override
			public boolean isInitialized() {
				return HandlerElementMap.getHandlerForElement(returnObj) != null;
			}

			@Override
			public StickableMap getStickableMap() {
				DiagramHandler h = HandlerElementMap.getHandlerForElement(returnObj);
				List<Relation> stickables = h.getDrawPanel().getStickables(Collections.<GridElement> emptyList());
				return Stickables.getStickingPointsWhichAreConnectedToStickingPolygon(returnObj.generateStickingBorder(), stickables);
			}
		};

		returnObj.init(bounds, panelAttributes, additionalAttributes, component, panel);
		handler.setHandlerAndInitListeners(returnObj);
		return returnObj;
	}

	public static GridElement createCopy(GridElement src) {
		if (src instanceof OldGridElement) {
			return ((OldGridElement) src).cloneFromMe();
		}
		else {
			return create(src.getId(), src.getRectangle().copy(), src.getPanelAttributes(), src.getAdditionalAttributes(), HandlerElementMap.getHandlerForElement(src));
		}
	}

	public static GridElement createCopy(GridElement src, DiagramHandler handler) {
		GridElement returnElement = createCopy(src);
		handler.setHandlerAndInitListeners(returnElement);
		return returnElement;
	}

	public static List<GridElement> createCopy(Collection<GridElement> src) {
		List<GridElement> list = new ArrayList<GridElement>();
		for (GridElement o : src) {
			list.add(createCopy(o));
		}
		return list;
	}

}