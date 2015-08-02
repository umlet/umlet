package com.baselet.element.facet.specific.sequence_aio;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
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
	private final String[] textLines;
	private final ArrowType arrowType;
	private final LineType lineType;

	/**
	 *
	 * @param from
	 * @param to
	 * @param duration &gt;= 0; if from==to (a self message) then duration must be &gt; 0
	 * @param sendTick
	 * @param text can be more than one line, but if so the lines must be separated by a \n
	 * @param arrowType
	 * @param lineType
	 */
	public Message(Lifeline from, Lifeline to, int duration, int sendTick, String text, ArrowType arrowType, LineType lineType) {
		super();
		this.from = from;
		this.to = to;
		this.duration = duration;
		this.sendTick = sendTick;
		textLines = text.split("\n");
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

	public double getReceiveX(HorizontalDrawingInfo hDrawingInfo) {
		double receiveX = hDrawingInfo.getHDrawingInfo(to).getHorizontalCenter();
		receiveX += getReceiveCenterXOffset();
		if (!to.isCreatedOnStart() && to.getCreated() != null && sendTick + duration == to.getCreated()) {
			// create message must end at the head
			if (getFirstLifeline() == to) {
				receiveX = hDrawingInfo.getHDrawingInfo(to).getSymmetricHorizontalEnd(sendTick + duration);
			}
			else {
				receiveX = hDrawingInfo.getHDrawingInfo(to).getSymmetricHorizontalStart(sendTick + duration);
			}
		}
		return receiveX;
	}

	@Override
	public void draw(DrawHandler drawHandler, DrawingInfo drawingInfo) {
		PointDouble send = new PointDouble(
				drawingInfo.getHDrawingInfo(from).getHorizontalCenter() + getSendCenterXOffset(),
				drawingInfo.getVerticalCenter(sendTick));
		PointDouble receive = new PointDouble(getReceiveX(drawingInfo), drawingInfo.getVerticalCenter(sendTick + duration));

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
			drawSelfMessage(drawHandler, send, receive, arrowEndType, fillArrow, drawingInfo);
		}
		else {
			drawNormalMessage(drawHandler, send, receive, arrowEndType, fillArrow, drawingInfo);
		}
		drawHandler.setLineType(oldLt);
	}

	/**
	 * draws a message which is sent between two different lifelines
	 */
	private void drawNormalMessage(DrawHandler drawHandler, PointDouble send, PointDouble receive,
			RelationDrawer.ArrowEndType arrowEndType, boolean fillArrow, DrawingInfo drawingInfo) {
		Line line = new Line(send, receive);
		drawHandler.drawLine(line);
		drawHandler.setLineType(LineType.SOLID);
		RelationDrawer.drawArrowToLine(receive, drawHandler, line, false, arrowEndType, fillArrow, false);

		double height = send.y - drawingInfo.getVerticalStart(sendTick);
		double topLeftX;
		AlignHorizontal hAlignment;
		if (from == getFirstLifeline()) {
			topLeftX = send.x;
			hAlignment = AlignHorizontal.LEFT;
		}
		else {
			topLeftX = receive.x;
			hAlignment = AlignHorizontal.RIGHT;
		}
		if (duration == 0) {
			hAlignment = AlignHorizontal.CENTER;
		}
		topLeftX += LIFELINE_TEXT_PADDING;
		AdvancedTextSplitter.drawText(drawHandler, textLines, topLeftX, send.y - height,
				Math.abs(send.x - receive.x) - LIFELINE_TEXT_PADDING * 2, height, hAlignment, AlignVertical.BOTTOM);
	}

	/**
	 * draws a message which is sent to the same lifeline
	 */
	private void drawSelfMessage(DrawHandler drawHandler, PointDouble send, PointDouble receive,
			RelationDrawer.ArrowEndType arrowEndType, boolean fillArrow, HorizontalDrawingInfo hInfo) {
		double rightBorderX = Math.max(send.x, receive.x) + SELF_MESSAGE_LIFELINE_GAP;
		PointDouble[] msgLine = new PointDouble[] {
				send,
				new PointDouble(rightBorderX, send.y),
				new PointDouble(rightBorderX, receive.y),
				receive };
		drawHandler.drawLines(msgLine);
		drawHandler.setLineType(LineType.SOLID);
		RelationDrawer.drawArrowToLine(receive, drawHandler, new Line(msgLine[2], msgLine[3]), false, arrowEndType, fillArrow, false);

		rightBorderX += SELF_MESSAGE_TEXT_PADDING;
		double lifelineXEnd = Math.min(hInfo.getHDrawingInfo(to).getSymmetricHorizontalEnd(sendTick),
				hInfo.getHDrawingInfo(to).getSymmetricHorizontalEnd(sendTick + duration));
		AdvancedTextSplitter.drawText(drawHandler, textLines, rightBorderX, send.y,
				lifelineXEnd - rightBorderX, receive.y - send.y, AlignHorizontal.LEFT, AlignVertical.CENTER);
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		if (from == to) {
			double executionSpecWidth = Math.max(from.getLifelineRightPartWidth(sendTick),
					to.getLifelineRightPartWidth(sendTick + duration));
			return (executionSpecWidth + SELF_MESSAGE_LIFELINE_GAP +
					SELF_MESSAGE_TEXT_PADDING + AdvancedTextSplitter.getTextMinWidth(textLines, drawHandler))
			* 2.0; // multiplied by 2, because this space is needed on the right half of the lifeline
		}
		else {
			double executionSpecWidth = Math.abs(getSendCenterXOffset()) + Math.abs(getReceiveCenterXOffset());
			double neededWidth = executionSpecWidth + AdvancedTextSplitter.getTextMinWidth(textLines, drawHandler)
									+ LIFELINE_TEXT_PADDING * 2;
			int affectedLifelineCount = getLastLifeline().getIndex() - getFirstLifeline().getIndex() + 1;
			// increase the needed width because we only calculated the width for the arrow, but we need the overall width
			if (!to.isCreatedOnStart() && to.getCreated() != null && to.getCreated() == sendTick + duration) {
				// here we must compensate for the 1 1/2 lifelines (half one at the send and a full lifeline at the receive)
				return (2 * affectedLifelineCount * neededWidth - 3 * (affectedLifelineCount - 1) * lifelineHorizontalPadding)
						/ (2 * affectedLifelineCount - 3);
			}
			else {
				// here we must compensate for the 2 half lifelines on each end
				return neededWidth / (affectedLifelineCount - 1) * affectedLifelineCount - lifelineHorizontalPadding;
			}
		}
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, HorizontalDrawingInfo hInfo,
			double defaultTickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		double maxTextWidth = 0;
		double additionalHeight = 0;
		if (from == to) {
			// if more y space is needed the all covered ticks will be increased
			double executionSpecWidth = Math.max(from.getLifelineRightPartWidth(sendTick),
					to.getLifelineRightPartWidth(sendTick + duration));
			maxTextWidth = Math.min(hInfo.getHDrawingInfo(to).getSymmetricWidth(sendTick),
					hInfo.getHDrawingInfo(to).getSymmetricWidth(sendTick + duration)) / 2.0;
			maxTextWidth = maxTextWidth - (executionSpecWidth + SELF_MESSAGE_LIFELINE_GAP + SELF_MESSAGE_TEXT_PADDING);
			additionalHeight = AdvancedTextSplitter.getSplitStringHeight(textLines, maxTextWidth, drawHandler)
								- duration * defaultTickHeight;
			if (additionalHeight > 0) {
				for (int i = sendTick + 1; i < sendTick + duration; i++) {
					ret.put(i, additionalHeight / duration);
				}
				ret.put(sendTick, additionalHeight / duration);
				ret.put(sendTick + duration, additionalHeight / duration);
			}
		}
		else {
			// if more y space is needed the send tick will be increased
			// only interested in the x coordinate, so provide fake y values
			maxTextWidth = Math.abs(hInfo.getHDrawingInfo(from).getHorizontalCenter() + getSendCenterXOffset()
									- getReceiveX(hInfo));
			maxTextWidth -= LIFELINE_TEXT_PADDING * 2;
			additionalHeight = AdvancedTextSplitter.getSplitStringHeight(textLines, maxTextWidth, drawHandler);
			// message text is always drawn at the send position only increase send tick height
			// since the message is always drawn in the V center we only can use one half of the tick height
			additionalHeight -= defaultTickHeight / 2.0;
			additionalHeight *= 2;
			if (additionalHeight > 0) {
				ret.put(sendTick, additionalHeight);
			}
		}
		return ret;
	}

	@Override
	public ContainerPadding getPaddingInformation() {
		return null;
	}

	public enum ArrowType {
		OPEN, FILLED
	}
}
