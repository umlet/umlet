package com.umlet.element.experimental.facets;

public abstract class LocalFacet implements Facet {
	public boolean replacesText(String line) {
		return true;
	}
}
