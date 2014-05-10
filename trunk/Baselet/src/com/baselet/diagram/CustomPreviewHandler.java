package com.baselet.diagram;

import com.baselet.element.GridElement;
import com.baselet.gui.listener.CustomPreviewEntityListener;
import com.baselet.gui.listener.DiagramListener;
import com.baselet.gui.listener.GridElementListener;

public class CustomPreviewHandler extends DiagramHandler {
	public CustomPreviewHandler() {
		super(null, true);
		setListener(new DiagramListener(this));
	}

	@Override
	public GridElementListener getEntityListener(GridElement e) {
		return CustomPreviewEntityListener.getInstance(this);
	}

	public void closePreview() {
		getDrawPanel().getGridElements().clear();
		getDrawPanel().removeAll();
	}
}
