package com.umlet.element.experimental.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet;
import com.umlet.element.experimental.settings.facets.Facet;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.ElementStyleEnum;

public abstract class Settings {

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public abstract XValues getXValues(double y, int height, int width);

	public abstract AlignVertical getVAlign();

	public abstract AlignHorizontal getHAlign();

	public abstract ElementStyleEnum getElementStyle();

	/**
	 * facets are checked and applied during text parsing.
	 * e.g. if a line matches "--" and the facet SeparatorLine is setup for the current element,
	 * a separator line will be drawn instead of printing the text.
	 * 
	 * Global facets are parsed before any other ones, because they influence the whole diagram, even if they are located at the bottom
	 * e.g. fg=red could be located at the bottom, but will still be applied to the whole text
	 */
	public abstract Facet[] createFacets();
	
	public int getYPosStart() {
		return 0;
	}

	private List<Facet> localFacets;
	private List<Facet> globalFacets;
	private void initFacets() {
		if (localFacets == null) {
			localFacets = new ArrayList<Facet>();
			globalFacets = new ArrayList<Facet>(getPreparseGlobalFacets()); // preparseGlobalFacets is applied again to make sure the keywords are filtered from the outputtext. it's would not be necessary for handling them because they have already been handled
			for (Facet f : createFacets()) {
				if (f.isGlobal()) {
					globalFacets.add(f);
				} else {
					localFacets.add(f);
				}
			}
		}
	}

	public final List<Facet> getLocalFacets() {
		initFacets();
		return localFacets;
	}

	public final List<Facet> getGlobalFacets() {
		initFacets();
		return globalFacets;
	}
	
	
	private List<? extends Facet> preparseFacets;
	public List<? extends Facet> createPreparseGlobalFacets() {
		return Arrays.asList(new DefaultGlobalFacet());
	}
	/**
	 * PreparseGlobalFacets manipulate important properties like element size, elementStyle, etc. and must be parsed before any other facets
	 * eg: it must be known if an element is of type AUTORESIZE, before it's size is calculated, before other global facets which could use the size (eg: {active} are applied
	 */
	public List<? extends Facet> getPreparseGlobalFacets() {
		if (preparseFacets == null) preparseFacets = createPreparseGlobalFacets();
		return preparseFacets;
	}

}