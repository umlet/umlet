package com.baselet.element.facet.specific.sequence_aio;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.SortedMergedLine1DList;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;

public class SequenceDiagram {

	private static final double LIFELINE_X_PADDING = 40;
	private static final double LIFELINE_Y_PADDING = 20;
	private static final double LIFELINE_MIN_WIDTH = 100;

	private static final double TICK_HEIGHT = 40;

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
		// TODO maybe need adaption for lost found gate!
		return lifelines.length;
	}

	public List<Lifeline> getLifelines() {
		return Arrays.asList(lifelines);
	}

	public Lifeline[] getLifelinesArray() {
		return lifelines;
	}

	public void draw(DrawHandler drawHandler) {
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

		Line1D[] lifelineHorizontalSpannings = new Line1D[lifelines.length];
		for (int i = 0; i < lifelineHorizontalSpannings.length; i++) {
			lifelineHorizontalSpannings[i] = new Line1D(lifelineHeadLeftStart + (lifelineWidth + LIFELINE_X_PADDING) * i,
					lifelineHeadLeftStart + (lifelineWidth + LIFELINE_X_PADDING) * i + lifelineWidth);
		}
		double lifelineHeadHeight = getLifelineHeadHeight(drawHandler, lifelineWidth);
		double[] addiontalHeight = calculateAddiontalHeights(drawHandler, lifelineWidth, lifelineHorizontalSpannings);
		double[] accumulativeAddiontalHeightOffsets = new double[addiontalHeight.length + 1];
		double sum = 0;
		for (int i = 0; i < addiontalHeight.length; i++) {
			sum += addiontalHeight[i];
			accumulativeAddiontalHeightOffsets[i + 1] = sum;
		}

		// first draw the occurrences which affect more than one lifeline, store the interrupted areas an pass them to the lifelines
		SortedMergedLine1DList[] interruptedAreas = new SortedMergedLine1DList[lifelines.length];
		for (int i = 0; i < interruptedAreas.length; i++) {
			interruptedAreas[i] = new SortedMergedLine1DList();
		}

		// drawing of the lifelines
		if (lifelines.length > 0) {
			for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
				Map<Integer, Line1D[]> tmpInterruptedAreas = llstso.draw(drawHandler, lifelineHeadTop + lifelineHeadHeight,
						lifelineHorizontalSpannings, TICK_HEIGHT, accumulativeAddiontalHeightOffsets);
				for (Map.Entry<Integer, Line1D[]> e : tmpInterruptedAreas.entrySet()) {
					interruptedAreas[e.getKey()].addAll(e.getValue());
				}
			}

			for (int i = 0; i < lifelines.length; i++) {
				lifelines[i].draw(drawHandler,
						new PointDouble(lifelineHorizontalSpannings[i].getLow(), lifelineHeadTop),
						lifelineWidth, lifelineHeadHeight, TICK_HEIGHT, accumulativeAddiontalHeightOffsets, lastTick, interruptedAreas[i]);
			}
		}
		// draw left,right and bottom border
		double bottomY = lifelineHeadTop + lifelineHeadHeight + (lastTick + 1) * TICK_HEIGHT + accumulativeAddiontalHeightOffsets[lastTick + 1] + LIFELINE_Y_PADDING;
		drawHandler.drawLine(0, bottomY, diagramWidth, bottomY);
		drawHandler.drawLine(0, 0, 0, bottomY);
		drawHandler.drawLine(diagramWidth, 0, diagramWidth, bottomY);
	}

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

	private double[] calculateAddiontalHeights(DrawHandler drawHandler, double width, Line1D[] lifelineHorizontalSpannings) {
		double[] addiontalHeight = new double[lastTick + 1];
		for (Lifeline ll : lifelines) {
			for (Map.Entry<Integer, Double> e : ll.getAdditionalYHeights(drawHandler, width, TICK_HEIGHT).entrySet()) {
				addiontalHeight[e.getKey()] = Math.max(addiontalHeight[e.getKey()], e.getValue());
			}
		}
		for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
			for (Map.Entry<Integer, Double> e : llstso.getEveryAdditionalYHeight(drawHandler, lifelineHorizontalSpannings, TICK_HEIGHT).entrySet()) {
				addiontalHeight[e.getKey()] = Math.max(addiontalHeight[e.getKey()], e.getValue());
			}
		}
		return addiontalHeight;
	}

	private double getLifelineHeadHeight(DrawHandler drawHandler, double width) {
		double maxHeight = 0;
		for (Lifeline ll : lifelines) {
			if (ll.isCreatedOnStart()) {
				maxHeight = Math.max(maxHeight, ll.getHeadMinHeight(drawHandler, width));
			}
		}
		return maxHeight;
	}

}
