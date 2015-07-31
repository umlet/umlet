package com.baselet.element.facet.specific.sequence_aio;

public class VerticalDrawingInfoImpl implements VerticalDrawingInfo {

	// tick y padding
	// top Y
	// lef X
	// H paddings per tick per left/right
	// V paddings per tick (accumaltive offsets)

	private final double startingHeadTopY;
	private final double startingHeadHeight;
	private final double tickVerticalPadding;
	private final double defaultTickHeight;
	private final double[] accumulativeAddiontalHeightOffsets;
	private final double startingTickTopY;

	/**
	 * @param startingHeadTopY
	 * @param startingHeadHeight
	 * @param defaultTickHeight
	 * @param tickVerticalPadding <code>tickVerticalPadding = getVerticalEnd(tick)- getVerticalStart(tick+1)</code>
	 * @param addiontalHeights for every tick how much height should be added to this tick, therefore length at least
	 * lastTick + 1 so that lastTick is a valid index
	 */
	public VerticalDrawingInfoImpl(double startingHeadTopY, double startingHeadHeight, double defaultTickHeight,
			double tickVerticalPadding, double[] addiontalHeights) {
		this.startingHeadTopY = startingHeadTopY;
		this.startingHeadHeight = startingHeadHeight;
		this.tickVerticalPadding = tickVerticalPadding;
		this.defaultTickHeight = defaultTickHeight;
		accumulativeAddiontalHeightOffsets = new double[addiontalHeights.length + 1];
		double sum = 0;
		for (int i = 0; i < addiontalHeights.length; i++) {
			sum += addiontalHeights[i];
			accumulativeAddiontalHeightOffsets[i + 1] = sum;
		}
		startingTickTopY = startingHeadTopY + startingHeadHeight + tickVerticalPadding;
	}

	@Override
	public double getVerticalStart(int tick) {

		// topLeft.y + headHeight + accumulativeAddiontalHeightOffsets[created] + created * tickHeight
		// topLeft.y + headHeight + accumulativeAddiontalHeightOffsets[tick] + tick * tickHeight
		// topY + currentStartTick * tickHeight + accumulativeAddiontalHeightOffsets[currentStartTick]
		// adding y padding!
		// TODO Auto-generated method stub
		return startingTickTopY + tick * (defaultTickHeight + tickVerticalPadding) + accumulativeAddiontalHeightOffsets[tick];
	}

	@Override
	public double getVerticalEnd(int tick) {
		// TODO Auto-generated method stub
		return getVerticalStart(tick) + getTickHeight(tick);
	}

	@Override
	public double getTickHeight(int tick) {
		// TODO Auto-generated method stub
		return defaultTickHeight + accumulativeAddiontalHeightOffsets[tick + 1] - accumulativeAddiontalHeightOffsets[tick];
		// tickHeight + accumulativeAddiontalHeightOffsets[created + 1] - accumulativeAddiontalHeightOffsets[created]
	}

	@Override
	public double getTickVerticalPadding() {
		return tickVerticalPadding;
	}

	@Override
	public double getVerticalHeadStart() {
		return startingHeadTopY;
	}

	@Override
	public double getVerticalHeadEnd() {
		return getVerticalHeadStart() + startingHeadHeight;
	}

	@Override
	public double getHeadHeight() {
		return startingHeadHeight;
	}

	@Override
	public double getVerticalCenter(int tick) {
		return getVerticalStart(tick) / 2 + getVerticalEnd(tick) / 2;
	}

}
