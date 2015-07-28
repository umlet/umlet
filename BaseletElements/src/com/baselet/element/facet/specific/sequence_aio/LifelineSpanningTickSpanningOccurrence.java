package com.baselet.element.facet.specific.sequence_aio;

import java.util.Map;

import com.baselet.control.basics.Line1D;
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
	 * @param lifelinesHorizontalSpanning for each lifeline the start- (Line1D.low) and endpoint (Line1D.high) on the x-axis is stored.
	 * The array can be accessed by the index of the lifeline
	 * @param tickHeight the default height of a tick
	 * @param accumulativeAddiontalHeightOffsets
	 * @return for each lifeline an array which contains the interrupted areas.
	 * If there are no interruptions for a lifeline then no entry is needed.
	 * It is also possible to return an array of size 0, but if there is an entry
	 * then the array must not be null. The key is the lifeline index.
	 */
	public Map<Integer, Line1D[]> draw(DrawHandler drawHandler, double topY, Line1D[] lifelinesHorizontalSpanning, double tickHeight, double[] accumulativeAddiontalHeightOffsets);

	/**
	 *
	 * @param drawHandler
	 * @param lifelineHorizontalPadding specifies how much space is between 2 adjacent lifelines
	 * @return
	 */
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding);

	/**
	 *
	 * @param drawHandler
	 * @param lifelinesHorizontalSpanning the horizontal start and end of each lifeline
	 * @return a map with the additional heights, the keys are the ticks and the values is the additional height needed
	 */
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, Line1D[] lifelinesHorizontalSpanning, double tickHeight);

}
