package com.baselet.gwt.client.view.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextArea;

public class OwnTextArea extends TextArea {

	private InstantValueChangeHandler handler;

	public OwnTextArea() {
		super();
		sinkEvents(Event.ONPASTE);
		addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireHandler();
			}
		});
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (DOM.eventGetType(event) == Event.ONPASTE) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					fireHandler();
				}
			});
		}
	}

	public void setInstantValueChangeHandler(InstantValueChangeHandler handler) {
		this.handler = handler;
	}

	public static interface InstantValueChangeHandler {
		void onValueChange(String value);
	}

	public void fireHandler() {
		handler.onValueChange(getText());
	}

}
