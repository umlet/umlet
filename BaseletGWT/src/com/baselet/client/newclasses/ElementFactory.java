package com.baselet.client.newclasses;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.baselet.client.copy.diagram.draw.BaseDrawHandler;
import com.baselet.client.copy.diagram.draw.geom.Rectangle;
import com.baselet.client.copy.diagram.draw.helper.ColorOwn;
import com.baselet.client.copy.element.GridElement;
import com.baselet.client.copy.umlet.element.experimental.ComponentInterface;
import com.baselet.client.copy.umlet.element.experimental.DrawHandlerInterface;
import com.baselet.client.copy.umlet.element.experimental.ElementId;
import com.baselet.client.copy.umlet.element.experimental.NewGridElement;
import com.baselet.client.copy.umlet.element.experimental.uml.Class;

public class ElementFactory {
	
	public static GridElement create(ElementId id, Rectangle rect, String panelAttributes) {
		NewGridElement element = id.createAssociatedGridElement();
		
		DrawHandlerInterface handler = new DrawHandlerInterface() {
			@Override
			public void updateLayer(NewGridElement newGridElement) { }
			@Override
			public void updatePropertyPanel() { }
			@Override
			public float getZoomFactor() { return 1.0f; }
			@Override
			public boolean displaceDrawingByOnePixel() { return false; }
		};
		
		element.init(rect, "paneltext", new GwtCanvasElementImpl(element), handler);
		element.setPanelAttributes(panelAttributes);
		return element;
	}
	
}
