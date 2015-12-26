package com.baselet.gwt.client.view.widgets;

import java.util.List;

import com.baselet.control.basics.geom.Point;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MenuPopup extends MyPopupPanel {
	public static abstract class MenuPopupItem {
		private String text;

		public MenuPopupItem(String text) {
			super();
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public abstract void execute();
	}

	public MenuPopup(List<MenuPopupItem> items) {
		super(false, Type.MENU);
		MenuBar popupMenuBar = new MenuBar(true);

		for (final MenuPopupItem item : items) {
			popupMenuBar.addItem(new MenuItem(item.getText(), true, new ScheduledCommand() {
				@Override
				public void execute() {
					item.execute();
					hide();
				}
			}));
		}
		popupMenuBar.setVisible(true);
		add(popupMenuBar);
	}

	public void show(Point p) {
		setPopupPosition(p.x, p.y);
		show();
	}

}
