package com.umlet.element.experimental.settings.facets;

public abstract class LocalStatelessFacet extends LocalFacet {
	@Override
	public final boolean replacesText(String line) {
		return true;
	}
}