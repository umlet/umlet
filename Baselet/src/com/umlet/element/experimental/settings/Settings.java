package com.umlet.element.experimental.settings;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;

public interface Settings {

	/**
	 * calculates the left and right x value for a certain y value
	 */
	public abstract float[] getXValues(float y, int height, int width);

	public abstract AlignVertical getVAlign();

	public abstract AlignHorizontal getHAlignBeforeLine();

	public abstract AlignHorizontal getHAlignAfterLine();

}