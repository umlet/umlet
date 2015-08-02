package com.baselet.element.facet.specific.sequence_aio;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.specific.sequence_aio.LifelineSpanningTickSpanningOccurrence.ContainerPadding;

public class SequenceDiagram {

	private static final double LIFELINE_X_PADDING = 40;
	private static final double LIFELINE_Y_PADDING = 20;
	private static final double LIFELINE_MIN_WIDTH = 100;

	private static final double TICK_HEIGHT = 40;
	private static final double TICK_Y_PADDING = 6;// should be odd and the nothing should draw at the center!

	private String[] titleLines;
	private String[] descLines;

	// options
	private Lifeline[] lifelines;

	private final Collection<LifelineSpanningTickSpanningOccurrence> spanningLifelineOccurrences;

	private int lastTick;

	public SequenceDiagram()
	{
		titleLines = new String[] { "" };
		descLines = null;
		lifelines = new Lifeline[0];
		spanningLifelineOccurrences = new LinkedList<LifelineSpanningTickSpanningOccurrence>();
	}

	/**
	 * @param title lines which are separated by a \n
	 */
	public void setTitle(String title) {
		titleLines = title.split("\n");
	}

	/**
	 * @param titleLines an array of lines which build the title (each element must not contain a \n)
	 */
	public void setTitle(String[] titleLines) {
		this.titleLines = titleLines;
	}

	/**
	 * @param text description lines which are separated by a \n
	 */
	public void setText(String text) {
		descLines = text.split("\n");
	}

	/**
	 * @param textLines an array of lines which build the description (each element must not contain a \n)
	 */
	public void setText(String[] textLines) {
		descLines = textLines;
	}

	public int getLastTick() {
		return lastTick;
	}

	public void setLastTick(int lastTick) {
		this.lastTick = lastTick;
	}

	/**
	 *
	 * @param headText
	 * @param headType
	 * @param createdOnStart if false the lifeline will be created by the first message sent to this lifeline
	 */
	public Lifeline addLiveline(String headText, Lifeline.LifelineHeadType headType, boolean createdOnStart) {
		lifelines = Arrays.copyOf(lifelines, lifelines.length + 1);
		lifelines[lifelines.length - 1] = new Lifeline(headText, lifelines.length - 1, headType, createdOnStart);
		return lifelines[lifelines.length - 1];
	}

	public void addLifelineSpanningTickSpanningOccurrence(LifelineSpanningTickSpanningOccurrence occurrence) {
		spanningLifelineOccurrences.add(occurrence);
	}

	/**
	 *
	 * @return how many lifelines the diagram has
	 */
	public int getLifelineCount() {
		return lifelines.length;
	}

	public List<Lifeline> getLifelines() {
		return Arrays.asList(lifelines);
	}

	public Lifeline[] getLifelinesArray() {
		return lifelines;
	}

