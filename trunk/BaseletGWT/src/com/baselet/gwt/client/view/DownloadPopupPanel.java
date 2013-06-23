package com.baselet.gwt.client.view;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;


public class DownloadPopupPanel {
	
	public DownloadPopupPanel(String uxfUrl, String pngUrl) {
		PopupPanel popup = new PopupPanel(true);
		popup.setGlassEnabled(true);
		popup.setStyleName("popup");
		String html =
				"<p>\"Right click -&gt; Save as\" on the following links</p>" +
				"<p><a href='" + uxfUrl + "'>Diagram File</a></p>" +
				"<p><a href='" + pngUrl + "'>Image File</a></p>";
		SimplePanel panel = new SimplePanel(new HTML(html));
		popup.setWidget(panel);
		popup.center();
	}
}