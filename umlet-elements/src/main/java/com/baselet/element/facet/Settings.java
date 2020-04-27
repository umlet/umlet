package com.baselet.element.facet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.ElementStyle;
import com.baselet.element.facet.common.BackgroundColorFacet;
import com.baselet.element.facet.common.CommentFacet;
import com.baselet.element.facet.common.ElementStyleFacet;
import com.baselet.element.facet.common.FontSizeFacet;
import com.baselet.element.facet.common.ForegroundColorFacet;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.facet.common.HorizontalAlignFacet;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.facet.common.LineTypeFacet;
import com.baselet.element.facet.common.LineWidthFacet;
import com.baselet.element.facet.common.SeparatorLineFacet;
import com.baselet.element.facet.common.TextPrintFacet;
import com.baselet.element.facet.common.TransparencyFacet;
import com.baselet.element.facet.common.VerticalAlignFacet;
import com.baselet.element.facet.customdrawings.CustomDrawingFacet;
import com.baselet.element.facet.specific.HierarchyFacet;
import com.baselet.element.relation.facet.LineDescriptionFacet;
import com.baselet.element.relation.facet.LineDescriptionPositionFacet;
import com.baselet.element.relation.facet.RelationLineTypeFacet;

/**
 * The basic settings of any NewGridElement.
 * They represent the default values for many important Facets (valign, halign, style) and defines the facets which should be applied to this element
 * It also specifies if the default text printing should be enabled for this element (e.g. Relation has its own text printing logic)
 */
public abstract class Settings {
	// the following lists are default facet configurations. they are declared here as a simple overview and for easy reuse
	protected static final List<Facet> BASE = listOf(BackgroundColorFacet.INSTANCE, TransparencyFacet.INSTANCE, ForegroundColorFacet.INSTANCE, LayerFacet.INSTANCE, LineWidthFacet.INSTANCE, GroupFacet.INSTANCE, CommentFacet.INSTANCE);
	protected static final List<Facet> BASE_WITH_LINETYPE = listOf(BASE, LineTypeFacet.INSTANCE, CustomDrawingFacet.INSTANCE);
	protected static final List<Facet> BASE_EXTENDED = listOf(BASE_WITH_LINETYPE, TextPrintFacet.INSTANCE, FontSizeFacet.INSTANCE);

	protected static final List<Facet> RELATION = listOf(BASE, FontSizeFacet.INSTANCE, RelationLineTypeFacet.INSTANCE, LineDescriptionFacet.INSTANCE, LineDescriptionPositionFacet.INSTANCE_MESSAGE_START, LineDescriptionPositionFacet.INSTANCE_MESSAGE_END, LineDescriptionPositionFacet.INSTANCE_ROLE_START, LineDescriptionPositionFacet.INSTANCE_ROLE_END);
	protected static final List<Facet> MANUALRESIZE = listOf(BASE_EXTENDED, VerticalAlignFacet.INSTANCE, HorizontalAlignFacet.INSTANCE, ElementStyleFacet.INSTANCE);
	protected static final List<Facet> NOTEXT = BASE_WITH_LINETYPE;
	protected static final List<Facet> AUTORESIZE = listOf(BASE_EXTENDED, SeparatorLineFacet.INSTANCE);
	protected static final List<Facet> HIERARCHY = listOf(BASE_WITH_LINETYPE, FontSizeFacet.INSTANCE, ElementStyleFacet.INSTANCE_AUTORESIZEONLY, HierarchyFacet.INSTANCE);

	protected static List<Facet> listOf(Facet... f) {
		List<Facet> facetList = new ArrayList<Facet>();
		facetList.addAll(Arrays.asList(f));
		return facetList;
	}

	protected static List<Facet> listOf(List<Facet> list, Facet... f) {
		List<Facet> facetList = new ArrayList<Facet>(list);
		facetList.addAll(Arrays.asList(f));
		return facetList;
	}

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public XValues getXValues(double y, int height, int width) {
		return new XValues(0, width); // default is rectangle form
	}

	public AlignVertical getVAlign() {
		return AlignVertical.TOP;
	}

	public AlignHorizontal getHAlign() {
		return AlignHorizontal.CENTER;
	}

	public abstract ElementStyle getElementStyle();

	/**
	 * facets are checked and applied during text parsing.
	 * e.g. if a line matches "--" and the facet SeparatorLine is setup for the current element,
	 * a separator line will be drawn instead of printing the text.
	 *
	 * First-run facets are parsed before any other ones, because they influence the whole diagram, even if they are located at the bottom
	 * e.g. style=wordwrap may be located at the bottom but has an influence on every printed line
	 */
	protected abstract List<Facet> createFacets();

	private List<Facet> firstRunFacets;
	private List<Facet> secondRunFacets;

	private void initFacets() {
		if (firstRunFacets == null) {
			firstRunFacets = new ArrayList<Facet>();
			secondRunFacets = new ArrayList<Facet>();
			addAll(createFacets());
			sortListByPriority(firstRunFacets);
			sortListByPriority(secondRunFacets);
		}
	}

	private void addAll(List<Facet> facets) {
		for (Facet f : facets) {
			if (f.handleOnFirstRun()) {
				secondRunFacets.add(f);
			}
			else {
				firstRunFacets.add(f);
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

	public final List<Facet> getFacetsForSecondRun() {
		initFacets();
		return firstRunFacets;
	}

	public final List<Facet> getFacetsForFirstRun() {
		initFacets();
		return secondRunFacets;
	}

}