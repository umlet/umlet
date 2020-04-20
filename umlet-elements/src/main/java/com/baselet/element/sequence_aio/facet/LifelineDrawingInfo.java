package com.baselet.element.sequence_aio.facet;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.SortedMergedLine1DList;

/**
 * Interface for the drawing information which is needed by a single lifeline.
 */
public interface LifelineDrawingInfo extends LifelineHorizontalDrawingInfo, VerticalDrawingInfo {

	// these two methods are not in the VerticalDrawingInfo because they are different for each lifeline
	public SortedMergedLine1DList getInterruptedAreas();

	public void addInterruptedArea(Line1D llInterruption);
}
