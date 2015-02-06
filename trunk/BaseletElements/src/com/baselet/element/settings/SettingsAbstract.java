package com.baselet.element.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.GlobalFacet;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.BackgroundColorFacet;
import com.baselet.element.facet.common.ElementStyleFacet;
import com.baselet.element.facet.common.FontSizeFacet;
import com.baselet.element.facet.common.ForegroundColorFacet;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.facet.common.HorizontalAlignFacet;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.facet.common.LineTypeFacet;
import com.baselet.element.facet.common.LineWidthFacet;
import com.baselet.element.facet.common.VerticalAlignFacet;

public abstract class SettingsAbstract implements Settings {
	protected static final List<? extends Facet> NOTEXT = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, LineTypeFacet.INSTANCE);
	protected static final List<? extends Facet> RELATION = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, FontSizeFacet.INSTANCE);
	protected static final List<? extends Facet> AUTORESIZE = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, FontSizeFacet.INSTANCE, LineTypeFacet.INSTANCE, HorizontalAlignFacet.INSTANCE);
	protected static final List<? extends Facet> ALL = Arrays.asList(BackgroundColorFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, FontSizeFacet.INSTANCE, LineTypeFacet.INSTANCE, HorizontalAlignFacet.INSTANCE, VerticalAlignFacet.INSTANCE, ElementStyleFacet.INSTANCE);

	/**
	 * calculates the left and right x value for a certain y value
	 */
	@Override
	public abstract XValues getXValues(double y, int height, int width);

	@Override
	public abstract AlignVertical getVAlign();

	@Override
	public abstract AlignHorizontal getHAlign();

	@Override
	public abstract ElementStyleEnum getElementStyle();

	/**
	 * facets are checked and applied during text parsing.
	 * e.g. if a line matches "--" and the facet SeparatorLine is setup for the current element,
	 * a separator line will be drawn instead of printing the text.
	 *
	 * Global facets are parsed before any other ones, because they influence the whole diagram, even if they are located at the bottom
	 * e.g. fg=red could be located at the bottom, but will still be applied to the whole text
	 */
	protected abstract List<? extends Facet> createFacets();

	protected abstract List<? extends Facet> createDefaultFacets();

	private List<Facet> localFacets;
	private List<GlobalFacet> globalFacets;

	private void initFacets() {
		if (localFacets == null) {
			localFacets = new ArrayList<Facet>();
			globalFacets = new ArrayList<GlobalFacet>();
			addAll(createFacets());
			addAll(createDefaultFacets());
			sortListByPriority(localFacets);
			sortListByPriority(globalFacets);
		}
	}

	private void addAll(List<? extends Facet> facets) {
		for (Facet f : facets) {
			if (f instanceof GlobalFacet) {
				globalFacets.add((GlobalFacet) f);
			}
			else {
				localFacets.add(f);
			}
		}
	}

	/**
	 * makes sure that higher priorities are first in the list and therefore are handled first
	 */
	private void sortListByPriority(List<? extends Facet> facets) {
		Collections.sort(facets, new Comparator<Facet>() {
			@Override
			public int compare(Facet o1, Facet o2) {
				return o1.getPriority().compareTo(o2.getPriority());
			}
		});
	}

	@Override
	public final List<Facet> getLocalFacets() {
		initFacets();
		return localFacets;
	}

	@Override
	public final List<GlobalFacet> getGlobalFacets() {
		initFacets();
		return globalFacets;
	}

	@Override
	public boolean printText() {
		return true;
	}

}