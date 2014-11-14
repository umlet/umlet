package com.baselet.elementnew.facet;

import java.util.List;
import java.util.Map;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.geom.XValues;
import com.baselet.elementnew.facet.Facet.Priority;

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