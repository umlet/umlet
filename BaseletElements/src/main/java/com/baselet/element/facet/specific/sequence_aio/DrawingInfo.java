package com.baselet.element.facet.specific.sequence_aio;

/**
 * Interface for the drawing information of a whole diagram.
 */
public interface DrawingInfo extends HorizontalDrawingInfo, VerticalDrawingInfo {

	public LifelineDrawingInfo getDrawingInfo(Lifeline lifeline);

}
