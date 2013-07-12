package com.umlet.element.experimental.settings;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.DefaultGlobalFacet;
import com.umlet.element.experimental.facets.DefaultGlobalTextFacet;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.DefaultGlobalTextFacet.ElementStyleEnum;

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

	protected boolean addDefaultGlobalFacets() {
		return true;
	}
	
	protected boolean addDefaultGlobalTextFacet() {
		return true;
	}
	
	public double getYPosStart() {
		return 0;
	}

	public double getMinElementWidthForAutoresize() {
		return 0;
	}

	private List<Facet> localFacets;
	private List<Facet> globalFacets;
	private void initFacets() {
		if (localFacets == null) {
			localFacets = new ArrayList<Facet>();
			globalFacets = new ArrayList<Facet>();
			for (Facet f : createFacets()) {
				if (f.isGlobal()) {
					globalFacets.add(f);
				} else {
					localFacets.add(f);
				}
			}
			if (addDefaultGlobalFacets()) {
				globalFacets.add(new DefaultGlobalFacet());
			}
			if (addDefaultGlobalTextFacet()) {
				globalFacets.add(new DefaultGlobalTextFacet());
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

}