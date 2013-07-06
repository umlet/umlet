package com.baselet.gwt.client.view.widgets;

import com.baselet.gwt.client.view.widgets.MyPopupPanel.Type;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

public class MenuPopup {
	public static interface MenuPopupItem {
		public String getText();
		public void execute();
	}
	
	public static void appendTo(Widget widget, MenuPopupItem ... items) {
		final MyPopupPanel popupPanel = new MyPopupPanel(false, Type.MENU);
        MenuBar popupMenuBar = new MenuBar(true);
        
        for (final MenuPopupItem item : items) {
        	popupMenuBar.addItem(new MenuItem(item.getText(), true, new ScheduledCommand() {
    			@Override
    			public void execute() {
    				item.execute();
    				popupPanel.hide();
    			}
    		}));
        }
        popupMenuBar.setVisible(true);
        popupPanel.add(popupMenuBar);
        
        widget.addDomHandler(new ContextMenuHandler() {
		    @Override
		    public void onContextMenu(ContextMenuEvent event) {
		        event.preventDefault();
		        event.stopPropagation();
				popupPanel.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
				popupPanel.show();
		    }
		},ContextMenuEvent.getType());

	}
}
