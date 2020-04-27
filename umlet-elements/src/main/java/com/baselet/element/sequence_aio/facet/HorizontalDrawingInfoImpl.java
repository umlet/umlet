package com.baselet.element.sequence_aio.facet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.baselet.element.sequence_aio.facet.LifelineSpanningTickSpanningOccurrence.ContainerPadding;
import com.baselet.element.sequence_aio.facet.SequenceDiagram.DoubleConverter;

public class HorizontalDrawingInfoImpl implements HorizontalDrawingInfo {

	private final double diagramStart;
	private final double diagramWidth;
	private final LifelineHorizontalDrawingInfo[] horizontalDrawingInfos;
	private final Map<Container, Double> containerLeftPadding;
	private final Map<Container, Double> containerRightPadding;

	/**
	 * Calculates the actual lifeline and diagram width.
	 * @param diagramStart left most point were the diagram can draw
	 * @param diagramMinWidth
	 * @param widthConverter used to adjust the calculated width (should only increase the width!)
	 * @param lifelineWidth width of a single lifeline without any paddings
	 * @param lifelineXPadding padding between two lifelines or a lifeline and the diagram border
	 * @param lifelineCount
	 * @param lastTick
	 * @param paddings
	 */
	public HorizontalDrawingInfoImpl(double diagramStart, double diagramMinWidth, DoubleConverter widthConverter,
			double lifelineWidth, double lifelineXPadding, int lifelineCount, int lastTick, Collection<ContainerPadding> paddings) {
		containerLeftPadding = new HashMap<Container, Double>((int) (paddings.size() / 0.7));
		containerRightPadding = new HashMap<Container, Double>((int) (paddings.size() / 0.7));
		horizontalDrawingInfos = new LifelineHorizontalDrawingInfo[lifelineCount];
		this.diagramStart = diagramStart;
		// calculate the padding information and the maximum padding
		double maxPadding = 0;
		double[][] leftPaddings = new double[lifelineCount][];
		double[][] rightPaddings = new double[lifelineCount][];
		for (int i = 0; i < lifelineCount; i++) {
			PaddingInfo paddingInfo = getPaddings(i, true, lastTick, paddings);
			leftPaddings[i] = paddingInfo.paddings;
			maxPadding = Math.max(maxPadding, paddingInfo.maxPadding);
			paddingInfo = getPaddings(i, false, lastTick, paddings);
			rightPaddings[i] = paddingInfo.paddings;
			maxPadding = Math.max(maxPadding, paddingInfo.maxPadding);
		}
		lifelineWidth += maxPadding * 2; // add the padding because it is not included in the lifeline width

		double diagramWidth = lifelineWidth * lifelineCount + lifelineXPadding * (lifelineCount + 1);
		if (diagramWidth < diagramMinWidth) {
			diagramWidth = diagramMinWidth;
			if (lifelineCount > 0) {
				lifelineWidth = (diagramWidth - lifelineXPadding * (lifelineCount + 1)) / lifelineCount;
			}
		}
		// adjust the width with the width converter
		diagramWidth = widthConverter.convert(diagramWidth);

		double lifelineHeadLeftStart = (diagramWidth
				- (lifelineWidth * lifelineCount + lifelineXPadding * (lifelineCount - 1))
				) / 2.0;

		this.diagramWidth = diagramWidth;
		for (int i = 0; i < horizontalDrawingInfos.length; i++) {
			horizontalDrawingInfos[i] = new LifelineHorizontalDrawingInfoImpl(
					leftPaddings[i],
					rightPaddings[i],
					lifelineHeadLeftStart + (lifelineWidth + lifelineXPadding) * i,
					lifelineHeadLeftStart + (lifelineWidth + lifelineXPadding) * i + lifelineWidth);
		}
	}

	private PaddingInfo getPaddings(int lifelineId, boolean left, int lastTick, Collection<ContainerPadding> allPaddings) {
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
		double maxPadding = 0;
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
			maxPadding = Math.max(maxPadding, lifelinePaddings[tick]);
		}
		return new PaddingInfo(lifelinePaddings, maxPadding);
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

	@Override
	public double getDiagramHorizontalStart() {
		return diagramStart;
	}

	@Override
	public double getDiagramHorizontalEnd() {
		return diagramStart + diagramWidth;
	}

	@Override
	public double getDiagramWidth() {
		return diagramWidth;
	}

	private static class PaddingInfo {
		private final double[] paddings;
		private final double maxPadding;

		public PaddingInfo(double[] paddings, double maxPadding) {
			super();
			this.paddings = paddings;
			this.maxPadding = maxPadding;
		}
	}
}
