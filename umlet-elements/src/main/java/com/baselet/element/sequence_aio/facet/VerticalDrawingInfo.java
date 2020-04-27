package com.baselet.element.sequence_aio.facet;

/**
 * Interface for the vertical drawing information of the diagram, this information is the same for each lifeline.
 */
public interface VerticalDrawingInfo {

	/**
	 * @return the start y coordinate of the head, if it would be created at start and not with a message.
	 */
	public double getVerticalHeadStart();

	/**
	 * @return the end y coordinate of the head, if it would be created at start and not with a message.
	 */
	public double getVerticalHeadEnd();

	/**
	 * @return the height of the head, if it would be created at start and not with a message.
	 */
	public double getHeadHeight();

	/**
	 * @param tick
	 * @return the y coordinate of the start of the given tick
	 */
	public double getVerticalStart(int tick);

	/**
	 * @param tick
	 * @return the y coordinate of the end of the given tick
	 */
	public double getVerticalEnd(int tick);

	/**
	 * Returns the y coordinate of the vertical center of the tick,
	 * i.e. the point between getVerticalStart and getVerticalEnd
	 * @param tick
	 * @return the vertical center of the tick at the given tick time
	 * @see #getVerticalStart(int)
	 * @see #getVerticalEnd(int)
	 */
	public double getVerticalCenter(int tick);

	/**
	 * <code>getVerticalEnd - getVerticalStart</code>
	 * @param tick
	 * @return the height of the tick at the given tick time.
	 * @see #getVerticalStart(int)
	 * @see #getVerticalEnd(int)
	 */
	public double getTickHeight(int tick);

	/**
	 * Returns the vertical padding between two ticks.
	 * @return the vertical padding between two ticks.
	 */
	public double getTickVerticalPadding();

	/**
	 * @param container
	 * @return the y coordinate of the start of the given container
	 */
	public double getVerticalStart(Container container);

	/**
	 * @param container
	 * @return the y coordinate of the end of the given container
	 */
	public double getVerticalEnd(Container container);
}