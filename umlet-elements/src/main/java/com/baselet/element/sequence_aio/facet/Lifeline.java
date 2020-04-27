package com.baselet.element.sequence_aio.facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.draw.DrawHelper;

public class Lifeline {
	private static final double ACTOR_DIMENSION = 10;
	private static final PointDouble ACTOR_SIZE = new PointDouble(DrawHelper.armLength(ACTOR_DIMENSION) * 2,
			DrawHelper.headToLegLength(ACTOR_DIMENSION));
	private static final double ACTIVE_CLASS_DOUBLE_BORDER_GAP = 10;
	private static final double HEAD_VERTICAL_BORDER_PADDING = 5;
	private static final double HEAD_HORIZONTAL_BORDER_PADDING = 5;
	private static final double EXECUTIONSPECIFICATION_WIDTH = 20;
	private static final double EXECUTIONSPECIFICATION_OVERLAPP = 8;

	private static final double DESTROY_SIZE = 20; // width and height of the destroy symbol X

	private static final Logger log = LoggerFactory.getLogger(Lifeline.class);

	private final String[] text;
	/** position in the array = numbered from left to right starting at 0 */
	private final int index;
	private final LifelineHeadType headType;
	/** If false it will be created by the first message sent to this object */
	private boolean createdOnStart;
	/** The tick time when the lifeline is created can be null even when createdOnStart is false, then nothing is drawn*/
	private Integer created;
	/** The tick time when the lifeline is destroyed (X symbol) or null if it exists till the end (no X symbol at the end) */
	private Integer destroyed;
	private boolean execSpecFromStart;

	private final LinkedHashMap<Integer, LifelineOccurrence> lifeline;

	/** order according to the start tick */
	private final List<ExecutionSpecification> activeAreas;

	/**
	 *
	 * @param text lines need to be separated by \n
	 * @param index
	 * @param headType
	 * @param createdOnStart
	 * @param execSpecFromStart true if an execution specification should be active from the creation
	 */
	public Lifeline(String text, int index, LifelineHeadType headType, boolean createdOnStart, boolean execSpecFromStart) {
		super();
		this.text = text.split("\n");
		this.index = index;
		this.headType = headType;
		this.createdOnStart = createdOnStart;
		created = null;
		destroyed = null;
		this.execSpecFromStart = execSpecFromStart;

		lifeline = new LinkedHashMap<Integer, LifelineOccurrence>();
		activeAreas = new ArrayList<ExecutionSpecification>();
	}

	public int getIndex() {
		return index;
	}

	// public String[] getText() {
	// return text;
	// }

	public void setCreatedOnStart(boolean createdOnStart) {
		this.createdOnStart = createdOnStart;
	}

	public boolean isCreatedOnStart() {
		return createdOnStart;
	}

	public void setCreated(Integer created) {
		this.created = created;
	}

	public Integer getCreated() {
		return created;
	}

	public void setDestroyed(Integer destroyed) {
		this.destroyed = destroyed;
	}

	public Integer getDestroyed() {
		return destroyed;
	}

	public boolean isExecSpecFromStart() {
		return execSpecFromStart;
	}

	public void setExecSpecFromStart(boolean execSpecFromStart) {
		this.execSpecFromStart = execSpecFromStart;
	}

	/**
	 *
	 * @param occurrence
	 * @param tick
	 * @throws SequenceDiagramCheckedException if the lifeline already contains an occurrence at the specified tick or
	 * if the lifeline is not created on start and the specified tick is prior in time to the create tick
	 */
	public void addLifelineOccurrenceAtTick(LifelineOccurrence occurrence, Integer tick)
			throws SequenceDiagramCheckedException {
		if (!isCreatedOnStart()) {
			if (created == null || created >= tick) {
				throw new SequenceDiagramCheckedException("The lifeline can not contain occurrences before it is created.");
			}
		}
		if (lifeline.containsKey(tick)) {
			throw new SequenceDiagramCheckedException("The lifeline already contains an occurence at the tick " + tick);
		}
		lifeline.put(tick, occurrence);
	}

