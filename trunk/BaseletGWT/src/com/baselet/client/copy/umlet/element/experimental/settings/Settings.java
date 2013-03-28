package com.baselet.client.copy.umlet.element.experimental.settings;

import com.baselet.client.copy.control.enumerations.AlignHorizontal;
import com.baselet.client.copy.control.enumerations.AlignVertical;
import com.baselet.client.copy.diagram.draw.geom.LineHorizontal;
import com.baselet.client.copy.umlet.element.experimental.settings.facets.DefaultGlobalFacet;
import com.baselet.client.copy.umlet.element.experimental.settings.facets.Facet;

public abstract class Settings {

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public abstract LineHorizontal getXValues(float y, int height, int width);

	public abstract AlignVertical getVAlign();

	public abstract AlignHorizontal getHAlign();
	
	public abstract Facet[] createFacets();
	
	public Facet[] createGlobalFacets() {
		return new Facet[]{new DefaultGlobalFacet()};
	}

	/**
	 * facets are checked and applied during text parsing.
	 * e.g. if a line matches "--" and the facet SeparatorLine is setup for the current element,
	 * a separator line will be drawn instead of printing the text.
	 */
	private Facet[] facets;
	public final Facet[] getFacets() {
		if (facets == null) facets = createFacets();
		return facets;
	}

	/**
	 * before any other parsing, the properties panel text is parsed for matching global facets.
	 * therefore any setting made by a global facet will influence every text line, although it could be located at the bottom
	 * e.g. fg=red could be located at the bottom, but will still be applied to the whole text
	 */
	private Facet[] globalFacets;
	public final Facet[] getGlobalFacets() {
		if (globalFacets == null) globalFacets = createGlobalFacets();
		return globalFacets;
	}

}