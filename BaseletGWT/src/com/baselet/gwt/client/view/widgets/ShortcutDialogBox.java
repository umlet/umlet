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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class ShortcutDialogBox extends MyPopupPanel {
	
	private static ShortcutDialogBox instance = new ShortcutDialogBox();
	
	public static ShortcutDialogBox getInstance() {
		return instance;
	}

	public ShortcutDialogBox() {
		super(true, Type.POPUP);
		setHeader("Keyboard Shortcuts");
		
		Shortcut[] values = Shortcut.values();
		Map<Shortcut.Category, SafeHtmlBuilder> map = new HashMap<Shortcut.Category, SafeHtmlBuilder>();
		for (Shortcut.Category c : Shortcut.Category.values()) {
			SafeHtmlBuilder  builder = new SafeHtmlBuilder();
			builder.appendHtmlConstant("<table><tbody>");
			map.put(c, builder);
		}
		for (int i = 0; i < values.length; i++) {
			Shortcut shortcut = values[i];
			map.get(shortcut.getCategory()).appendHtmlConstant("<tr><td width=\"85em\">" + shortcut.getShortcut() + "</td><td>" + shortcut.getDescription() + "</td></tr>");
		}
		
		FlowPanel panel = new FlowPanel();
		boolean first = true;
		for (Shortcut.Category c : Arrays.asList(Category.DIAGRAM, Category.BROWSER, Category.PROPERTIES)) {
			String header = "<strong>" + c.getHeader() + "</strong>";
			if (!first) {
				header = "<hr/>" + header;
			}
			first = false;
			panel.add(new HTML(header));
			SafeHtmlBuilder builder = map.get(c);
			builder.appendHtmlConstant("</tbody></table>");
			panel.add(new HTML(builder.toSafeHtml()));
		}
		setWidget(panel);
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