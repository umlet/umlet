package com.umlet.element.experimental.facets;

public abstract class GlobalFacet implements Facet {

	/**
	 * priority enum, must be ordered from highest to lowest priority!
	 */
	public enum Priority {HIGH, MEDIUM}
	
	/**
	 * facets with higher priority will be applied before facets with lower priority
	 */
	public Priority getPriority() {
		return Priority.MEDIUM;
	}
}
