package com.baselet.element.sequence_aio.facet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.baselet.element.sequence_aio.facet.LifelineSpanningTickSpanningOccurrence.ContainerPadding;

/**
 * Only supports ContainerPaddings which don't overlap at start, i.e. if the start tick is the same the lifeline
 * interval must not intersect.
 *
 */
public class VerticalDrawingInfoImpl implements VerticalDrawingInfo {

	private final double startingHeadTopY;
	private final double startingHeadHeight;
	private final double tickVerticalPadding;
	private final double defaultTickHeight;
	private final double[] accumulativeAddiontalHeightOffsets;
	private final double[] topPadding;
	private final double[] bottomPadding;
	/** contains the bottom padding of the enclosing containers which end at the same tick */
	private final Map<Container, Double> containerBottomPadding;
	private final double startingTickTopY;

	/**
	 * @param startingHeadTopY
	 * @param startingHeadHeight
	 * @param defaultTickHeight
	 * @param tickVerticalPadding <code>tickVerticalPadding = getVerticalEnd(tick)- getVerticalStart(tick+1)</code>
	 * @param addiontalHeights for every tick how much height should be added to this tick, therefore length at least
	 * lastTick + 1 so that lastTick is a valid index
	 * @param allPaddings
	 */
	public VerticalDrawingInfoImpl(double startingHeadTopY, double startingHeadHeight, double defaultTickHeight,
			double tickVerticalPadding, double[] addiontalHeights, Collection<ContainerPadding> allPaddings) {
		this.startingHeadTopY = startingHeadTopY;
		this.startingHeadHeight = startingHeadHeight;
		this.tickVerticalPadding = tickVerticalPadding;
		this.defaultTickHeight = defaultTickHeight;
		topPadding = new double[addiontalHeights.length + 1];
		bottomPadding = new double[addiontalHeights.length + 1];
		containerBottomPadding = new HashMap<Container, Double>((int) (allPaddings.size() / 0.7));
		accumulativeAddiontalHeightOffsets = new double[addiontalHeights.length + 1];
		processPaddings(addiontalHeights, allPaddings);
		startingTickTopY = startingHeadTopY + startingHeadHeight + tickVerticalPadding;
	}

	private void processPaddings(double[] addiontalHeights, Collection<ContainerPadding> allPaddings) {
		Map<Integer, List<ContainerPadding>> endMap = new HashMap<Integer, List<ContainerPadding>>();
		Map<Integer, List<ContainerPadding>> startMap = new HashMap<Integer, List<ContainerPadding>>();
		for (ContainerPadding cp : allPaddings) {
			if (!startMap.containsKey(cp.getContainer().getStartTick())) {
				startMap.put(cp.getContainer().getStartTick(), new LinkedList<ContainerPadding>());
			}
			startMap.get(cp.getContainer().getStartTick()).add(cp);
			if (!endMap.containsKey(cp.getContainer().getEndTick())) {
				endMap.put(cp.getContainer().getEndTick(), new LinkedList<ContainerPadding>());
			}
			endMap.get(cp.getContainer().getEndTick()).add(cp);
		}
		for (Map.Entry<Integer, List<ContainerPadding>> e : startMap.entrySet()) {
			for (ContainerPadding cp : e.getValue()) {
				topPadding[e.getKey()] = Math.max(topPadding[e.getKey()], cp.getTopPadding());
			}
		}
		calculateBottomPaddings(endMap);

		double sum = 0;
		for (int i = 0; i < addiontalHeights.length; i++) {
			sum += addiontalHeights[i] + topPadding[i] + bottomPadding[i];
			accumulativeAddiontalHeightOffsets[i + 1] = sum;
		}
	}

	private void calculateBottomPaddings(Map<Integer, List<ContainerPadding>> endMap) {
		for (Map.Entry<Integer, List<ContainerPadding>> e : endMap.entrySet()) {
			while (!e.getValue().isEmpty()) {
				List<ContainerPadding> cpList = new LinkedList<ContainerPadding>();
				cpList.add(e.getValue().remove(0));
				int startLl = cpList.get(0).getContainer().getFirstLifeline().getIndex();
				int endLl = cpList.get(0).getContainer().getLastLifeline().getIndex();
				// add all lifelines to the list which have intersecting lifelines
				ListIterator<ContainerPadding> cpIter = e.getValue().listIterator();
				while (cpIter.hasNext()) {
					ContainerPadding cp = cpIter.next();
					if (isIntersecting(startLl, endLl, cp.getContainer())) {
						cpList.add(cp);
						cpIter.remove();
						startLl = Math.min(startLl, cp.getContainer().getFirstLifeline().getIndex());
						endLl = Math.max(endLl, cp.getContainer().getLastLifeline().getIndex());
					}
				}
				Collections.sort(cpList, ContainerPadding.getContainerStartTickLifelineAscComparator());
				double padding = 0;
				for (ContainerPadding cp : cpList) {
					containerBottomPadding.put(cp.getContainer(), padding);
					padding += cp.getBottomPadding();
				}
				bottomPadding[e.getKey()] = Math.max(bottomPadding[e.getKey()], padding);
			}
		}

	}

	private boolean contains(int low, int high, int value) {
		return low <= value && value <= high;
	}

	private boolean isIntersecting(int startLl, int endLl, Container container) {
		boolean isIntersecting = contains(startLl, endLl, container.getFirstLifeline().getIndex());
		isIntersecting = isIntersecting || contains(startLl, endLl, container.getLastLifeline().getIndex());
		isIntersecting = isIntersecting || contains(container.getLastLifeline().getIndex(),
				container.getLastLifeline().getIndex(), startLl);
		return isIntersecting;
	}

	@Override
	public double getVerticalStart(int tick) {
		return startingTickTopY + tick * (defaultTickHeight + tickVerticalPadding) + accumulativeAddiontalHeightOffsets[tick]
				+ topPadding[tick];
	}

	@Override
	public double getVerticalEnd(int tick) {
		return startingTickTopY + tick * (defaultTickHeight + tickVerticalPadding) + defaultTickHeight
				+ accumulativeAddiontalHeightOffsets[tick + 1] - bottomPadding[tick];
	}

	@Override
	public double getTickHeight(int tick) {
		return getVerticalEnd(tick) - getVerticalStart(tick);
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

	@Override
	public double getVerticalStart(Container container) {
		// since the starting points aren't allowed to overlapp we only need to calculate the start without the topPadding
		int tick = container.getStartTick();
		return startingTickTopY + tick * (defaultTickHeight + tickVerticalPadding) + accumulativeAddiontalHeightOffsets[tick];
	}

	@Override
	public double getVerticalEnd(Container container) {
		// this is tricky because the end ticks can overlap
		int tick = container.getEndTick();
		return startingTickTopY + tick * (defaultTickHeight + tickVerticalPadding) + defaultTickHeight
				+ accumulativeAddiontalHeightOffsets[tick + 1] - containerBottomPadding.get(container);
	}

}
