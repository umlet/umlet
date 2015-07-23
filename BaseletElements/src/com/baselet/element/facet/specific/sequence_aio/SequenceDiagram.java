package com.baselet.element.facet.specific.sequence_aio;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.SortedMergedLine1DList;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;

public class SequenceDiagram {
	private static final double LIFELINE_X_PADDING = 40;
	private static final double LIFELINE_MIN_WIDTH = 100;
	private static final double TICK_HEIGHT = 40;

	private String title;
	private String text;

	// options
	/** if true no default ids are generated and every lifeline needs an id */
	private boolean overrideDefaultIds;

	private final Map<String, Integer> ids;
	private Lifeline[] lifelines;

	private final Collection<LifelineSpanningTickSpanningOccurrence> spanningLifelineOccurrences;

	private int lastTick;

	public SequenceDiagram()
	{
		overrideDefaultIds = false;

		ids = new HashMap<String, Integer>();
		lifelines = new Lifeline[0];
		spanningLifelineOccurrences = new LinkedList<LifelineSpanningTickSpanningOccurrence>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLastTick() {
		return lastTick;
	}

	public void setLastTick(int lastTick) {
		this.lastTick = lastTick;
	}

	/**
	 *
	 * @param name
	 * @param id can be NULL, if overrideDefaultIds option is NOT active
	 * @param createdOnStart if false the lifeline will be created by the first message sent to this lifeline
	 */
	public Lifeline addLiveline(String headText, String id, Lifeline.LifelineHeadType headType, boolean createdOnStart) {
		lifelines = Arrays.copyOf(lifelines, lifelines.length + 1);
		lifelines[lifelines.length - 1] = new Lifeline(headText, lifelines.length - 1, headType, createdOnStart);
		if (id != null) {
			ids.put(id, lifelines.length - 1);
		}
		if (!overrideDefaultIds) {
			ids.put("id" + lifelines.length, lifelines.length - 1);
		}
		return lifelines[lifelines.length - 1];
	}

	/**
	 * Should only be used for testing!
	 * @param id of the lifeline
	 * @param event e.g. message, invariant, activity
	 */
	void addEvent(String id, int tick, LifelineOccurrence occurrence) {
		getLifelineException(id).addLifelineOccurrenceAtTick(occurrence, tick);
	}

	/**
	 * should only be used for testing and not for the parser
	 * @param fromId
	 * @param toId
	 * @param sendTick
	 * @param duration
	 * @param text
	 * @param lineType
	 * @param arrowType
	 */
	void addMessage(String fromId, String toId, int sendTick, int duration, String text, LineType lineType, Message.ArrowType arrowType) {
		Message msg = new Message(getLifelineException(fromId), getLifelineException(toId), duration, sendTick, text, arrowType, lineType);
		addLifelineSpanningTickSpanningOccurrence(msg);
	}

	public void addLifelineSpanningTickSpanningOccurrence(LifelineSpanningTickSpanningOccurrence occurrence) {
		spanningLifelineOccurrences.add(occurrence);
	}

	/**
	 * @param id of the lifeline which should be returned
	 * @return the lifeline or null if no lifeline is associated with the given id
	 */
	public Lifeline getLifeline(String id) {
		if (!ids.containsKey(id)) {
			return null;
		}
		return lifelines[ids.get(id)];
	}

	/**
	 * @param id of the lifeline which should be returned
	 * @return the lifeline
	 * @throws SequenceDiagramException if no lifeline is associated with the given id
	 */
	Lifeline getLifelineException(String id) {
		if (!ids.containsKey(id)) {
			throw new SequenceDiagramException("No lifeline is associated with the id: '" + id + "'");
		}
		return lifelines[ids.get(id)];
	}

	/**
	 *
	 * @return how many lifelines the diagram has
	 */
	public int getLifelineCount() {
		// TODO maybe need adaption for lost found gate!
		return lifelines.length;
	}

	public boolean isOverrideDefaultIds() {
		return overrideDefaultIds;
	}

	public void setOverrideDefaultIds(boolean overrideDefaultIds) {
		this.overrideDefaultIds = overrideDefaultIds;
	}

	// TODO perform action that couldn't be done while building
	// public void completeDiagram()
	// {
	// TODO for each operand condition add a LL occurence to the lifeline with the first message (or to the lowest index)
	// }

	/**
	 * should be called after the last built step in the diagram to make a sanity check (i.e. message to all Lifelines which are created later, but could be problematic while writting the diagram!)
	 * @return
	 */
	// public boolean checkIfDiagramIsComplete() {
	// return false;
	// }

	public void draw(DrawHandler drawHandler) {
		// draw top border and Text
		// draw description

		double lifelineHeadTop = 20;
		double lifelineHeadLeftStart = 10;

		// drawing of the lifelines
		if (lifelines.length > 0) {
			double lifelineWidth = Math.max(getLifelineWidth(drawHandler), LIFELINE_MIN_WIDTH);
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

			for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
				Map<Lifeline, Line1D[]> tmpInterruptedAreas = llstso.draw(drawHandler, lifelineHeadTop + lifelineHeadHeight,
						lifelineHorizontalSpannings, TICK_HEIGHT, accumulativeAddiontalHeightOffsets);
				for (Map.Entry<Lifeline, Line1D[]> e : tmpInterruptedAreas.entrySet()) {
					interruptedAreas[e.getKey().getIndex()].addAll(e.getValue());
				}
			}

			for (int i = 0; i < lifelines.length; i++) {
				lifelines[i].draw(drawHandler,
						new PointDouble(lifelineHorizontalSpannings[i].getLow(), lifelineHeadTop),
						lifelineWidth, lifelineHeadHeight, TICK_HEIGHT, accumulativeAddiontalHeightOffsets, lastTick, interruptedAreas[i]);
			}
		}
		// draw left,right and bottom border
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
