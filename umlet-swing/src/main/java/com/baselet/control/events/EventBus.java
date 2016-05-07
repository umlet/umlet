package com.baselet.control.events;

import net.engio.mbassy.bus.MBassador;

/**
 * Umlet currently uses a single synchronous event bus.
 * MBassador uses weak references for subscribers, therefore unsubscribing is not necessary
 */
public class EventBus {
	private static MBassador<Object> bus = new MBassador<Object>();

	public static void publish(Object event) {
		bus.publish(event);
	}

	public static void subscribe(Object listener) {
		bus.subscribe(listener);
	}
}
