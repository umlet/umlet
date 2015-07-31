package com.baselet.element.facet.specific.sequence_aio;

import java.util.Comparator;
import java.util.Map;

import com.baselet.diagram.draw.DrawHandler;

// TODO change javadoc because refactoring
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
	 * Draws the element and adds interrupted Areas to the LifelineDrawingInfo.
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
	public void draw(DrawHandler drawHandler, DrawingInfo drawingInfo);

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
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, HorizontalDrawingInfo hInfo,
			double defaultTickHeight);

	/**
	 * @return the left padding of the first lifeline or NULL if no padding is needed
	 * @see #getFirstLifeline()
	 */
	public PaddingInterval getLeftPadding();

	/**
	 * @return the right padding of the last lifeline or NULL if no padding is needed
	 * @see #getLastLifeline()
	 */
	public PaddingInterval getRightPadding();

	public static class PaddingInterval {
		private int startTick;
		private int endTick;
		private double paddingValue;

		public PaddingInterval(int startTick, int endTick, double paddingValue) {
			super();
			this.startTick = startTick;
			this.endTick = endTick;
			this.paddingValue = paddingValue;
		}

		public int getStartTick() {
			return startTick;
		}

		public void setStartTick(int startTick) {
			this.startTick = startTick;
		}

		public int getEndTick() {
			return endTick;
		}

		public void setEndTick(int endTick) {
			this.endTick = endTick;
		}

		public double getPadding() {
			return paddingValue;
		}

		public void setPadding(double paddingValue) {
			this.paddingValue = paddingValue;
		}

		public static Comparator<PaddingInterval> getStartAscComparator()
		{
			return new Comparator<PaddingInterval>() {
				@Override
				public int compare(PaddingInterval o1, PaddingInterval o2) {
					return o1.getStartTick() - o2.getStartTick();
				}
			};
		}

		public static Comparator<PaddingInterval> getEndAscComparator()
		{
			return new Comparator<PaddingInterval>() {
				@Override
				public int compare(PaddingInterval o1, PaddingInterval o2) {
					return o1.getEndTick() - o2.getEndTick();
				}
			};
		}

	}
}
