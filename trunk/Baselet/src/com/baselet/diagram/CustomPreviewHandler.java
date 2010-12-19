package com.baselet.diagram;

import com.baselet.element.GridElement;
import com.baselet.gui.base.listener.CustomPreviewEntityListener;
import com.baselet.gui.base.listener.CustomPreviewListener;
import com.baselet.gui.base.listener.EntityListener;

public class CustomPreviewHandler extends DiagramHandler {
	public CustomPreviewHandler() {
		super(null, true);
		this.setListener(new CustomPreviewListener(this));
	}

	@Override
	public EntityListener getEntityListener(GridElement e) {
		return CustomPreviewEntityListener.getInstance(this);
	}

	public void closePreview() {
		for (GridElement e : this.getDrawPanel().getAllEntities())
			this.getDrawPanel().remove(e);
	}
}
