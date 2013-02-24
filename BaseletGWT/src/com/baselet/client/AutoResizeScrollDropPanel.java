package com.baselet.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

public class AutoResizeScrollDropPanel extends ScrollPanel {
	
	private static final int SCROLLBAR_HEIGHT = 28;
//	private static final int SCROLLBAR_HEIGHT = 10; // if using this height, a horizontal scrollbar gets added if width is larger than default
	
	private OwnDropPanel dropPanel;

	private DrawPanelCanvas diagramHandler;

	public AutoResizeScrollDropPanel(final DrawPanelCanvas diagramHandler) {
		this.diagramHandler = diagramHandler;
		dropPanel = new OwnDropPanel(diagramHandler);
		this.add(dropPanel);

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				updateCanvasMinimalSize();
			}

		});
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
            	updateCanvasMinimalSize();
           }
        }); 
		
	}
	
	public void updateCanvasMinimalSize() {
		diagramHandler.setMinSize(getOffsetWidth() - SCROLLBAR_HEIGHT, getOffsetHeight() - SCROLLBAR_HEIGHT);
	}
}
