package com.baselet.elementnew.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.Facet.Priority;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.elementnew.facet.common.BackgroundColorFacet;
import com.baselet.elementnew.facet.common.ElementStyleFacet;
import com.baselet.elementnew.facet.common.ElementStyleFacet.ElementStyleEnum;
import com.baselet.elementnew.facet.common.FontSizeFacet;
import com.baselet.elementnew.facet.common.ForegroundColorFacet;
import com.baselet.elementnew.facet.common.GroupFacet;
import com.baselet.elementnew.facet.common.HorizontalAlignFacet;
import com.baselet.elementnew.facet.common.LayerFacet;
import com.baselet.elementnew.facet.common.LineTypeFacet;
import com.baselet.elementnew.facet.common.LineWidthFacet;
import com.baselet.elementnew.facet.common.VerticalAlignFacet;

public abstract class Settings {
	protected static final List<? extends Facet> NOTEXT = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, LineTypeFacet.INSTANCE);
	protected static final List<? extends Facet> RELATION = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, FontSizeFacet.INSTANCE);
	protected static final List<? extends Facet> AUTORESIZE = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, FontSizeFacet.INSTANCE, LineTypeFacet.INSTANCE, HorizontalAlignFacet.INSTANCE);
	protected static final List<? extends Facet> ALL = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, FontSizeFacet.INSTANCE, LineTypeFacet.INSTANCE, HorizontalAlignFacet.INSTANCE, VerticalAlignFacet.INSTANCE, ElementStyleFacet.INSTANCE);

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
	public abstract List<? extends Facet> createFacets();

	protected abstract List<? extends Facet> createDefaultFacets();

	private List<Facet> localFacets;
	Map<Priority, List<GlobalFacet>> globalFacets;

	private void initFacets() {
		if (localFacets == null) {
			localFacets = new ArrayList<Facet>();
			globalFacets = new HashMap<Priority, List<GlobalFacet>>();
			addAll(createFacets());
			addAll(createDefaultFacets());
		}
	}

	private void addAll(List<? extends Facet> facets) {
		for (Facet f : facets) {
			if (f instanceof GlobalFacet) {
				addGlobalFacet((GlobalFacet) f);
			}
			else {
				localFacets.add(f);
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

	public boolean printText() {
		return true;
	}

}