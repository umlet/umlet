package com.baselet.element.facet.specific.sequence_aio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.draw.DrawHelper;

public class Lifeline {
	private static final double ACTOR_DIMENSION = 10;
	private static final PointDouble ACTOR_SIZE = new PointDouble(DrawHelper.armLength(ACTOR_DIMENSION) * 2, DrawHelper.headToLegLength(ACTOR_DIMENSION));
	private static final double ACTIVE_CLASS_DOUBLE_BORDER_GAP = 10;
	private static final double HEAD_VERTICAL_BORDER_PADDING = 5;
	private static final double HEAD_HORIZONTAL_BORDER_PADDING = 5;
	private static final double EXECUTIONSPECIFICATION_WIDTH = 20;
	private static final double EXECUTIONSPECIFICATION_OVERLAPP = 8;

	private final String text;
	// private String id;
	// private String defaultId;
	/** position in the array = numbered from left to right starting at 0 */
	private final int index;
	private final LifelineHeadType headType;
	/** If false it will be created by the first message sent to this object */
	private final boolean createdOnStart;
	/** The tick time when the lifeline is created or null if it exists from the beginning */
	private Integer created;
	/** The tick time when the lifeline is destroyed (X symbol) or null if it exists till the end (no X symbol at the end) */
	private Integer destroyed;

	private final LinkedHashMap<Integer, LifelineOccurrence> lifeline;

	/** order according to the start tick */
	private final ExecutionSpecification[] activeAreas;

	/**
	 * each PointDouble represents an area (y1=x,y2=y) in which the lifeline e is interrupted (e.g. InteractionConstraint)
	 * this is only used for drawing purposes
	 */
	private final LinkedList<PointDouble> interruptedAreas;

	/** ids on this lifeline e.g. ids for receiving and sending messages, value is the tick */
	private final Map<String, Integer> localIds;

	// /** Starting point of the lifeline, the head sits directly above it*/
	// private PointDouble lifelineTopCenter;
	// private PointDouble headSize;

	public Lifeline(String text, int index, LifelineHeadType headType, boolean createdOnStart) {
		super();
		this.text = text;
		this.index = index;
		this.headType = headType;
		this.createdOnStart = createdOnStart;
		created = null;
		destroyed = null;

		lifeline = new LinkedHashMap<Integer, LifelineOccurrence>();
		// TODO change to collection?
		activeAreas = new ExecutionSpecification[0];
		interruptedAreas = new LinkedList<PointDouble>();
		localIds = new HashMap<String, Integer>();
	}

	public void setCreated(Integer created) {
		this.created = created;
	}

	public void setDestroyed(Integer destroyed) {
		this.destroyed = destroyed;
	}

	public void addLifelineOccurrenceAtTick(LifelineOccurrence occurrence, Integer tick) {
		if (lifeline.containsKey(tick)) {
			throw new RuntimeException("The lifeline already contains an Occurence at the tick " + tick);
		}
		lifeline.put(tick, occurrence);
	}

	/**
	 * @param tick the time at which the width should be calculated
	 * @return 0 if it is only a line, otherwise return the width from the center to the left outermost border of the execution specification
	 * @see Coregion for a drawing
	 */
	public double getLifelineLeftPartWidth(int tick)
	{
		return getCurrentlyActiveExecutionSpecifications(tick) > 0 ? EXECUTIONSPECIFICATION_WIDTH / 2.0 : 0;
	}

	/**
	 * @param tick the time at which the width should be calculated
	 * @return 0 if it is only a line, otherwise return the width from the center to the right outermost border of the execution specification
	 * @see Coregion for a drawing
	 */
	public double getLifelineRightPartWidth(int tick)
	{
		int currentlyActiveExecSpec = getCurrentlyActiveExecutionSpecifications(tick);
		if (currentlyActiveExecSpec == 0) {
			return 0;
		}
		else {
			return (currentlyActiveExecSpec - 1) * (EXECUTIONSPECIFICATION_WIDTH - EXECUTIONSPECIFICATION_OVERLAPP) + EXECUTIONSPECIFICATION_WIDTH / 2.0;
		}
	}

	/**
	 * @param drawHandler
	 * @return the minimum width which is needed by this lifeline
	 */
	public double getMinWidth(DrawHandler drawHandler) {
		double minWidth = 0;
		for (LifelineOccurrence llOccurrence : lifeline.values()) {
			minWidth = Math.max(minWidth, llOccurrence.getMinWidth(drawHandler));
		}
		if (activeAreas.length == 1) {
			minWidth = Math.max(minWidth, EXECUTIONSPECIFICATION_WIDTH);
		}
		else if (activeAreas.length > 1) {
			int maxSimultaneousExecSpec = 0;
			// for each start of an area calculate the currently active ExecutionSpecifications and set the maximum
			for (ExecutionSpecification activeArea : activeAreas) {
				maxSimultaneousExecSpec = Math.max(maxSimultaneousExecSpec, getCurrentlyActiveExecutionSpecifications(activeArea.getStartTick()));
			}
			minWidth = Math.max(minWidth, (maxSimultaneousExecSpec - 1) * (EXECUTIONSPECIFICATION_WIDTH - EXECUTIONSPECIFICATION_OVERLAPP) + EXECUTIONSPECIFICATION_WIDTH);
		}
		minWidth = Math.max(minWidth, getHeadMinWidth(drawHandler));
		return minWidth;
	}

