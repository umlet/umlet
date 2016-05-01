package com.baselet.control.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.engio.mbassy.bus.MBassador;

/**
 * Umlet uses a single synchronous event bus.
 * MBassador uses weak references for subscribers, therefore unsubscribing is not necessary
 *
 * Unfortunately this EventBus implementation is not supported in GWT. Perhaps use a GWT Eventbus there instead
 */
public class EventBus {

	private static final Logger log = LoggerFactory.getLogger(EventBus.class);

	private static MBassador<Object> bus = new MBassador<Object>();

	public static void publish(Object event) {
		bus.publish(event);
		log.debug("Published event " + event);
	}

	public static void subscribe(Object listener) {
		bus.subscribe(listener);
	}
}
