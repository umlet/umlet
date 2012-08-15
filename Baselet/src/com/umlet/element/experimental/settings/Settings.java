package com.umlet.element.experimental.settings;

import java.util.Collection;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.umlet.element.experimental.settings.text.Facet;

public interface Settings {

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public abstract float[] getXValues(float y, int height, int width);

	public abstract AlignVertical getVAlign();

	public abstract AlignHorizontal getHAlign();
	
	public Facet[] getFacets();

}