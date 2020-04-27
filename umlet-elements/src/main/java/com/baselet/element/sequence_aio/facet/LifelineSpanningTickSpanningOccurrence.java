package com.baselet.element.sequence_aio.facet;

import java.util.Comparator;
import java.util.Map;

import com.baselet.diagram.draw.DrawHandler;

/**
 * Represents an element which may span over multiple lifelines and mutliple ticks.
 */
public interface LifelineSpanningTickSpanningOccurrence {

	/**
	 * @return the first lifeline (lowest index) which is covered by this occurrence
	 */
	public Lifeline getFirstLifeline();

	/**
	 * @return the last lifeline (highest index) which is covered by this occurrence
	 */
	public Lifeline getLastLifeline();

	/**
	 * Draws the element and adds interrupted Areas to the LifelineDrawingInfo
	 * (which can be access via {@link DrawingInfo#getDrawingInfo(Lifeline)})
	 * @param drawHandler
	 * @param drawingInfo
	 */
	public void draw(DrawHandler drawHandler, DrawingInfo drawingInfo);

	/**
	 * Returns the minimum width between the start of the first lifeline and the end of the last lifeline.
	 * @param drawHandler
	 * @param lifelineHorizontalPadding specifies how much space is between 2 adjacent lifelines
	 * (can be useful for calculating the exact minimum width)
	 * @return the minimum width of the occurrence
	 */
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding);

	/**
	 * Returns the additional heights needed by this occurrence, the tick is the key
	 * @param drawHandler
	 * @param hInfo the horizontal drawing information, from which the usable width can be calculated
	 * @param defaultTickHeight
	 * @return a map with the additional heights, the keys are the ticks and the values is the additional height needed
	 */
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, HorizontalDrawingInfo hInfo,
			double defaultTickHeight);

	public ContainerPadding getPaddingInformation();

	public static class ContainerPadding {
		private final Container container;

		private final double leftPadding;
		private final double rightPadding;
		private final double topPadding;
		private final double bottomPadding;

		public ContainerPadding(Container container, double leftPadding, double rightPadding, double topPadding, double bottomPadding) {
			super();
			this.container = container;
			this.leftPadding = leftPadding;
			this.rightPadding = rightPadding;
			this.topPadding = topPadding;
			this.bottomPadding = bottomPadding;
		}

		public Container getContainer() {
			return container;
		}

		public double getLeftPadding() {
			return leftPadding;
		}

		public double getRightPadding() {
			return rightPadding;
		}

		public double getTopPadding() {
			return topPadding;
		}

		public double getBottomPadding() {
			return bottomPadding;
		}

		public static Comparator<ContainerPadding> getContainerStartTickAscComparator()
		{
			return new Comparator<ContainerPadding>() {
				@Override
				public int compare(ContainerPadding o1, ContainerPadding o2) {
					return o1.getContainer().getStartTick() - o2.getContainer().getStartTick();
				}
			};
		}

		public static Comparator<ContainerPadding> getContainerStartTickLifelineAscComparator()
		{
			return new Comparator<ContainerPadding>() {
				@Override
				public int compare(ContainerPadding o1, ContainerPadding o2) {
					int ret = o1.getContainer().getStartTick() - o2.getContainer().getStartTick();
					if (ret == 0) {
						return o1.getContainer().getFirstLifeline().getIndex() - o2.getContainer().getFirstLifeline().getIndex();
					}
					else {
						return ret;
					}
				}
			};
		}

		public static Comparator<ContainerPadding> getContainerEndTickAscComparator()
		{
			return new Comparator<ContainerPadding>() {
				@Override
				public int compare(ContainerPadding o1, ContainerPadding o2) {
					return o1.getContainer().getEndTick() - o2.getContainer().getEndTick();
				}
			};
		}
	}
}
