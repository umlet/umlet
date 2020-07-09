package com.baselet.gwt.client.view.widgets;

import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.view.DrawPanel;

public abstract class DownloadPopupPanel extends MyPopupPanel {

	protected DrawPanel drawPanel;

	public DownloadPopupPanel() {
		super(true, Type.POPUP);
	}

	public void init(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
	}

	public abstract void prepare(final FilenameAndScaleHolder filenameAndScaleHolder);
}