	public void addExecutionSpecification(ExecutionSpecification execSpec) {
		int i = 0;
		for (; i < activeAreas.size() && activeAreas.get(i).getStartTick() < execSpec.getStartTick(); i++) {}
		activeAreas.add(i, execSpec);
	}

	/**
	 * @param tick the time at which the width should be calculated
	 * @return 0 if it is only a line, otherwise return the width from
	 * the center to the left outermost border of the execution specification
	 * @see Coregion Coregion for an example how this is used for drawing
	 */
	public double getLifelineLeftPartWidth(int tick) {
		return getCurrentlyActiveExecutionSpecifications(tick) > 0 ? EXECUTIONSPECIFICATION_WIDTH / 2.0 : 0;
	}

	/**
	 * @param tick the time at which the width should be calculated
	 * @return 0 if it is only a line, otherwise return the width from
	 * the center to the right outermost border of the execution specification
	 * @see Coregion Coregion for an example how this is used for drawing
	 */
	public double getLifelineRightPartWidth(int tick) {
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
		double minWidth = DESTROY_SIZE;
		for (LifelineOccurrence llOccurrence : lifeline.values()) {
			minWidth = Math.max(minWidth, llOccurrence.getMinWidth(drawHandler));
		}
		if (activeAreas.size() == 1) {
			minWidth = Math.max(minWidth, EXECUTIONSPECIFICATION_WIDTH);
		}
		else if (activeAreas.size() > 1) {
			int maxSimultaneousExecSpec = 0;
			// for each start of an area calculate the currently active ExecutionSpecifications and set the maximum
			for (ExecutionSpecification activeArea : activeAreas) {
				maxSimultaneousExecSpec = Math.max(maxSimultaneousExecSpec,
						getCurrentlyActiveExecutionSpecifications(activeArea.getStartTick()));
			}
			minWidth = Math.max(minWidth,
					(maxSimultaneousExecSpec - 1) * (EXECUTIONSPECIFICATION_WIDTH - EXECUTIONSPECIFICATION_OVERLAPP) * 2 + EXECUTIONSPECIFICATION_WIDTH);
		}
		minWidth = Math.max(minWidth, getHeadMinWidth(drawHandler));
		return minWidth;
	}

	private int getCurrentlyActiveExecutionSpecifications(int tick) {
		int currentlyActiveExecSpec = 0;
		for (int i = 0; i < activeAreas.size() && tick >= activeAreas.get(i).getStartTick(); i++) {
			if (activeAreas.get(i).enclosesTick(tick)) {
				currentlyActiveExecSpec++;
			}
		}
		return currentlyActiveExecSpec;
	}

	/**
	 * Calculates the additonal height which are needed by the occurrences on this lifeline
	 * @param drawHandler
	 * @param drawingInfo
	 * @param defaultTickHeight
	 * @return a Map which stores the ticks as keys and the additional height as values
	 * (i.e. the height which exceeds the tickHeight). This values are all &gt;= 0
	 */
	public Map<Integer, Double> getAdditionalYHeights(DrawHandler drawHandler,
			LifelineHorizontalDrawingInfo drawingInfo, double defaultTickHeight) {
		PointDouble size;
		double additionalY;
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		for (Map.Entry<Integer, LifelineOccurrence> e : lifeline.entrySet()) {
			size = new PointDouble(drawingInfo.getSymmetricWidth(e.getKey()), defaultTickHeight);
			additionalY = e.getValue().getAdditionalYHeight(drawHandler, size);
			if (additionalY > 0) {
				ret.put(e.getKey(), additionalY);
			}
		}
		// add head size if it the obj is created with an message
		if (!createdOnStart && created != null) {
			double headAdditionalHeight = getHeadMinHeight(drawHandler, drawingInfo.getSymmetricWidth(created)) - defaultTickHeight;
			if (headAdditionalHeight > 0) {
				if (ret.containsKey(created)) {
					ret.put(created, Math.max(ret.get(created), headAdditionalHeight));
				}
				else {
					ret.put(created, headAdditionalHeight);
				}
			}
		}
		if (destroyed != null && DESTROY_SIZE > defaultTickHeight) {
			if (ret.containsKey(destroyed)) {
				ret.put(destroyed, Math.max(ret.get(destroyed), DESTROY_SIZE - defaultTickHeight));
			}
			else {
				ret.put(destroyed, DESTROY_SIZE - defaultTickHeight);
			}
		}
		return ret;

	}

