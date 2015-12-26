package com.baselet.gwt.client.view.widgets;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class DownloadPopupPanel extends MyPopupPanel {

	public DownloadPopupPanel(String uxfUrl, String pngUrl) {
		super(true, Type.POPUP);
		setHeader("Export Diagram");
		String html = "<p>\"Right click -&gt; Save as\" on the following links</p>"
						+ "<p>" + link(uxfUrl) + "Diagram File</a></p>"
						+ "<p>" + link(pngUrl) + "Image File</a></p>";
		SimplePanel panel = new SimplePanel(new HTML(html));
		panel.addStyleName("exportPopup");
		setWidget(panel);
		center();
	}

	private String link(String uxfUrl) {
		return "<a href='" + uxfUrl.replace("'", "&apos;") + "'>"; // apostrophes in datauris must be escaped because it's the closing sign fore the href
	}
}