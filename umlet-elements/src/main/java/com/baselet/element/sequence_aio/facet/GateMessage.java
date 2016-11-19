package com.baselet.element.sequence_aio.facet;

import java.util.Map;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.relation.helper.RelationDrawer.ArrowEndType;

public class GateMessage extends Message {

	/** true if the sender is a gate */
	private final boolean sendGate;

	private final boolean dockToLeftBorder;

	private GateMessage(Lifeline from, Lifeline to, int sendTick, String text, ArrowType arrowType, LineType lineType,
			boolean sendGate, boolean dockToLeftBorder) {
		super(from, to, 0, sendTick, text, arrowType, lineType);
		this.sendGate = sendGate;
		this.dockToLeftBorder = dockToLeftBorder;
	}

	public static GateMessage createSendGateMessage(Lifeline receiver, int sendTick, String text, ArrowType arrowType,
			LineType lineType, Lifeline leftMostLifeline, Lifeline rightMostLifeline) {
		if (Math.abs(receiver.getIndex() - leftMostLifeline.getIndex()) <= Math.abs(receiver.getIndex() - rightMostLifeline.getIndex())) {
			return new GateMessage(leftMostLifeline, receiver, sendTick, text, arrowType, lineType, true, true);
		}
		else {
			return new GateMessage(rightMostLifeline, receiver, sendTick, text, arrowType, lineType, true, false);
		}
	}

	public static GateMessage createReceiveGateMessage(Lifeline sender, int sendTick, String text, ArrowType arrowType,
			LineType lineType, Lifeline leftMostLifeline, Lifeline rightMostLifeline) {
		if (Math.abs(sender.getIndex() - leftMostLifeline.getIndex()) < Math.abs(sender.getIndex() - rightMostLifeline.getIndex())) {
			return new GateMessage(sender, leftMostLifeline, sendTick, text, arrowType, lineType, false, true);
		}
		else {
			return new GateMessage(sender, rightMostLifeline, sendTick, text, arrowType, lineType, false, false);
		}
	}

	@Override
	protected double getSendX(HorizontalDrawingInfo hDrawingInfo) {
		if (sendGate) {
			if (dockToLeftBorder) {
				return hDrawingInfo.getDiagramHorizontalStart();
			}
			else {
				return hDrawingInfo.getDiagramHorizontalEnd();
			}
		}
		else {
			return super.getSendX(hDrawingInfo);
		}
	}

	@Override
	protected double getReceiveX(HorizontalDrawingInfo hDrawingInfo) {
		if (sendGate) {
			return super.getReceiveX(hDrawingInfo);
		}
		else {
			if (dockToLeftBorder) {
				return hDrawingInfo.getDiagramHorizontalStart();
			}
			else {
				return hDrawingInfo.getDiagramHorizontalEnd();
			}
		}
	}

	@Override
	protected void drawSelfMessage(DrawHandler drawHandler, PointDouble send, PointDouble receive, ArrowEndType arrowEndType, boolean fillArrow, DrawingInfo hInfo) {
		super.drawNormalMessage(drawHandler, send, receive, arrowEndType, fillArrow, hInfo);
		// draw the self messages as normal messages because these message aren't real self messages
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		double executionSpecWidth = !sendGate ? Math.abs(getSendCenterXOffset()) : Math.abs(getReceiveCenterXOffset());
		double neededWidth = executionSpecWidth + TextSplitter.getTextMinWidth(textLines, drawHandler)
								+ LIFELINE_TEXT_PADDING * 2;
		if (from == to) {
			return neededWidth * 2;
		}
		else {
			int affectedLifelineCount = getLastLifeline().getIndex() - getFirstLifeline().getIndex();
			double padding = lifelineHorizontalPadding * affectedLifelineCount;
			return (neededWidth - padding) * (affectedLifelineCount + 1) / (affectedLifelineCount + 0.5);
		}
	};

	@Override
	protected void getEveryAdditionalYHeightSelfMessage(DrawHandler drawHandler, HorizontalDrawingInfo hInfo, double defaultTickHeight, Map<Integer, Double> ret) {
		super.getEveryAdditionalYHeightNormalMessage(drawHandler, hInfo, defaultTickHeight, ret);
	}

	@Override
	public OccurrenceSpecification sendOccurrenceSpecification() {
		if (sendGate) {
			throw new IllegalStateException("This GateMessage was created with an sender gate, and a gate has no associated occurrence specification.");
		}
		return super.sendOccurrenceSpecification();
	}

	@Override
	public OccurrenceSpecification receiveOccurrenceSpecification() {
		if (!sendGate) {
			throw new IllegalStateException("This GateMessage was created with an receiver gate, and a gate has no associated occurrence specification.");
		}
		return super.receiveOccurrenceSpecification();
	}

}
