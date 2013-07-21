package com.baselet.gwt.client.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CustomScrollPanel;

public class AutoResizeScrollDropPanel extends CustomScrollPanel {
	
	private OwnDropPanel dropPanel;

	private DrawFocusPanel diagramHandler;

	public AutoResizeScrollDropPanel(final DrawFocusPanel diagramHandler) {
		this.diagramHandler = diagramHandler;
		diagramHandler.setScrollPanel(this);
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
		diagramHandler.setMinSize(getOffsetWidth(), getOffsetHeight() - 4);
	}
}
