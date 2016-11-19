package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;

public class LifelineHorizontalDrawingInfoImpl implements LifelineHorizontalDrawingInfo {

	private final double[] leftPaddings;
	private final double[] rightPaddings;
	private final double startX;
	private final double endX;

	public LifelineHorizontalDrawingInfoImpl(double[] leftPaddings, double[] rightPaddings, double startX, double endX) {
		super();
		this.leftPaddings = Arrays.copyOf(leftPaddings, leftPaddings.length);
		this.rightPaddings = Arrays.copyOf(rightPaddings, rightPaddings.length);
		this.startX = startX;
		this.endX = endX;
	}

	@Override
	public double getHorizontalStart() {
		return startX;
	}

	@Override
	public double getHorizontalEnd() {
		return endX;
	}

	@Override
	public double getHorizontalStart(int tick) {
		return getHorizontalStart() + leftPaddings[tick];
	}

	@Override
	public double getHorizontalEnd(int tick) {
		return getHorizontalEnd() - rightPaddings[tick];
	}

	@Override
	public double getHorizontalCenter() {
		return (startX + endX) / 2.0;
	}

	@Override
	public double getWidth() {
		return endX - startX;
	}

	@Override
	public double getWidth(int tick) {
		return getHorizontalEnd(tick) - getHorizontalStart(tick);
	}

	@Override
	public double getSymmetricHorizontalStart(int tick) {
		return getHorizontalStart() + Math.max(leftPaddings[tick], rightPaddings[tick]);
	}

	@Override
	public double getSymmetricHorizontalEnd(int tick) {
		return getHorizontalEnd() - Math.max(leftPaddings[tick], rightPaddings[tick]);
	}

	@Override
	public double getSymmetricWidth(int tick) {
		return getWidth() - Math.max(leftPaddings[tick], rightPaddings[tick]) * 2;
	}

}
