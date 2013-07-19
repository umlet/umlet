package com.baselet.gwt.client.element;

import com.baselet.control.SharedUtils;
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
				double x = (double)oldSize.getX();
				double y = (double)oldSize.getY();
				double w = (double)SharedUtils.realignToGrid(false, oldSize.getWidth()+(float)diffw, true);
				double h = (double)SharedUtils.realignToGrid(false, oldSize.getHeight()+(float)diffh, true);
				element.setRectangle(new Rectangle(x, y, w, h));
			}
		};
		
		element.init(rect, panelAttributes, additionalPanelAttributes, new GwtComponent(element), handler);
		element.setPanelAttributes(panelAttributes);
		return element;
	}
	
}
