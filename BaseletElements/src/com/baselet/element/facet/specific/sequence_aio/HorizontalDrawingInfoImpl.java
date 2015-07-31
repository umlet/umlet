package com.baselet.element.facet.specific.sequence_aio;

public class HorizontalDrawingInfoImpl implements HorizontalDrawingInfo {

	private final LifelineHorizontalDrawingInfo[] horizontalDrawingInfos;

	public HorizontalDrawingInfoImpl(LifelineHorizontalDrawingInfo[] horizontalDrawingInfos) {
		super();
		this.horizontalDrawingInfos = horizontalDrawingInfos;
	}

	@Override
	public LifelineHorizontalDrawingInfo getHDrawingInfo(Lifeline lifeline) {
		return horizontalDrawingInfos[lifeline.getIndex()];
	}

	@Override
	public double getSymmetricWidthTo(Lifeline ll1, Lifeline ll2, int tick) {
		if (ll1.getIndex() <= ll2.getIndex()) {
			return getHDrawingInfo(ll2).getSymmetricHorizontalEnd(tick)
					- getHDrawingInfo(ll1).getSymmetricHorizontalStart(tick);
		}
		else {
			return getHDrawingInfo(ll1).getSymmetricHorizontalEnd(tick)
					- getHDrawingInfo(ll2).getSymmetricHorizontalStart(tick);
		}
	}
}
