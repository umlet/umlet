package com.baselet.element.sequence_aio.facet;

/**
 * Interface for the horizontal drawing information for a single lifeline.
 */
public interface LifelineHorizontalDrawingInfo {

	/**
	 * @return the start (left) x coordinate of the lifeline without any padding,
	 * i.e. the left x coordinate of the rectangular head (if created at start)
	 */
	public double getHorizontalStart();

	/**
	 * @return the end (right) x coordinate of the lifeline without any padding,
	 * i.e. the right x coordinate of the rectangular head (if created at start)
	 */
	public double getHorizontalEnd();

	/**
	 * Returns the left x coordinate with respect to the left padding.
	 * @param tick at which the start should be calculated, the tick is necessary to determine if a padding is present
	 * @return the start (left) x coordinate of the lifeline with left padding included, if no left padding is present
	 * (e.g. from a combined fragment border on the left side) it is equal to getHorizontalStart()
	 * @see #getHorizontalStart()
	 */
	public double getHorizontalStart(int tick);

	/**
	 * Returns the right x coordinate with respect to the right padding.
	 * @param tick at which the end should be calculated, the tick is necessary to determine if a padding is present
	 * @return the end (right) x coordinate of the lifeline with right padding included, if no right padding is present
	 * (e.g. from a combined fragment border on the right side) it is equal to getHorizontalEnd()
	 * @see #getHorizontalEnd()
	 */
	public double getHorizontalEnd(int tick);

	/**
	 * Center of the lifeline i.e. the dashed line, this must not lie at <code>getHorizontalStart + getWidth / 2</code>
	 * but always is <code>getSymmetricHorizontalStart + getSymmetricWidth / 2</code>
	 * @return the center of the lifeline
	 */
	public double getHorizontalCenter();

	/**
	 * <code>getHorizontalEnd - getHorizontalStart</code>
	 * @return the width of the lifeline
	 * @see #getHorizontalStart()
	 * @see #getHorizontalEnd()
	 */
	public double getWidth();

	/**
	 * <code>getHorizontalEnd - getHorizontalStart</code>
	 * @param tick
	 * @return the width of the lifeline at the given tick
	 * @see #getHorizontalStart(int)
	 * @see #getHorizontalEnd(int)
	 */
	public double getWidth(int tick);

	/**
	 * Returns the  left x coordinate with respect to the max(left padding, right padding).
	 * getSymmetricHorizontalStart and getSymmetricHorizontalEnd have the same distance to the center.
	 * @param tick at which the start should be calculated, the tick is necessary to determine if a padding is present
	 * @return the start (left) x coordinate of the lifeline with left and right padding included, if no padding is
	 * present (e.g. from a combined fragment border) it is equal to getHorizontalStart(). If the left padding is
	 * &gt;= right padding it is equal to getHorizontalStart(int)
	 * @see #getHorizontalStart()
	 * @see #getHorizontalStart(int)
	 * @see #getSymmetricHorizontalEnd(int)
	 */
	public double getSymmetricHorizontalStart(int tick);

	/**
	 * Returns the  right x coordinate with respect to the max(left padding, right padding).
	 * getSymmetricHorizontalStart and getSymmetricHorizontalEnd have the same distance to the center.
	 * @param tick at which the end should be calculated, the tick is necessary to determine if a padding is present
	 * @return the end (right) x coordinate of the lifeline with left and right padding included, if no padding is
	 * present (e.g. from a combined fragment border) it is equal to getHorizontalEnd(). If the left padding is
	 * &lt;= right padding it is equal to getHorizontalEnd(int)
	 * @see #getHorizontalEnd()
	 * @see #getHorizontalEnd(int)
	 * @see #getSymmetricHorizontalStart(int)
	 */
	public double getSymmetricHorizontalEnd(int tick);

	/**
	 * <code>getSymmetricHorizontalEnd - getSymmetricHorizontalStart</code>
	 * @param tick
	 * @return the symmetric width of the lifeline at the given tick
	 * @see #getSymmetricHorizontalStart(int)
	 * @see #getSymmetricHorizontalEnd(int)
	 */
	public double getSymmetricWidth(int tick);

}