package com.umlet.control.diagram;

import com.umlet.element.base.Entity;
import com.umlet.listeners.CustomPreviewEntityListener;
import com.umlet.listeners.CustomPreviewListener;
import com.umlet.listeners.EntityListener;

public class CustomPreviewHandler extends DiagramHandler {
	public CustomPreviewHandler() {
		super(null,true);
		this.setListener(new CustomPreviewListener(this));
	}
	
	@Override
	public EntityListener getEntityListener(Entity e) {
		return CustomPreviewEntityListener.getInstance(this);
	}	
	
	public void closePreview()
	{
		for(Entity e : this.getDrawPanel().getAllEntities())
			this.getDrawPanel().remove(e);
	}
}
