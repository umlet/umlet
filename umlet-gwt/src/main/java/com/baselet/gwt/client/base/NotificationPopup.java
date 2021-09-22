package com.baselet.gwt.client.base;

import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.gwt.client.view.DrawPanel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class NotificationPopup extends PopupPanel {
	private static final int spaceRight = 20;
	private static final int spaceTop = 10;

	public NotificationPopup() {
		super(true, false);
		getElement().addClassName("notificationPopup");
	}

	public void show(String text, DrawPanel drawPanel) {
		hide();
		setWidget(new Label(text));
		Element popupElement = getElement();
		popupElement.removeClassName("light");
		popupElement.removeClassName("dark");
		switch (ThemeFactory.getActiveThemeEnum()) {
			case DARK:
				popupElement.addClassName("dark");
				break;
			case LIGHT:
			default:
				popupElement.addClassName("light");
		}

		setPopupPositionAndShow((offsetWidth, offsetTop) -> {
			// Retrieving container of panel:
			Element container = drawPanel.getElement().getParentElement().getParentElement().getParentElement();
			int aboluteLeft = container.getAbsoluteLeft();
			int clientWidth = container.getClientWidth();
			NotificationPopup.this.setPopupPosition(aboluteLeft + clientWidth - offsetWidth - spaceRight, spaceTop);
		});

		Timer t = new Timer() {
			@Override
			public void run() {
				NotificationPopup.this.hide();
			}
		};
		t.schedule(10000);
	}
}
