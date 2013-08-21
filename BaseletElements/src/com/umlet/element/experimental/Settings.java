package com.umlet.element.experimental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.GlobalFacet;
import com.umlet.element.experimental.facets.GlobalFacet.Priority;
import com.umlet.element.experimental.facets.defaults.BackgroundColorFacet;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet;
import com.umlet.element.experimental.facets.defaults.FontSizeFacet;
import com.umlet.element.experimental.facets.defaults.ForegroundColorFacet;
import com.umlet.element.experimental.facets.defaults.HorizontalAlignFacet;
import com.umlet.element.experimental.facets.defaults.LayerFacet;
import com.umlet.element.experimental.facets.defaults.LineThicknessFacet;
import com.umlet.element.experimental.facets.defaults.LineTypeFacet;
import com.umlet.element.experimental.facets.defaults.VerticalAlignFacet;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

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
	
	protected GlobalFacet[] createDefaultGlobalFacets() {
		return new GlobalFacet[] {new BackgroundColorFacet(), new ElementStyleFacet(), new FontSizeFacet(), new ForegroundColorFacet(), new HorizontalAlignFacet(), new LayerFacet(), new LineThicknessFacet(), new LineTypeFacet(), new VerticalAlignFacet()};
		
	}
	
	public double getYPosStart() {
		return 0;
	}

	public double getMinElementWidthForAutoresize() {
		return 0;
	}

	private List<Facet> localFacets;
	Map<Priority, List<GlobalFacet>> globalFacets;
	private void initFacets() {
		if (localFacets == null) {
			localFacets = new ArrayList<Facet>();
			globalFacets = new HashMap<Priority, List<GlobalFacet>>();
			for (Facet f : createFacets()) {
				if (f instanceof GlobalFacet) {
					addGlobalFacet((GlobalFacet) f);
				} else {
					localFacets.add(f);
				}
			}
			for (GlobalFacet f : createDefaultGlobalFacets()) {
				addGlobalFacet(f);
			}
		}
	}

	private void addGlobalFacet(GlobalFacet f) {
		List<GlobalFacet> list = globalFacets.get(f.getPriority());
		if (list == null) {
			list = new ArrayList<GlobalFacet>();
			globalFacets.put(f.getPriority(), list);
		}
		list.add(f);
	}

	public final List<Facet> getLocalFacets() {
		initFacets();
		return localFacets;
	}

	public final Map<Priority, List<GlobalFacet>> getGlobalFacets() {
		initFacets();
		return globalFacets;
	}

}