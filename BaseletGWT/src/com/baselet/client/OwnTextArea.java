package com.baselet.client;

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
		this.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (handler != null) {
					handler.onValueChange(getText());
				}
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
					if (handler != null) {
						handler.onValueChange(getText());
					}
				}
			});
		}
	}

	static interface InstantValueChangeHandler {
		void onValueChange(String value);
	}

	public void addInstantValueChangeHandler(InstantValueChangeHandler handler) {
		this.handler = handler;
	}

}
