package com.umlet.listeners;

import java.util.HashMap;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class CustomPreviewEntityListener extends EntityListener {

	private static HashMap<DiagramHandler,CustomPreviewEntityListener> entitylistener = new HashMap<DiagramHandler,CustomPreviewEntityListener>();
	
	public static CustomPreviewEntityListener getInstance(DiagramHandler handler) {
		if(!entitylistener.containsKey(handler))
			entitylistener.put(handler, new CustomPreviewEntityListener(handler));
		return entitylistener.get(handler);
	}
	
	public CustomPreviewEntityListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mouseDoubleClicked(Entity me) {

	}
}
