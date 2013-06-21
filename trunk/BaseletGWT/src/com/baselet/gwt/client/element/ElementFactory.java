package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.gwt.client.view.SelectorNew;
import com.umlet.element.experimental.DrawHandlerInterface;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;

public class ElementFactory {
	
	public static GridElement create(ElementId id, Rectangle rect, String panelAttributes, String additionalPanelAttributes, final SelectorNew selector) {
		final NewGridElement element = id.createAssociatedGridElement();
		
		DrawHandlerInterface handler = new DrawHandlerInterface() {
			@Override
			public void updateLayer() { }
			@Override
			public void updatePropertyPanel() { }
			@Override
			public float getZoomFactor() { return 1.0f; }
			@Override
			public boolean displaceDrawingByOnePixel() { return false; }
			@Override
			public GridElement clone() {
				return create(element.getId(), element.getRectangle().copy(), element.getPanelAttributes(), element.getAdditionalAttributes(), selector);
			}
			@Override
			public void Resize(float diffw, float diffh) {
				// TODO Auto-generated method stub
			}
			@Override
			public Selector getSelector() {
				return selector;
			}
		};
		
		element.init(rect, panelAttributes, additionalPanelAttributes, new GwtComponent(element), handler);
		element.setPanelAttributes(panelAttributes);
		return element;
	}
	
}