	private double getHeadMinWidth(DrawHandler drawHandler) {
		double minWidth = ACTOR_SIZE.x; // actor width is small, so use it as minimum width
		minWidth = Math.max(minWidth, TextSplitter.getTextMinWidth(text, drawHandler));
		if (headType == LifelineHeadType.STANDARD) {
			minWidth = minWidth + HEAD_HORIZONTAL_BORDER_PADDING * 2;
		}
		else if (headType == LifelineHeadType.ACTIVE_CLASS) {
			minWidth = minWidth + HEAD_HORIZONTAL_BORDER_PADDING * 2 + ACTIVE_CLASS_DOUBLE_BORDER_GAP * 2;
		}
		return minWidth;
	}

	public double getHeadMinHeight(DrawHandler drawHandler, double width) {
		if (headType == LifelineHeadType.STANDARD || headType == LifelineHeadType.ACTIVE_CLASS) {
			width -= HEAD_HORIZONTAL_BORDER_PADDING * 2;
			if (headType == LifelineHeadType.ACTIVE_CLASS) {
				width -= ACTIVE_CLASS_DOUBLE_BORDER_GAP * 2;
			}
		}
		double minHeight = TextSplitter.getSplitStringHeight(text, width, drawHandler);
		if (headType == LifelineHeadType.ACTOR) {
			minHeight += ACTOR_SIZE.y;
		}
		else if (headType == LifelineHeadType.ACTIVE_CLASS || headType == LifelineHeadType.STANDARD) {
			minHeight += HEAD_VERTICAL_BORDER_PADDING * 2;
		}
		else {
			log.error("Encountered unhandled enumeration value '" + headType + "'.");
		}
		return minHeight;
	}

	/**
	 * @param drawHandler
	 * @param drawingInfo information about the drawing positions
	 * @param lifelineLastTick the last tick of the diagram
	 */
	public void draw(DrawHandler drawHandler, LifelineDrawingInfo drawingInfo, int lifelineLastTick) {
		// draw Head with text
		if (createdOnStart) {
			drawHead(drawHandler, drawingInfo.getHorizontalStart(), drawingInfo.getVerticalHeadStart(),
					drawingInfo.getWidth(), drawingInfo.getHeadHeight());
		}
		// check if the creation tick was set, while writing a diagram it can be possible that the message that
		// creates this head wasn't yet written
		else if (created != null) {
			drawHead(drawHandler, drawingInfo.getSymmetricHorizontalStart(created), drawingInfo.getVerticalStart(created),
					drawingInfo.getSymmetricWidth(created), drawingInfo.getTickHeight(created));
		}
		// without an starting point we can not draw anything
		if (createdOnStart || created != null) {
			// draw lifeline occurrences
			for (Map.Entry<Integer, LifelineOccurrence> e : lifeline.entrySet()) {
				int tick = e.getKey();
				PointDouble topLeftOccurence = new PointDouble(drawingInfo.getSymmetricHorizontalStart(tick),
						drawingInfo.getVerticalStart(tick));
				PointDouble sizeOccurence = new PointDouble(drawingInfo.getSymmetricWidth(tick),
						drawingInfo.getTickHeight(tick));
				Line1D llInterruption = e.getValue().draw(drawHandler, topLeftOccurence, sizeOccurence);
				if (llInterruption != null) {
					drawingInfo.addInterruptedArea(llInterruption);
				}
			}
			// draw actual lifeline (horizontal line)
			drawLifeline(drawHandler, drawingInfo, lifelineLastTick);
		}
	}

