package com.baselet.element.facet.specific.sequence_aio;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.AdvancedTextSplitter;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.relation.helper.RelationDrawer;
import com.baselet.element.relation.helper.RelationDrawer.ArrowEndType;

public class Message implements LifelineSpanningTickSpanningOccurrence {

	private static final Logger log = Logger.getLogger(Message.class);
	private static final double LIFELINE_TEXT_PADDING = RelationDrawer.ARROW_LENGTH + 3;
	private static final double SELF_MESSAGE_LIFELINE_GAP = RelationDrawer.ARROW_LENGTH + 7;
	private static final double SELF_MESSAGE_TEXT_PADDING = 5;

	private final Lifeline from;
	private final Lifeline to;
	/** how many ticks it takes to transmit the message */
	private final int duration;
	private final int sendTick;
	private final String text;
	private final ArrowType arrowType;
	private final LineType lineType;

	public Message(Lifeline from, Lifeline to, int duration, int sendTick, String text, ArrowType arrowType, LineType lineType) {
		super();
		this.from = from;
		this.to = to;
		this.duration = duration;
		this.sendTick = sendTick;
		this.text = text;
		this.arrowType = arrowType;
		this.lineType = lineType;
	}

	@Override
	public Lifeline getFirstLifeline() {
		return from.getIndex() < to.getIndex() ? from : to;
	}

	@Override
	public Lifeline getLastLifeline() {
		return from.getIndex() > to.getIndex() ? from : to;
	}

	public double getSendCenterXOffset() {
		if (from == to) {
			return from.getLifelineRightPartWidth(sendTick);
		}
		else if (getFirstLifeline() == from) {
			return from.getLifelineRightPartWidth(sendTick);
		}
		else {
			return -from.getLifelineLeftPartWidth(sendTick);
		}
	}

	public double getReceiveCenterXOffset() {
		if (from == to) {
			return to.getLifelineRightPartWidth(sendTick + duration);
		}
		else if (getFirstLifeline() == from) {
			return -to.getLifelineLeftPartWidth(sendTick + duration);
		}
		else {
			return to.getLifelineRightPartWidth(sendTick + duration);
		}
	}

	@Override
	public Map<Lifeline, Line1D[]> draw(DrawHandler drawHandler, double topY,
			Line1D[] lifelinesHorizontalSpanning, double tickHeight, double[] accumulativeAddiontalHeightOffsets) {
		double sendY = topY + sendTick * tickHeight + tickHeight / 2
						+ accumulativeAddiontalHeightOffsets[sendTick] / 2
						+ accumulativeAddiontalHeightOffsets[sendTick + 1] / 2;
		double recieveY = topY + (sendTick + duration) * tickHeight + tickHeight / 2
							+ accumulativeAddiontalHeightOffsets[sendTick + duration] / 2
							+ accumulativeAddiontalHeightOffsets[sendTick + duration + 1] / 2;
		double sendX = lifelinesHorizontalSpanning[from.getIndex()].getLow() / 2
						+ lifelinesHorizontalSpanning[from.getIndex()].getHigh() / 2;
		double receiveX = lifelinesHorizontalSpanning[to.getIndex()].getLow() / 2
							+ lifelinesHorizontalSpanning[to.getIndex()].getHigh() / 2;
		sendX += getSendCenterXOffset();
		receiveX += getReceiveCenterXOffset();
		if (!to.isCreatedOnStart() && to.getCreated() != null && sendTick + duration == to.getCreated()) {
			// create message must end at the head
			if (getFirstLifeline() == to) {
				receiveX = lifelinesHorizontalSpanning[to.getIndex()].getHigh();
			}
			else {
				receiveX = lifelinesHorizontalSpanning[to.getIndex()].getLow();
			}
		}
		PointDouble send = new PointDouble(sendX, sendY);
		PointDouble receive = new PointDouble(receiveX, recieveY);

		RelationDrawer.ArrowEndType arrowEndType = ArrowEndType.NORMAL;
		boolean fillArrow = false;
		switch (arrowType) {
			case OPEN:
				arrowEndType = ArrowEndType.NORMAL;
				fillArrow = false;
				break;
			case FILLED:
				arrowEndType = ArrowEndType.CLOSED;
				fillArrow = true;
				break;
			default:
				log.error("Encountered unhandled enumeration value '" + arrowType + "'.");
				break;
		}
		LineType oldLt = drawHandler.getLineType();
		drawHandler.setLineType(lineType);
		if (from == to) {
			drawSelfMessage(drawHandler, send, receive, arrowEndType, fillArrow);
		}
		else {
			drawNormalMessage(drawHandler, send, receive, arrowEndType, fillArrow);
		}
		drawHandler.setLineType(oldLt);
		return new HashMap<Lifeline, Line1D[]>();
	}

