package com.baselet.element.facet;

import java.util.List;
import java.util.Map;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.element.facet.Facet.Priority;

/**
 * The basic settings of any NewGridElement.
 * They represent the default values for many important Facets (valign, halign, style) and defines the facets which should be applied to this element
 * It also specifies if the default text printing should be enabled for this element (e.g. Relation has its own text printing logic)
 */
public interface Settings {

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public abstract XValues getXValues(double y, int height, int width);

	public abstract AlignVertical getVAlign();

	public abstract AlignHorizontal getHAlign();

	public abstract ElementStyleEnum getElementStyle();

	public abstract List<Facet> getLocalFacets();

	public abstract Map<Priority, List<GlobalFacet>> getGlobalFacets();

	public abstract boolean printText();

}