package com.baselet.gwt.client.view.widgets;

import java.util.List;

import com.baselet.control.basics.geom.Point;
import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.gwt.client.base.Converter;
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
		addStyleName(getStyleNameForTheme());
		MenuBar popupMenuBar = new MenuBar(true);
		popupMenuBar.getElement().getStyle().setBackgroundColor(Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_DOCUMENT_BACKGROUND)).value());
		popupMenuBar.getElement().getStyle().setColor(Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_FOREGROUND)).value());

		for (final MenuPopupItem item : items) {
			MenuItem menuItem = new MenuItem(item.getText(), true, new ScheduledCommand() {
				@Override
				public void execute() {
					item.execute();
					hide();
				}
			});
			menuItem.addStyleName(getStyleNameForTheme());
			popupMenuBar.addItem(menuItem);
		}
		popupMenuBar.setVisible(true);
		add(popupMenuBar);
	}

	public void show(Point p) {
		setPopupPosition(p.x, p.y);
		show();
	}

	private String getStyleNameForTheme() {
		switch (ThemeFactory.getActiveThemeEnum()) {
			case LIGHT:
				return "light";
			case DARK:
				return "dark";
			default:
				return "light";
		}
	}

}