	/**
	 * Draws the actual line (dashed or the rectangles of the execution specification)
	 * @param drawHandler
	 * @param drawingInfo
	 * @param lifelineLastTick
	 */
	private void drawLifeline(DrawHandler drawHandler, LifelineDrawingInfo drawingInfo, int lifelineLastTick) {
		int currentStartTick = 0;
		int endTick;
		int currentActiveCount = 0;
		boolean startInc = false;
		boolean endInc;
		// used as stack with newest elements at start
		LinkedList<ExecutionSpecification> active = new LinkedList<ExecutionSpecification>();
		if (!createdOnStart) {
			currentStartTick = created + 1;
		}
		ListIterator<ExecutionSpecification> execSpecIter = activeAreas.listIterator();
		ListIterator<Line1D> interruptedAreasIter = drawingInfo.getInterruptedAreas().listIterator();
		LineType oldLt = drawHandler.getLineType();
		// add execution specification which start from the creation before the loop so that they are drawn directly beneath the head
		if (execSpecFromStart && execSpecIter.hasNext()) {
			ExecutionSpecification execSpec = execSpecIter.next();
			if (execSpec.getStartTick() == currentStartTick - 1) {
				active.addFirst(execSpec);
				startInc = true;
			}
			else {
				execSpecIter.previous();
			}
		}
		double llTopY = drawingInfo.getVerticalStart(currentStartTick) - drawingInfo.getTickVerticalPadding();
		while (active.size() > 0 || execSpecIter.hasNext()) {
			// find change of drawing style; if a new executionSpecification starts or an old ends
			currentActiveCount = active.size();
			if (active.size() > 0 && execSpecIter.hasNext()) {
				ExecutionSpecification execSpec = execSpecIter.next();
				if (active.getFirst().getEndTick() < execSpec.getStartTick()) {
					execSpecIter.previous();
					endInc = false;
					endTick = active.removeFirst().getEndTick();
				}
				else {
					endInc = true;
					endTick = execSpec.getStartTick();
					active.addFirst(execSpec);
				}
			}
			else if (active.size() > 0) {
				endInc = false;
				endTick = active.removeFirst().getEndTick();
			}
			else { // execSpecIter.hasNext() is true
				ExecutionSpecification execSpec = execSpecIter.next();
				endInc = true;
				endTick = execSpec.getStartTick();
				active.addFirst(execSpec);
			}

			double llBottomY = drawingInfo.getVerticalCenter(endTick);
			// topY + endTick * tickHeight + tickHeight / 2 + accumulativeAddiontalHeightOffsets[endTick] / 2 + accumulativeAddiontalHeightOffsets[endTick + 1] / 2;
			drawLifelinePart(drawHandler, drawingInfo.getHorizontalCenter(),
					llTopY,
					startInc,
					llBottomY,
					endInc,
					currentActiveCount,
					interruptedAreasIter);
			currentStartTick = endTick;
			llTopY = llBottomY;
			startInc = endInc;
		}
		// draw final line segment
		if (destroyed == null) {
			drawLifelinePart(drawHandler, drawingInfo.getHorizontalCenter(),
					llTopY,
					false,
					drawingInfo.getVerticalEnd(lifelineLastTick),
					false,
					0,
					interruptedAreasIter);
		}
		else {
			drawHandler.setLineType(LineType.SOLID);
			double halfSize = DESTROY_SIZE / 2.0;
			double centerX = drawingInfo.getHorizontalCenter();
			double centerY = drawingInfo.getVerticalCenter(destroyed);
			drawHandler.drawLine(centerX - halfSize, centerY - halfSize, centerX + halfSize, centerY + halfSize);
			drawHandler.drawLine(centerX + halfSize, centerY - halfSize, centerX - halfSize, centerY + halfSize);
			if (destroyed > currentStartTick) {

				drawLifelinePart(drawHandler, drawingInfo.getHorizontalCenter(),
						llTopY,
						false,
						drawingInfo.getVerticalCenter(destroyed),
						false,
						0,
						interruptedAreasIter);
			}
		}
		drawHandler.setLineType(oldLt);
	}

