package com.baselet.gwt.client.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class WrapperView extends Composite {

	public WrapperView() {
		MainView mainView = new MainView();

		RootPanel headerEl = RootPanel.get("aswift_0_expand");
		RootPanel footerEl = RootPanel.get("footer");
		RootPanel footerTextEl = RootPanel.get("footertext");

		if (headerEl == null && footerEl == null && footerTextEl == null) {
			initWidget(mainView);
		}
		else {
			DockLayoutPanel wrapperDock = new DockLayoutPanel(Unit.PX);
			wrapperDock.addNorth(headerEl, 90);

			HTMLPanel footerPanel = new HTMLPanel("");
			footerPanel.add(footerEl);
			footerPanel.add(footerTextEl);
			wrapperDock.addSouth(footerPanel, 67);

			wrapperDock.add(mainView);
			initWidget(wrapperDock);
		}
	}

}
