package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.umlet.element.experimental.DrawHandlerInterface;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;

public class ElementFactory {
	
	public static GridElement create(ElementId id, Rectangle rect, String panelAttributes, String additionalPanelAttributes) {
		final NewGridElement element = id.createAssociatedGridElement();
		
		DrawHandlerInterface handler = new DrawHandlerInterface() {
			@Override
			public void updatePropertyPanel() { }
			@Override
			public float getZoomFactor() { return 1.0f; }
			@Override
			public boolean displaceDrawingByOnePixel() { return false; }
			@Override
			public GridElement cloneElement() {
				return create(element.getId(), element.getRectangle().copy(), element.getPanelAttributes(), element.getAdditionalAttributes());
			}
			@Override
			public void Resize(double diffw, double diffh) {
				Rectangle oldSize = element.getRectangle();
				element.setRectangle(new Rectangle((double)oldSize.getX(), (double)oldSize.getY(), oldSize.getWidth()+diffw, oldSize.getHeight()+diffh));
			}
		};
		
		element.init(rect, panelAttributes, additionalPanelAttributes, new GwtComponent(element), handler);
		element.setPanelAttributes(panelAttributes);
		return element;
	}
	
}
