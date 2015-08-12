package com.baselet.gwt.client.view.widgets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.keyboard.Shortcut.Category;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class ShortcutDialogBox extends MyPopupPanel {

	private static class TableBuilder {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		int i = 0;

		public TableBuilder() {
			builder.appendHtmlConstant("<table style='border-spacing: 0px;'><tbody>");
		}

		public void append(Shortcut shortcut) {
			String color = i++ % 2 != 0 ? "white" : "#E5E4E2";
			builder.appendHtmlConstant("<tr style='background-color:" + color + "'><td style='padding-right:0.7em'>" + shortcut.getShortcut() + "</td><td style='width:100%'>" + shortcut.getDescription() + "</td></tr>");
		}

		public HTML toHTML() {
			builder.appendHtmlConstant("</tbody></table>");
			return new HTML(builder.toSafeHtml());
		}
	}

	private static ShortcutDialogBox instance = new ShortcutDialogBox();

	public static ShortcutDialogBox getInstance() {
		instance.setWidth("30em");
		return instance;
	}

	public ShortcutDialogBox() {
		super(true, Type.POPUP);
		setHeader("Keyboard Shortcuts");

		Shortcut[] values = Shortcut.values();
		Map<Shortcut.Category, TableBuilder> map = new HashMap<Shortcut.Category, TableBuilder>();
		for (Shortcut.Category c : Shortcut.Category.values()) {
			map.put(c, new TableBuilder());
		}
		for (Shortcut shortcut : values) {
			map.get(shortcut.getCategory()).append(shortcut);
		}

		FlowPanel panel = new FlowPanel();
		boolean first = true;
		for (Shortcut.Category c : Arrays.asList(Category.values())) {
			String additional = "";
			if (!first) {
				additional = "padding-top:0.5em;margin-top:0.5em;border-top:1px solid;";
			}
			String header = "<div style='font-weight:bold;" + additional + "'>" + c.getHeader() + "</div>";
			first = false;
			panel.add(new HTML(header));
			panel.add(map.get(c).toHTML());
		}
		setWidget(panel);
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