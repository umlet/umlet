package com.baselet.diagram;

import com.baselet.element.GridElement;
import com.baselet.gui.listener.CustomPreviewEntityListener;
import com.baselet.gui.listener.CustomPreviewListener;
import com.baselet.gui.listener.GridElementListener;

public class CustomPreviewHandler extends DiagramHandler {
	public CustomPreviewHandler() {
		super(null, true);
		this.setListener(new CustomPreviewListener(this));
	}

	@Override
	public GridElementListener getEntityListener(GridElement e) {
		return CustomPreviewEntityListener.getInstance(this);
	}

	public void closePreview() {
		for (GridElement e : this.getDrawPanel().getAllEntities())
			this.getDrawPanel().remove(e);
	}
}
