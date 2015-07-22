package com.baselet.element.facet.specific.sequence_aio;

import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;

public interface LifelineSpanningTickSpanningOccurrence {

	/**
	 * @return the first lifeline (lowest index) which is covered by this occurence
	 */
	public Lifeline getFirstLifeline();

	/**
	 * @return the last lifeline (highest index) which is covered by this occurence
	 */
	public Lifeline getLastLifeline();

	/**
	 *
	 * @param drawHandler
	 * @param topY the top position of the lifeline, exactly beneath the head (if it was created on start)
	 * @param lifelinesHorizontalSpanning for each covered lifeline the start- (Line1D.low) and endpoint (Line1D.high) on the x-axis is stored
	 * @param tickHeight the default height of a tick
	 * @param accumulativeAddiontalHeightOffsets
	 * @return for each lifeline an array which contains the interrupted areas.
	 * If there are no interruptions for a lifeline then no entry is needed.
	 * It is also possible to return an array of size 0, but if there is an entry
	 * then the array must not be null.
	 */
	public Map<Lifeline, Line1D[]> draw(DrawHandler drawHandler, double topY, Line1D[] lifelinesHorizontalSpanning, double tickHeight, double[] accumulativeAddiontalHeightOffsets);

	/**
	 *
	 * @param drawHandler
	 * @return
	 */
	public double getOverallMinWidth(DrawHandler drawHandler);

	/**
	 *
	 * @param drawHandler
	 * @param size
	 * @return
	 */
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, PointDouble size);

}