	public void draw(DrawHandler drawHandler) {
		LifelineHorizontalDrawingInfo[] llHorizontalDrawingInfos;
		HorizontalDrawingInfo horizontalDrawingInfo;
		VerticalDrawingInfo verticalInfo;
		DrawingInfo drawingInfo;
		// calculate the minimum width of the lifelines and the diagram, if the header increases the diagram width adjust the lifeline width
		double lifelineWidth = Math.max(getLifelineWidth(drawHandler), LIFELINE_MIN_WIDTH);
		double diagramWidth = lifelineWidth * getLifelineCount() + LIFELINE_X_PADDING * (getLifelineCount() + 1);
		if (diagramWidth < LIFELINE_MIN_WIDTH) {
			diagramWidth = LIFELINE_MIN_WIDTH;
		}
		if (diagramWidth < PentagonDrawingHelper.getMinimumWidth(drawHandler, titleLines)) {
			diagramWidth = PentagonDrawingHelper.getMinimumWidth(drawHandler, titleLines);
			if (getLifelineCount() > 0) {
				lifelineWidth = (diagramWidth - LIFELINE_X_PADDING * (getLifelineCount() + 1)) / getLifelineCount();
			}
		}
		// calculate and draw the header, then draw top border
		double headerHeight = PentagonDrawingHelper.draw(drawHandler, titleLines, diagramWidth, new PointDouble(0, 0)).y;
		drawHandler.drawLine(0, 0, diagramWidth, 0);

		// draw description
		// TODO
		double lifelineHeadTop = headerHeight + LIFELINE_Y_PADDING;

		double lifelineHeadLeftStart = LIFELINE_X_PADDING;
		// llHorizontalDrawingInfos = new LifelineHorizontalDrawingInfo[lifelines.length];
		// for (int i = 0; i < llHorizontalDrawingInfos.length; i++) {
		// llHorizontalDrawingInfos[i] = new LifelineHorizontalDrawingInfoImpl(
		// getPaddings(lifelines[i], true),
		// getPaddings(lifelines[i], false),
		// lifelineHeadLeftStart + (lifelineWidth + LIFELINE_X_PADDING) * i,
		// lifelineHeadLeftStart + (lifelineWidth + LIFELINE_X_PADDING) * i + lifelineWidth);
		// }
		// horizontalDrawingInfo = new HorizontalDrawingInfoImpl(llHorizontalDrawingInfos);
		Collection<ContainerPadding> allPaddings = new LinkedList<ContainerPadding>();
		for (LifelineSpanningTickSpanningOccurrence lstso : spanningLifelineOccurrences) {
			if (lstso.getPaddingInformation() != null) {
				allPaddings.add(lstso.getPaddingInformation());
			}
		}
		horizontalDrawingInfo = new HorizontalDrawingInfoImpl(lifelineHeadLeftStart, lifelineWidth, LIFELINE_X_PADDING,
				lifelines.length, lastTick, allPaddings);

		double lifelineHeadHeight = getLifelineHeadHeight(drawHandler, horizontalDrawingInfo);
		verticalInfo = new VerticalDrawingInfoImpl(lifelineHeadTop, lifelineHeadHeight, TICK_HEIGHT, TICK_Y_PADDING,
				calculateAddiontalHeights(drawHandler, horizontalDrawingInfo), allPaddings);
		drawingInfo = new DrawingInfoImpl(horizontalDrawingInfo, verticalInfo, getLifelineCount());
		// first draw the occurrences which affect more than one lifeline which stores the interrupted areas in the
		// corresponding LifelineDrawingInfo. This info is then passed to the lifeline so it can be drawn
		if (lifelines.length > 0) {
			for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
				llstso.draw(drawHandler, drawingInfo);
			}
			for (Lifeline ll : lifelines) {
				ll.draw(drawHandler, drawingInfo.getDrawingInfo(ll), lastTick);
			}
		}
		// draw left,right and bottom border
		double bottomY = verticalInfo.getVerticalEnd(lastTick) + LIFELINE_Y_PADDING;// TODO check were lifeline really ends
		drawHandler.drawLine(0, bottomY, diagramWidth, bottomY);
		drawHandler.drawLine(0, 0, 0, bottomY);
		drawHandler.drawLine(diagramWidth, 0, diagramWidth, bottomY);
	}

	// private double[] getPaddings(Lifeline lifeline, boolean left) {
	// double[] paddings = new double[lastTick + 2];
	// // define 1 queue for starting of padding intervals, and the other for the ending of the intervals
	// Queue<PaddingInterval> paddingQueueStart = new PriorityQueue<PaddingInterval>(5,
	// PaddingInterval.getStartAscComparator());
	// List<PaddingInterval> paddingListEnd = new LinkedList<PaddingInterval>();
	//
	// for (LifelineSpanningTickSpanningOccurrence lstso : spanningLifelineOccurrences) {
	// if (left && lstso.getFirstLifeline() == lifeline && lstso.getLeftPadding() != null) {
	// paddingQueueStart.add(lstso.getLeftPadding());
	// }
	// else if (!left && lstso.getLastLifeline() == lifeline && lstso.getRightPadding() != null) {
	// paddingQueueStart.add(lstso.getRightPadding());
	// }
	// }
	// for (int tick = 0; tick < paddings.length; tick++) {
	// // insert paddings that start at the current tick at the right place (endTick asc) and remove them.
	// while (paddingQueueStart.peek() != null && paddingQueueStart.peek().getStartTick() == tick) {
	// int index = Collections.binarySearch(paddingListEnd, paddingQueueStart.peek(),
	// PaddingInterval.getEndAscComparator());
	// if (index >= 0) {
	// paddingListEnd.add(index, paddingQueueStart.poll());
	// }
	// else {
	// paddingListEnd.add(-index - 1, paddingQueueStart.poll());
	// }
	// }
	// Iterator<PaddingInterval> endIter = paddingListEnd.iterator();
	// while (endIter.hasNext()) {
	// PaddingInterval paddingInterval = endIter.next();
	// paddings[tick] += paddingInterval.getPadding();
	// if (paddingInterval.getEndTick() == tick) {
	// endIter.remove();
	// }
	// }
	// }
	// return paddings;
	// }

	private double getLifelineWidth(DrawHandler drawHandler) {
		double maxMinWidth = 0;
		for (Lifeline ll : lifelines) {
			maxMinWidth = Math.max(maxMinWidth, ll.getMinWidth(drawHandler));
		}
		for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
			int llCount = llstso.getLastLifeline().getIndex() - llstso.getFirstLifeline().getIndex() + 1;
			maxMinWidth = Math.max(maxMinWidth,
					(llstso.getOverallMinWidth(drawHandler, LIFELINE_X_PADDING) - LIFELINE_X_PADDING * (llCount - 1)) / llCount);
		}
		return maxMinWidth;
	}

	private double[] calculateAddiontalHeights(DrawHandler drawHandler, HorizontalDrawingInfo hDrawingInfo) {
		double[] addiontalHeight = new double[lastTick + 1];
		for (Lifeline ll : lifelines) {
			for (Map.Entry<Integer, Double> e : ll.getAdditionalYHeights(drawHandler, hDrawingInfo.getHDrawingInfo(ll),
					TICK_HEIGHT).entrySet()) {
				addiontalHeight[e.getKey()] = Math.max(addiontalHeight[e.getKey()], e.getValue());
			}
		}
		for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
			for (Map.Entry<Integer, Double> e : llstso.getEveryAdditionalYHeight(drawHandler, hDrawingInfo,
					TICK_HEIGHT).entrySet()) {
				addiontalHeight[e.getKey()] = Math.max(addiontalHeight[e.getKey()], e.getValue());
			}
		}
		return addiontalHeight;
	}

	private double getLifelineHeadHeight(DrawHandler drawHandler, HorizontalDrawingInfo hDrawingInfo) {
		double maxHeight = 0;
		for (Lifeline ll : lifelines) {
			if (ll.isCreatedOnStart()) {
				maxHeight = Math.max(maxHeight, ll.getHeadMinHeight(drawHandler, hDrawingInfo.getHDrawingInfo(ll).getWidth()));
			}
		}
		return maxHeight;
	}

}
