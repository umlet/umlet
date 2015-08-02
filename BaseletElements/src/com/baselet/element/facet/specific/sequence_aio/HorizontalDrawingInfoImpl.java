package com.baselet.element.facet.specific.sequence_aio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.baselet.element.facet.specific.sequence_aio.LifelineSpanningTickSpanningOccurrence.ContainerPadding;

public class HorizontalDrawingInfoImpl implements HorizontalDrawingInfo {

	private final LifelineHorizontalDrawingInfo[] horizontalDrawingInfos;
	private final Map<Container, Double> containerLeftPadding;
	private final Map<Container, Double> containerRightPadding;

	public HorizontalDrawingInfoImpl(double lifelineHeadLeftStart, double lifelineWidth, double lifelineXPadding,
			int lifelineCount, int lastTick, Collection<ContainerPadding> paddings) {
		containerLeftPadding = new HashMap<Container, Double>((int) (paddings.size() / 0.7));
		containerRightPadding = new HashMap<Container, Double>((int) (paddings.size() / 0.7));
		horizontalDrawingInfos = new LifelineHorizontalDrawingInfo[lifelineCount];
		for (int i = 0; i < horizontalDrawingInfos.length; i++) {
			horizontalDrawingInfos[i] = new LifelineHorizontalDrawingInfoImpl(
					getPaddings(i, true, lastTick, paddings),
					getPaddings(i, false, lastTick, paddings),
					lifelineHeadLeftStart + (lifelineWidth + lifelineXPadding) * i,
					lifelineHeadLeftStart + (lifelineWidth + lifelineXPadding) * i + lifelineWidth);
		}
	}

	private double[] getPaddings(int lifelineId, boolean left, int lastTick, Collection<ContainerPadding> allPaddings) {
		double[] lifelinePaddings = new double[lastTick + 2];
		// define 1 queue for starting of padding intervals, and the other for the ending of the intervals
		Queue<ContainerPadding> paddingQueueStart = new PriorityQueue<ContainerPadding>(5,
				ContainerPadding.getContainerStartTickLifelineAscComparator());
		LinkedList<ContainerPadding> paddingListEnd = new LinkedList<ContainerPadding>();

		for (ContainerPadding cp : allPaddings) {
			if (left && cp.getContainer().getFirstLifeline().getIndex() == lifelineId && cp.getLeftPadding() > 0) {
				paddingQueueStart.add(cp);
			}
			else if (!left && cp.getContainer().getLastLifeline().getIndex() == lifelineId && cp.getRightPadding() > 0) {
				paddingQueueStart.add(cp);
			}
		}
		for (int tick = 0; tick < lifelinePaddings.length; tick++) {
			// insert paddings that start at the current tick at the right place (endTick asc) and remove them.
			while (paddingQueueStart.peek() != null && paddingQueueStart.peek().getContainer().getStartTick() == tick) {
				paddingListEnd.addLast(paddingQueueStart.poll());
			}
			Iterator<ContainerPadding> endIter = paddingListEnd.iterator();
			while (endIter.hasNext()) {
				ContainerPadding paddingInterval = endIter.next();
				if (left) {
					if (!containerLeftPadding.containsKey(paddingInterval.getContainer())) {
						containerLeftPadding.put(paddingInterval.getContainer(), lifelinePaddings[tick]);
					}
					lifelinePaddings[tick] += paddingInterval.getLeftPadding();
				}
				else {
					if (!containerRightPadding.containsKey(paddingInterval.getContainer())) {
						containerRightPadding.put(paddingInterval.getContainer(), lifelinePaddings[tick]);
					}
					lifelinePaddings[tick] += paddingInterval.getRightPadding();
				}
				if (paddingInterval.getContainer().getEndTick() == tick) {
					endIter.remove();
				}
			}
		}
		return lifelinePaddings;
	}

	@Override
	public LifelineHorizontalDrawingInfo getHDrawingInfo(Lifeline lifeline) {
		return horizontalDrawingInfos[lifeline.getIndex()];
	}

	@Override
	public double getSymmetricWidth(Lifeline ll1, Lifeline ll2, int tick) {
		if (ll1.getIndex() <= ll2.getIndex()) {
			return getHDrawingInfo(ll2).getSymmetricHorizontalEnd(tick)
					- getHDrawingInfo(ll1).getSymmetricHorizontalStart(tick);
		}
		else {
			return getHDrawingInfo(ll1).getSymmetricHorizontalEnd(tick)
					- getHDrawingInfo(ll2).getSymmetricHorizontalStart(tick);
		}
	}

	@Override
	public double getHorizontalStart(Container container) {
		return getHDrawingInfo(container.getFirstLifeline()).getHorizontalStart() + containerLeftPadding.get(container);
	}

	@Override
	public double getHorizontalEnd(Container container) {
		return getHDrawingInfo(container.getLastLifeline()).getHorizontalEnd() - containerRightPadding.get(container);
	}

	@Override
	public double getWidth(Container container) {
		return getHorizontalEnd(container) - getHorizontalStart(container);
	}
}
