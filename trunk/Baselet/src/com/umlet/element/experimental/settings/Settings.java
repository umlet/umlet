package com.umlet.element.experimental.settings;

import java.util.Collection;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.umlet.element.experimental.helper.XPoints;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet;
import com.umlet.element.experimental.settings.facets.Facet;

public abstract class Settings {

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public abstract XPoints getXValues(float y, int height, int width);

	public abstract AlignVertical getVAlign();

	public abstract AlignHorizontal getHAlign();
	
	public abstract Facet[] createFacets();
	
	public Facet[] createGlobalFacets() {
		return new Facet[]{new DefaultGlobalFacet()};
	}

	private Facet[] facets;
	public final Facet[] getFacets() {
		if (facets == null) facets = createFacets();
		return facets;
	}

	private Facet[] globalFacets;
	public final Facet[] getGlobalFacets() {
		if (globalFacets == null) globalFacets = createGlobalFacets();
		return globalFacets;
	}

}