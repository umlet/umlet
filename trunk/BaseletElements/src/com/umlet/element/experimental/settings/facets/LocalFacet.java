package com.umlet.element.experimental.settings.facets;

public abstract class LocalFacet implements Facet {
	@Override
	public final boolean isGlobal() {
		return false;
	}
}
