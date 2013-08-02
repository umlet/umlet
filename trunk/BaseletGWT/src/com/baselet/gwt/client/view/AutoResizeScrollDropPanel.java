package com.baselet.gwt.client.view;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.gwt.client.Browser;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CustomScrollPanel;

public class AutoResizeScrollDropPanel extends CustomScrollPanel {
	
	private OwnDropPanel dropPanel;

	public AutoResizeScrollDropPanel(final DrawFocusPanel diagram) {
		this.setAlwaysShowScrollBars(true);
		diagram.setScrollPanel(this);
		dropPanel = new OwnDropPanel(diagram);
		this.add(dropPanel);
		
		// update size after initialization of gui has finished
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
            	diagram.redraw();
           }
        }); 
		
		// also update size everytime the mouse has been released on the scrollbar or the window has been resized
		MouseUpHandler handler = new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				diagram.redraw();
			}
		};
		getHorizontalScrollbar().asWidget().addDomHandler(handler, MouseUpEvent.getType());
		getVerticalScrollbar().asWidget().addDomHandler(handler, MouseUpEvent.getType());

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				diagram.redraw();
			}
		});
		
	}

	public Rectangle getVisibleBounds() {
		int height = getOffsetHeight();
		if (Browser.get() == Browser.FIREFOX) {
			height -= 21; // if too low, the "scroll down" arrow of the vertical scrollbar will never stop moving the diagram and the scrollbar is always visible, if too high, elements will move down if user clicks on the diagram
		}
		else {
			height -= 20; // if too low, the "scroll down" arrow of the vertical scrollbar will never stop moving the diagram and the scrollbar is always visible, if too high, elements will move down if user clicks on the diagram
		}
		return new Rectangle(getHorizontalScrollPosition(), getVerticalScrollPosition(), getOffsetWidth(), height);
	}
	
	public void moveHorizontalScrollbar(int diff) {
		setHorizontalScrollPosition(getHorizontalScrollPosition() + diff);
	}
	
	public void moveVerticalScrollbar(int diff) {
		setVerticalScrollPosition(getVerticalScrollPosition() + diff);
	}
}
