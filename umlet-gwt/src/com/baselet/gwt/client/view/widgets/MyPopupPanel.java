package com.baselet.gwt.client.view.widgets;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class MyPopupPanel extends PopupPanel {

	public enum Type {
		POPUP, MENU
	}

	private String header;
	private Type type;

	public MyPopupPanel(boolean glassEnabled, Type type) {
		super(true);
		setGlassEnabled(glassEnabled);
		this.type = type;

	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public void setWidget(Widget w) {
		switch (type) {
			case POPUP:
				addStyleName("centerPopup");
				w.addStyleName("centerPopupContent");
				break;
			case MENU:
				addStyleName("menuPopup");
				break;
		}

		if (header == null) {
			super.setWidget(w);
		}
		else {
			FlowPanel fp = new FlowPanel();
			fp.add(new HTML("<div class=\"popupHeader\">" + header + "</div>"));
			fp.add(w);
			super.setWidget(fp);
		}
	}

	/**
	 * pressing ESC closes the dialogbox
	 */
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if (event.getTypeInt() == Event.ONKEYDOWN &&
			event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
			hide();
		}
	}
}