	private int getCurrentlyActiveExecutionSpecifications(int tick) {
		int currentlyActiveExecSpec = 0;
		for (int i = 0; i < activeAreas.length && tick >= activeAreas[i].getStartTick(); i++) {
			if (activeAreas[i].enclosesTick(tick)) {
				currentlyActiveExecSpec++;
			}
		}
		return currentlyActiveExecSpec;
	}

	/**
	 * calculates the
	 * @param drawHandler
	 * @return a Map which stores the ticks as keys and the additional height as values (i.e. the height which exceeds the tickHeight)
	 */
	public Map<Integer, Double> getAdditionalYHeights(DrawHandler drawHandler, double width, double tickHeight)
	{
		PointDouble size = new PointDouble(width, tickHeight);
		double additionalY;
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		for (Map.Entry<Integer, LifelineOccurrence> e : lifeline.entrySet()) {
			additionalY = e.getValue().getAdditionalYHeight(drawHandler, size);
			if (additionalY > 0) {
				ret.put(e.getKey(), additionalY);
			}
		}
		// TODO add head size if it the obj is created with an message
		return ret;
	}

	private double getHeadMinWidth(DrawHandler drawHandler) {
		double minWidth = ACTOR_SIZE.x; // actor width is small, so use it as minimum width

		// TODO calculate text min width

		if (headType == LifelineHeadType.ACTIVE_CLASS) {
			minWidth = minWidth + ACTIVE_CLASS_DOUBLE_BORDER_GAP * 2;
		}
		return minWidth;
	}

	public double getHeadMinHeight(DrawHandler drawHandler, double width) {
		double minHeight = 50;
		// TODO
		return minHeight;
	}

	/**
	 *
	 * @param drawHandler
	 * @param topLeft the top left corner of the lifeline head
	 * @param width the maximum width of the lifeline
	 * @param headHeight the height of the heads which exists from the start, the head which are created by a message can have a different height!
	 * @param tickHeight
	 * @param addiontalHeights array which stores the extra space needed for every tick (0 values if no addional space is needed)
	 */
	public void draw(DrawHandler drawHandler, PointDouble topLeft, double width, double headHeight, double tickHeight, double[] accumulativeAddiontalHeightOffsets) {
		// draw Head with text
		if (createdOnStart) {
			drawHead(drawHandler, topLeft.x, topLeft.y, width, headHeight);
		}
		else {
			drawHead(drawHandler, topLeft.x,
					topLeft.y + headHeight + accumulativeAddiontalHeightOffsets[created] + created * tickHeight,
					width,
					tickHeight + accumulativeAddiontalHeightOffsets[created + 1] - accumulativeAddiontalHeightOffsets[created]);
		}

		// draw lifeline occurrences
		for (Map.Entry<Integer, LifelineOccurrence> e : lifeline.entrySet()) {
			int tick = e.getKey();
			PointDouble topLeftOccurence = new PointDouble(topLeft.x,
					topLeft.y + headHeight + accumulativeAddiontalHeightOffsets[tick] + tick * tickHeight);
			PointDouble sizeOccurence = new PointDouble(width,
					tickHeight + accumulativeAddiontalHeightOffsets[tick + 1] - accumulativeAddiontalHeightOffsets[tick]);
			e.getValue().draw(drawHandler, topLeftOccurence, sizeOccurence);
		}
	}

	private void drawHead(DrawHandler drawHandler, double x, double y, double width, double height) {
		if (headType == LifelineHeadType.STANDARD || headType == LifelineHeadType.ACTIVE_CLASS) {
			drawHandler.drawRectangle(x, y, width, height);
			if (headType == LifelineHeadType.ACTIVE_CLASS) {
				drawHandler.drawLine(x + ACTIVE_CLASS_DOUBLE_BORDER_GAP, y, x + ACTIVE_CLASS_DOUBLE_BORDER_GAP, y + height);
				drawHandler.drawLine(x + width - ACTIVE_CLASS_DOUBLE_BORDER_GAP, y, x + width - ACTIVE_CLASS_DOUBLE_BORDER_GAP, y + height);
				x += ACTIVE_CLASS_DOUBLE_BORDER_GAP;
				width -= ACTIVE_CLASS_DOUBLE_BORDER_GAP * 2;
			}
			x += HEAD_HORIZONTAL_BORDER_PADDING;
			width -= HEAD_HORIZONTAL_BORDER_PADDING * 2;
			y += HEAD_VERTICAL_BORDER_PADDING;
			height -= HEAD_VERTICAL_BORDER_PADDING * 2;
			// draw Text in x,y with width,height
		}
		else if (headType == LifelineHeadType.ACTOR) {
			DrawHelper.drawActor(drawHandler, (int) (x + width / 2.0), (int) y, ACTOR_DIMENSION);
			y += ACTOR_SIZE.y;
			height -= ACTOR_SIZE.y;
			// draw Text in x,y with width,height
		}
		else {
			assert false;
		}
	}

	public enum LifelineHeadType {
		STANDARD, ACTIVE_CLASS, ACTOR
	}

}
