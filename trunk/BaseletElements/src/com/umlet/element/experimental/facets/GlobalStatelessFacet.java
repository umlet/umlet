package com.umlet.element.experimental.facets;

public abstract class GlobalStatelessFacet extends GlobalFacet {
	@Override
	public final boolean replacesText(String line) {
		return true;
	}
}
