package com.baselet.element.sequence_aio.facet;

/**
 * Interface for the drawing information of a whole diagram.
 */
public interface HorizontalDrawingInfo {

	public LifelineHorizontalDrawingInfo getHDrawingInfo(Lifeline lifeline);

	/**
	 * Returns the distance between the starting point of the lifeline which starts first and the end point of the
	 * other.
	 * I.e. with no loss of generality {@code ll1.getIndex() <= ll2.getIndex()} then
	 * <pre>
	 * {@code
	 * getHDrawingInfo(ll1).getSymmetricHorizontalEnd(tick)
	 * -getHDrawingInfo(ll2).getSymmetricHorizontalStart(tick)
	 * }
	 * </pre>
	 * is returned.
	 * @param ll1
	 * @param ll2
	 * @param tick
	 * @return the symmetric width which is spanned by the two lifelines at the given tick
	 */
	public double getSymmetricWidth(Lifeline ll1, Lifeline ll2, int tick);

	public double getHorizontalStart(Container container);

	public double getHorizontalEnd(Container container);

	public double getWidth(Container container);

	public double getDiagramHorizontalStart();

	public double getDiagramHorizontalEnd();

	public double getDiagramWidth();
}
