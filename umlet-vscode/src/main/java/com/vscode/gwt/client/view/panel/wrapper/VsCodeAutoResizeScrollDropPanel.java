package com.vscode.gwt.client.view.panel.wrapper;

import com.baselet.gwt.client.view.panel.wrapper.AutoResizeScrollDropPanel;

public class VsCodeAutoResizeScrollDropPanel extends AutoResizeScrollDropPanel {

	@Override
	public int[] getScrollbarSize() {
		return new int[] { 10, 10 };
	}
}
