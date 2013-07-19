package com.baselet.gwt.client.view;

import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusWidget;

public class MouseDoubleClickUtils {

	private static class Cache {
		private boolean doubleClickEnabled = true;
	}

	public interface Handler {
		void onDoubleClick(GridElement ge);
	}

	/**
	 * doubleclicks are only handled if the mouse has moved into the canvas before
	 * this is necessary to void unwanted propagation of suggestbox-selections via doubleclick
	 * TODO: a better fix would be a custom SuggestDisplay which stops mouseevent propagation after handling them
	 * @param elementCanvas 
	 */
	public static void addMouseDragHandler(final DrawFocusPanel drawPanelCanvas, FocusWidget canvas, final Handler handler) {
		final Cache storage = new Cache();

		canvas.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (storage.doubleClickEnabled) {
					handler.onDoubleClick(drawPanelCanvas.getGridElementOnPosition(new Point(event.getX(), event.getY())));
				}
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				storage.doubleClickEnabled = false;
			}
		});

		canvas.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				storage.doubleClickEnabled = true;
			}
		});
	}

}
