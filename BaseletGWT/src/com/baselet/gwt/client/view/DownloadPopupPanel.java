package com.baselet.gwt.client.view;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;


public class DownloadPopupPanel extends PopupPanel {
	
	public DownloadPopupPanel(String uxfUrl, String pngUrl) {
		super(true);
		setGlassEnabled(true);
		setStyleName("popup");
		String html =
				"<p>\"Right click -&gt; Save as\" on the following links</p>" +
				"<p><a href='" + uxfUrl + "'>Diagram File</a></p>" +
				"<p><a href='" + pngUrl + "'>Image File</a></p>";
		SimplePanel panel = new SimplePanel(new HTML(html));
		setWidget(panel);
		center();
	}

	/**
	 * pressing ESC closes the dialogbox
	 */
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if (
				event.getTypeInt() == Event.ONKEYDOWN && 
				event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			hide();
		}
	}
}