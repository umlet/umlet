package com.baselet.gwt.client.view.widgets;

import com.baselet.gwt.client.view.DrawPanelDiagram;

public abstract class DownloadPopupPanel extends MyPopupPanel {

	protected DrawPanelDiagram drawPanelDiagram;

	public DownloadPopupPanel() {
		super(true, Type.POPUP);
	}

	public void init(DrawPanelDiagram drawPanel) {
		drawPanelDiagram = drawPanel;
	}

	public abstract void prepare(final FilenameAndScaleHolder filenameAndScaleHolder);

	public abstract void onData(String data, DownloadType downloadType);

}