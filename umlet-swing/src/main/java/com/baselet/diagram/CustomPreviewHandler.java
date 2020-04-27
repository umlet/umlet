package com.baselet.diagram;

import com.baselet.element.interfaces.GridElement;
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

	@Override
	protected void initDiagramPopupMenu(boolean extendedPopupMenu) {
		/* no diagram popup menu */
	}

	@Override
	protected DrawPanel createDrawPanel() {
		return new DrawPanel(this, false); /* no startup and filedrop */
	}
}
