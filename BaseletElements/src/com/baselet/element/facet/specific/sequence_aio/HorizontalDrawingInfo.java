package com.baselet.element.facet.specific.sequence_aio;

/**
 * Interface for the drawing information of a whole diagram.
 */
public interface HorizontalDrawingInfo {

	public LifelineHorizontalDrawingInfo getHDrawingInfo(Lifeline lifeline);

	/**
	 * Returns the distance between the starting point of the lifeline which starts first and the end point of the
	 * other.
	 * I.e. with no loss of generality ll1.getIndex() <= ll2.getIndex() then
	 * <code><nobr>getHDrawingInfo(ll1).getSymmetricHorizontalEnd(tick)</nobr>
	 * - <nobr>getHDrawingInfo(ll2).getSymmetricHorizontalStart(tick)</nobr></code> is returned.
	 * @param other
	 * @param tick
	 * @return
	 */
	public double getSymmetricWidthTo(Lifeline ll1, Lifeline ll2, int tick);

}