	/**
	 * draws a message which is sent between two different lifelines
	 */
	private void drawNormalMessage(DrawHandler drawHandler, PointDouble send, PointDouble receive, RelationDrawer.ArrowEndType arrowEndType, boolean fillArrow) {
		Line line = new Line(send, receive);
		drawHandler.drawLine(line);
		drawHandler.setLineType(LineType.SOLID);
		RelationDrawer.drawArrowToLine(receive, drawHandler, line, false, arrowEndType, fillArrow, false);
		// TODO text
	}

	/**
	 * draws a message which is sent to the same lifeline
	 */
	private void drawSelfMessage(DrawHandler drawHandler, PointDouble send, PointDouble receive, RelationDrawer.ArrowEndType arrowEndType, boolean fillArrow) {
		double rightBorderX = Math.max(send.x, receive.x) + SELF_MESSAGE_LIFELINE_GAP;
		PointDouble[] msgLine = new PointDouble[] {
				send,
				new PointDouble(rightBorderX, send.y),
				new PointDouble(rightBorderX, receive.y),
				receive };
		drawHandler.drawLines(msgLine);
		drawHandler.setLineType(LineType.SOLID);
		RelationDrawer.drawArrowToLine(receive, drawHandler, new Line(msgLine[2], msgLine[3]), false, arrowEndType, fillArrow, false);
		// TODO text
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		if (from == to) {
			double executionSpecWidth = Math.max(from.getLifelineRightPartWidth(sendTick), to.getLifelineRightPartWidth(sendTick + duration));
			return (executionSpecWidth + SELF_MESSAGE_LIFELINE_GAP +
					SELF_MESSAGE_TEXT_PADDING + AdvancedTextSplitter.getTextMinWidth(text, drawHandler))
			* 2.0; // multiplied by 2, because this space is needed on the right half of the lifeline
		}
		else {
			double executionSpecWidth = Math.abs(getSendCenterXOffset()) + Math.abs(getReceiveCenterXOffset());
			double neededWidth = executionSpecWidth + AdvancedTextSplitter.getTextMinWidth(text, drawHandler) + LIFELINE_TEXT_PADDING * 2;
			int affectedLifelineCount = getLastLifeline().getIndex() - getFirstLifeline().getIndex() + 1;
			// increase the needed width because we only calculated the width between the 2 lifelines
			// therefore we must compensate for the 2 half lifelines on each end
			return neededWidth / (affectedLifelineCount - 1) * affectedLifelineCount - lifelineHorizontalPadding;
		}
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, Line1D[] lifelinesHorizontalSpanning, double tickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		// if more y space is needed the send tick will be increased
		double textWidth = 0;
		if (AdvancedTextSplitter.getSplitStringHeight(text, textWidth, drawHandler) - tickHeight * duration > 0) {
			ret.put(sendTick, AdvancedTextSplitter.getSplitStringHeight(text, textWidth, drawHandler) - tickHeight * duration);
		}
		return ret;
	}

	public enum ArrowType {
		OPEN, FILLED
	}
}
