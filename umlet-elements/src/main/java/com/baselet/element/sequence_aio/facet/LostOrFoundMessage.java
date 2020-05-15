package com.baselet.element.sequence_aio.facet;

import com.baselet.diagram.draw.helper.ColorOwn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.relation.helper.RelationDrawer;
import com.baselet.element.relation.helper.RelationDrawer.ArrowEndType;
import com.baselet.element.sequence_aio.facet.Message.ArrowType;

/**
 * Class for representing a lost or a found message.
 * Lost Messages are always on the right side of the lifeline and found messages are always on the left side of the lifeline.
 * Therefore the message always travels from left to right.
 *
 */
public class LostOrFoundMessage implements LifelineOccurrence {

	private static final Logger log = LoggerFactory.getLogger(LostOrFoundMessage.class);
	private static final double CIRCLE_RADIUS = 10;
	private static final double LIFELINE_TEXT_PADDING = Math.max(RelationDrawer.ARROW_LENGTH, CIRCLE_RADIUS * 2) + 3;

	private final Lifeline lifeline;
	// private final Lifeline to;
	private final int sendTick;
	private final String[] textLines;
	private final ArrowType arrowType;
	private final LineType lineType;
	private final boolean found;

	/**
	 *
	 * @param lifeline
	 * @param found true if it is a found message, if false it is a lost message
	 * @param sendTick
	 * @param text can be more than one line, but if so the lines must be separated by a \n
	 * @param arrowType
	 * @param lineType
	 */
	public LostOrFoundMessage(Lifeline lifeline, boolean found, int sendTick, String text, ArrowType arrowType, LineType lineType) {
		super();
		this.lifeline = lifeline;
		this.found = found;
		this.sendTick = sendTick;
		textLines = text.split("\n");
		this.arrowType = arrowType;
		this.lineType = lineType;
	}

	public double getCenterXOffset() {
		if (found) {
			return -lifeline.getLifelineLeftPartWidth(sendTick);
		}
		else {
			return lifeline.getLifelineRightPartWidth(sendTick);
		}
	}

	@Override
	public Line1D draw(DrawHandler drawHandler, PointDouble topLeft, PointDouble size) {
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
		double centerY = topLeft.y + size.y / 2;
		double lineXStart, lineXEnd;
		ColorOwn oldBg = drawHandler.getBackgroundColor();
		drawHandler.setBackgroundColor(drawHandler.getForegroundColor());
		if (found) {
			drawHandler.drawCircle(topLeft.x + CIRCLE_RADIUS, centerY, CIRCLE_RADIUS);
			lineXStart = topLeft.x + CIRCLE_RADIUS * 2;
			lineXEnd = topLeft.x + size.x / 2 + getCenterXOffset();
		}
		else {
			drawHandler.drawCircle(topLeft.x + size.x - CIRCLE_RADIUS, topLeft.y + size.y / 2, CIRCLE_RADIUS);
			lineXStart = topLeft.x + size.x / 2 + getCenterXOffset();
			lineXEnd = topLeft.x + size.x - CIRCLE_RADIUS * 2;
		}
		drawHandler.setBackgroundColor(oldBg);

		TextSplitter.drawText(drawHandler, textLines, lineXStart, topLeft.y,
				lineXEnd - lineXStart, size.y / 2, AlignHorizontal.CENTER, AlignVertical.BOTTOM);
		PointDouble arrowPoint = new PointDouble(lineXEnd, centerY);
		Line line = new Line(new PointDouble(lineXStart, centerY), arrowPoint);
		drawHandler.drawLine(line);
		RelationDrawer.drawArrowToLine(arrowPoint, drawHandler, line, false, arrowEndType, fillArrow, false);
		drawHandler.setLineType(oldLt);
		return null;
	}

	@Override
	public double getMinWidth(DrawHandler drawHandler) {
		return (TextSplitter.getTextMinWidth(textLines, drawHandler)
				+ LIFELINE_TEXT_PADDING * 2 + Math.abs(getCenterXOffset())) * 2;
	}

	@Override
	public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
		return TextSplitter.getSplitStringHeight(textLines,
				size.x / 2 - LIFELINE_TEXT_PADDING * 2 - Math.abs(getCenterXOffset()), drawHandler
				) * 2 - size.y;
	}

	public OccurrenceSpecification sendOccurrenceSpecification() {
		if (found) {
			throw new IllegalStateException("A found message has no send occurrence specification.");
		}
		return new LostOrFoundOccurrenceSpecification();
	}

	public OccurrenceSpecification receiveOccurrenceSpecification() {
		if (!found) {
			throw new IllegalStateException("A lost message has no receive occurrence specification.");
		}
		return new LostOrFoundOccurrenceSpecification();
	}

	private class LostOrFoundOccurrenceSpecification implements OccurrenceSpecification {

		@Override
		public Lifeline getLifeline() {
			return lifeline;
		}

		@Override
		public boolean hasFixedPosition() {
			return true;
		}

		@Override
		public AlignHorizontal getFixedPositionAlignment() {
			if (getCenterXOffset() >= 0) {
				return AlignHorizontal.RIGHT;
			}
			else {
				return AlignHorizontal.LEFT;
			}
		}

		@Override
		public double getHorizonatlPosition(HorizontalDrawingInfo hDrawingInfo) {
			return hDrawingInfo.getHDrawingInfo(getLifeline()).getHorizontalCenter() + getCenterXOffset();
		}

		@Override
		public double getHorizontalPosition(HorizontalDrawingInfo llHDrawingInfo, boolean left) {
			return 0;
		}

		@Override
		public PointDouble getPosition(DrawingInfo drawingInfo) {
			return new PointDouble(getHorizonatlPosition(drawingInfo), drawingInfo.getVerticalCenter(sendTick));
		}

		@Override
		public PointDouble getPosition(DrawingInfo drawingInfo, boolean left) {
			return new PointDouble(0, 0);
		}

	}

}
