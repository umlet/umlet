package com.baselet.element.facet.specific.sequence_aio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;

public class SequenceDiagram {
	private static final double LIFELINE_X_PADDING = 40;
	private static final double TICK_HEIGHT = 40;

	private String title;
	private String text;

	// options
	/** if true no default ids are generated and every lifeline needs an id */
	private final boolean overrideDefaultIds;

	private final Map<String, Integer> ids;
	private Lifeline[] lifelines;

	// private final Lifeline lost;
	// private Lifeline found;
	// private Lifeline gate;

	private int lastTick;

	public SequenceDiagram()
	{
		overrideDefaultIds = false;

		// lost = ;
		// found = ;
		// gate = ;
		ids = new HashMap<String, Integer>();
		// ids.put("lost", value)
		lifelines = new Lifeline[0];
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
	 * @param id can be NULL, if none of reorder and idoverride option is active
	 * @param createOnStart if false the lifeline will be created by the first message sent to this lifeline
	 */
	public void addLiveline(String headText, String id, Lifeline.LifelineHeadType headType, boolean createOnStart) {
		lifelines = Arrays.copyOf(lifelines, lifelines.length + 1);
		lifelines[lifelines.length - 1] = new Lifeline(headText, lifelines.length - 1, headType, createOnStart);
		if (id != null) {
			ids.put(id, lifelines.length - 1);
		}
		if (!overrideDefaultIds) { // ! override default ids
			ids.put("id" + lifelines.length, lifelines.length - 1);
		}
		else if (id == null) {
			throw new RuntimeException("If the override option is set to true then every lifeline needs an id!");
		}

	}

	/**
	 *
	 * @param id of the lifeline
	 * @param event e.g. message, invariant, activity
	 */
	public void addEvent(String id, LifelineOccurrence occurrence) {

	}

	/**
	 *
	 * @param id of the lifeline
	 * @param event e.g. message, invariant, activity
	 */
	public void addEvent(String id, int tick, LifelineOccurrence occurrence) {
		lifelines[ids.get(id)].addLifelineOccurrenceAtTick(occurrence, tick);
	}

	/**
	 * advances a step down
	 */
	public void tick() {

	}

	public Lifeline getLifeline(String id) {
		return lifelines[ids.get(id)];
	}

	/**
	 * should be called after the last built step in the diagram to make a sanity check (i.e. message to all Lifelines which are created later, but could be problematic while writting the diagram!)
	 * @return
	 */
	public boolean checkIfDiagramIsComplete() {
		return false;
	}

	public void draw(DrawHandler drawHandler) {
		// draw top border and Text
		// draw description

		double lifelineHeadTop = 20;
		double lifelineHeadLeftStart = 10;

		// drawing of the lifelines
		if (lifelines.length > 0) {
			double lifelineWidth = getLifelineWidth(drawHandler);
			double lifelineHeadHeight = getLifelineHeadHeight(drawHandler, lifelineWidth);
			double[] addiontalHeight = calculateAddiontalHeights(drawHandler, lifelineWidth);
			double[] accumulativeAddiontalHeightOffsets = new double[addiontalHeight.length + 1];
			double sum = 0;
			for (int i = 0; i < addiontalHeight.length; i++) {
				sum += addiontalHeight[i];
				accumulativeAddiontalHeightOffsets[i + 1] = sum;
			}
			for (int i = 0; i < lifelines.length; i++) {
				lifelines[i].draw(drawHandler,
						new PointDouble(lifelineHeadLeftStart + (lifelineWidth + LIFELINE_X_PADDING) * i, lifelineHeadTop),
						lifelineWidth, lifelineHeadHeight, TICK_HEIGHT, accumulativeAddiontalHeightOffsets, lastTick);
			}
		}
		// draw bottom border
	}

	private double getLifelineWidth(DrawHandler drawHandler) {
		double maxMinWidth = 0;
		for (Lifeline ll : lifelines) {
			maxMinWidth = Math.max(maxMinWidth, ll.getMinWidth(drawHandler));
		}
		return maxMinWidth;
	}

	private double[] calculateAddiontalHeights(DrawHandler drawHandler, double width) {
		double[] addiontalHeight = new double[lastTick + 1];
		for (Lifeline ll : lifelines) {
			for (Map.Entry<Integer, Double> e : ll.getAdditionalYHeights(drawHandler, width, TICK_HEIGHT).entrySet()) {
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