	/**
	 * draw a part of the Lifeline which has the same active count
	 * (same number of active ExecutionSpecifications), which changes at startY ending at endY.
	 * Draws head if increment at start, draws end if decrement at end.
	 *
	 * @param drawHandler
	 * @param centerX
	 * @param startY
	 * @param activeCountIncStart true if the active count was lower before the start
	 * @param endY
	 * @param activeCountIncEnd true if the active count is higher after the end
	 * @param activeCount
	 * @param interruptedAreas Iterator which point to the first span where span.End &gt; startY, after the call it points to the first span where span.End &gt; endY
	 */
	private void drawLifelinePart(DrawHandler drawHandler, final double centerX,
			final double startY, boolean activeCountIncStart,
			final double endY, boolean activeCountIncEnd,
			int activeCount, ListIterator<Line1D> interruptedAreas) {
		double nextStartY = startY;
		boolean drawHead = true;
		// check if we must draw the head
		if (interruptedAreas.hasNext()) {
			Line1D area = interruptedAreas.next();
			if (area.contains(nextStartY)) {
				drawHead = false;
				nextStartY = area.getHigh();
			}
			else {
				interruptedAreas.previous(); // no intersection so push it back
			}
		}
		boolean drawingFinished = false;
		boolean drawEnd = false;
		double currentEndY;
		double currentStartY;
		while (!drawingFinished) {
			currentStartY = nextStartY;
			if (interruptedAreas.hasNext()) {
				Line1D area = interruptedAreas.next();
				if (area.getLow() < endY) {
					currentEndY = area.getLow();
					nextStartY = area.getHigh();

					if (area.getHigh() > endY) {
						drawingFinished = true;
						interruptedAreas.previous();
					}
				}
				// the interruption start after the end
				else {
					interruptedAreas.previous();
					drawingFinished = true;
					drawEnd = true;
					currentEndY = endY;
				}
			}
			// we can draw till the end
			else {
				drawingFinished = true;
				drawEnd = true;
				currentEndY = endY;
			}
			// drawing part
			if (activeCount == 0) {
				drawHandler.setLineType(LineType.DASHED);
				drawHandler.drawLine(centerX, currentStartY, centerX, currentEndY);
			}
			else {
				drawHandler.setLineType(LineType.SOLID);
				// the right border of an rectangle is always overlaid by the rectangle to its right -> draw left lines and the right most line
				double lineX = centerX - EXECUTIONSPECIFICATION_WIDTH / 2.0;
				drawHandler.drawLine(lineX, currentStartY, lineX, currentEndY);
				for (int i = 0; i < activeCount - 1; i++) {
					lineX += EXECUTIONSPECIFICATION_WIDTH - EXECUTIONSPECIFICATION_OVERLAPP;
					drawHandler.drawLine(lineX, currentStartY, lineX, currentEndY);
				}
				lineX += EXECUTIONSPECIFICATION_WIDTH;
				drawHandler.drawLine(lineX, currentStartY, lineX, currentEndY);
				if (drawHead && activeCountIncStart) {
					// draw top border
					drawHandler.drawLine(lineX - EXECUTIONSPECIFICATION_WIDTH, currentStartY, lineX, currentStartY);
				}
				if (drawEnd && !activeCountIncEnd) {
					// draw bottom border
					drawHandler.drawLine(lineX - EXECUTIONSPECIFICATION_WIDTH, currentEndY, lineX, currentEndY);
				}
			}
			drawHead = false;
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
			TextSplitter.drawText(drawHandler, text, x, y, width, height, AlignHorizontal.CENTER, AlignVertical.CENTER);
		}
		else if (headType == LifelineHeadType.ACTOR) {
			DrawHelper.drawActor(drawHandler, (int) (x + width / 2.0), (int) y, ACTOR_DIMENSION);
			y += ACTOR_SIZE.y;
			height -= ACTOR_SIZE.y;
			// draw Text in x,y with width,height
			TextSplitter.drawText(drawHandler, text, x, y, width, height, AlignHorizontal.CENTER, AlignVertical.BOTTOM);
		}
		else {
			log.error("Encountered unhandled enumeration value '" + headType + "'.");
		}
	}

	public enum LifelineHeadType {
		STANDARD, ACTIVE_CLASS, ACTOR
	}

}
