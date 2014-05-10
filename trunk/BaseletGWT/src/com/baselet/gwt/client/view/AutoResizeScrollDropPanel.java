package com.baselet.gwt.client.view;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.gwt.client.Browser;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

public class AutoResizeScrollDropPanel extends ScrollPanel {

	private OwnDropPanel dropPanel;

	public AutoResizeScrollDropPanel(final DrawPanel diagram) {
		setAlwaysShowScrollBars(true); // must be set otherwise elements can "jump around" (eg: empty diagram, class outside of top of diagram, click multiple times on diagram -> class jumps back to diagram)
		diagram.setScrollPanel(this);
		dropPanel = new OwnDropPanel(diagram);
		this.add(dropPanel);

		// update size after initialization of gui has finished
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				diagram.redraw();
			}
		});

		// also update size everytime the mouse has been released on the scrollbar or the window has been resized
		// MouseUpHandler handler = new MouseUpHandler() {
		// @Override
		// public void onMouseUp(MouseUpEvent event) {
		// diagram.redraw();
		// }
		// };
		// getHorizontalScrollbar().asWidget().addDomHandler(handler, MouseUpEvent.getType());
		// getVerticalScrollbar().asWidget().addDomHandler(handler, MouseUpEvent.getType());

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				diagram.redraw();
			}
		});

	}

	public Rectangle getVisibleBounds() {
		int width = getOffsetWidth() - getScrollbarSize()[0];
		int height = getOffsetHeight() - getScrollbarSize()[1];
		if (Browser.get() == Browser.FIREFOX || Browser.get() == Browser.INTERNET_EXPLORER) {
			height -= 4; // if too low, the "scroll down" arrow of the vertical scrollbar will never stop moving the diagram and the scrollbar is always visible, if too high, elements will move down if user clicks on the diagram
		}
		else {
			height -= 3; // if too low, the "scroll down" arrow of the vertical scrollbar will never stop moving the diagram and the scrollbar is always visible, if too high, elements will move down if user clicks on the diagram
		}
		return new Rectangle(getHorizontalScrollPosition(), getVerticalScrollPosition(), width, height);
	}

	public void moveHorizontalScrollbar(int diff) {
		setHorizontalScrollPosition(getHorizontalScrollPosition() + diff);
	}

	public void moveVerticalScrollbar(int diff) {
		setVerticalScrollPosition(getVerticalScrollPosition() + diff);
	}

	private static Integer[] scrollbarSize;

	/**
	 * returns vertical scrollbar width and horizontal scrollbar height
	 * IGNORES ZOOM LEVEL AT THE MOMENT!!
	 */
	public Integer[] getScrollbarSize() {
		if (scrollbarSize == null) {
			String[] split = getScrollbarSizeHelper().split(" ");
			scrollbarSize = new Integer[] { Integer.valueOf(split[0]), Integer.valueOf(split[1]) };
		}
		return scrollbarSize;
	}

	private final native static String getScrollbarSizeHelper() /*-{
		var inner = document.createElement('p');
		inner.style.width = "100%";
		inner.style.height = "100%";

		var outer = document.createElement('div');
		outer.style.position = "absolute";
		outer.style.top = "0px";
		outer.style.left = "0px";
		outer.style.visibility = "hidden";
		outer.style.width = "100px";
		outer.style.height = "100px";
		outer.style.overflow = "hidden";
		outer.appendChild(inner);

		document.body.appendChild(outer);

		var w1 = inner.offsetWidth;
		var h1 = inner.offsetHeight;
		outer.style.overflow = 'scroll';
		var w2 = inner.offsetWidth;
		var h2 = inner.offsetHeight;
		if (w1 == w2)
			w2 = outer.clientWidth;
		if (h1 == h2)
			h2 = outer.clientHeight;

		document.body.removeChild(outer);

		return (w1 - w2) + " " + (h1 - h2);
	}-*/;
}
