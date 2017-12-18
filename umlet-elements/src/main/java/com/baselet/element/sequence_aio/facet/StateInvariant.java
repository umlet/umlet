package com.baselet.element.sequence_aio.facet;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

public class StateInvariant implements LifelineOccurrence {
	/** the width of the ellipse which builds the left and right border (no text is drawn in this) */
	private static final double ROUND_PART_WIDTH = 20;
	/** how much of the round part width can be used for drawing text */
	private static final double ROUND_PART_TEXT_PERCENTAGE = 0.3;
	/** the space between the text and the border line */
	private static final double VERTICAL_BORDER_PADDING = 5;
	private static final double CURLY_BRACKETS_Y_PADDING = 2;
	/** the minimum width, to avoid splitting text with very short words e.g. print "I am I" in one line instead of 3*/
	private static final double MIN_WIDTH = 50;

	private final String[] lines;
	private final StateInvariantStyle style;

	/**
	 *
	 * @param text the text, lines need to be separated by \n
	 * @param style how it should be drawn
	 */
	public StateInvariant(String text, StateInvariantStyle style) {
		super();
		this.style = style;
		if (style == StateInvariantStyle.CURLY_BRACKETS) {
			lines = ("{" + text + "}").split("\n");
		}
		else {
			lines = text.split("\n");
		}
	}

	@Override
	public Line1D draw(DrawHandler drawHandler, PointDouble topLeft, PointDouble size) {
		double height = getHeight(drawHandler, size.x);
		double topY = topLeft.y + (size.y - height) / 2;
		Line1D interruptedLifeline = new Line1D(topY, topY + height);
		if (style == StateInvariantStyle.STATE) {
			// draw border and adjust textY and textLeftX
			drawHandler.drawArc(topLeft.x, topY, ROUND_PART_WIDTH * 2, height, 90, 180, true);
			drawHandler.drawArc(topLeft.x + size.x - ROUND_PART_WIDTH * 2, topY, ROUND_PART_WIDTH * 2, height, 270, 180, true);
			drawHandler.drawLine(topLeft.x + ROUND_PART_WIDTH, topY, topLeft.x + size.x - ROUND_PART_WIDTH, topY);
			drawHandler.drawLine(topLeft.x + ROUND_PART_WIDTH, topY + height, topLeft.x + size.x - ROUND_PART_WIDTH, topY + height);
			TextSplitter.drawText(drawHandler, lines, topLeft.x + ROUND_PART_WIDTH * (1 - ROUND_PART_TEXT_PERCENTAGE), topY,
					size.x - ROUND_PART_WIDTH * (1 - ROUND_PART_TEXT_PERCENTAGE) * 2, height, AlignHorizontal.CENTER, AlignVertical.CENTER);
		}
		else if (style == StateInvariantStyle.CURLY_BRACKETS) {
			TextSplitter.drawText(drawHandler, lines, topLeft.x, topY, size.x, height,
					AlignHorizontal.CENTER, AlignVertical.CENTER);
		}
		return interruptedLifeline;

	}

	@Override
	public double getMinWidth(DrawHandler drawHandler) {
		double minWidth = Math.max(MIN_WIDTH, TextSplitter.getTextMinWidth(lines, drawHandler));
		if (style == StateInvariantStyle.CURLY_BRACKETS) {
			return minWidth;
		}
		else {
			return minWidth + ROUND_PART_WIDTH * (1 - ROUND_PART_TEXT_PERCENTAGE) * 2;
		}
	}

	@Override
	public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
		return getHeight(drawHandler, size.x) - size.y;
	}

	private double getHeight(DrawHandler drawHandler, double totalWidth) {
		if (style == StateInvariantStyle.STATE) {
			totalWidth -= ROUND_PART_WIDTH * (1 - ROUND_PART_TEXT_PERCENTAGE) * 2;
		}
		if (style == StateInvariantStyle.CURLY_BRACKETS) {
			return TextSplitter.getSplitStringHeight(lines, totalWidth, drawHandler) + CURLY_BRACKETS_Y_PADDING * 2;
		}
		else {
			return TextSplitter.getSplitStringHeight(lines, totalWidth, drawHandler) + VERTICAL_BORDER_PADDING * 2;
		}
	}

	public enum StateInvariantStyle {
		STATE, CURLY_BRACKETS
		// UML 2.5 also allows a NOTE as a StateInvariant but this is not supported by this diagram (yet)
	}

}
