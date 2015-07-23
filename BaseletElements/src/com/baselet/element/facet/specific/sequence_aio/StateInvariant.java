package com.baselet.element.facet.specific.sequence_aio;

import java.util.ArrayList;
import java.util.Collection;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

public class StateInvariant implements LifelineOccurrence {
	/** the width of the ellipse which builds the left and right border (no text is drawn in this) */
	private static final double ROUND_PART_WIDTH = 20;
	/** the space between the text and the border line */
	private static final double VERTICAL_BORDER_PADDING = 5;
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
		Collection<String> wrappedText = splitText(drawHandler, size.x);
		double height = getHeight(drawHandler, size.x, wrappedText);
		double textY = topLeft.y + (size.y - height) / 2;
		Line1D interruptedLifeline = new Line1D(textY, textY + height);
		if (style == StateInvariantStyle.STATE) {
			// draw border and adjust textY and textLeftX
			drawHandler.drawArc(topLeft.x, textY, ROUND_PART_WIDTH * 2, height, 90, 180, true);
			drawHandler.drawArc(topLeft.x + size.x - ROUND_PART_WIDTH * 2, textY, ROUND_PART_WIDTH * 2, height, 270, 180, true);
			drawHandler.drawLine(topLeft.x + ROUND_PART_WIDTH, textY, topLeft.x + size.x - ROUND_PART_WIDTH, textY);
			drawHandler.drawLine(topLeft.x + ROUND_PART_WIDTH, textY + height, topLeft.x + size.x - ROUND_PART_WIDTH, textY + height);
			textY += VERTICAL_BORDER_PADDING;
		}
		textY += drawHandler.textHeightMax();
		for (String line : wrappedText) {
			drawHandler.print(line, topLeft.x + size.x / 2.0, textY, AlignHorizontal.CENTER);
			textY += drawHandler.textHeightMaxWithSpace();
		}
		return interruptedLifeline;

	}

	@Override
	public double getMinWidth(DrawHandler drawHandler) {
		// search for the biggest word which can't be wrapped and return its size
		double minWidth = MIN_WIDTH;
		for (String line : lines) {
			for (String word : line.split("\\s+")) {
				minWidth = Math.max(minWidth, drawHandler.textWidth(word));
			}
		}
		if (style == StateInvariantStyle.CURLY_BRACKETS) {
			return minWidth;
		}
		else {
			return minWidth + ROUND_PART_WIDTH * 2;
		}
	}

	@Override
	public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
		return getHeight(drawHandler, size.x) - size.y;
	}

	/**
	 * calculate the needed y space in respect to the width which we can use
	 * @param drawHandler
	 * @param textWidth
	 * @return the needed y space in respect to the width which we can use
	 */
	private double getHeight(DrawHandler drawHandler, double textWidth) {
		return getHeight(drawHandler, textWidth, splitText(drawHandler, textWidth));
	}

	/**
	 * calculate the needed y space in respect to the width which we can use
	 * @param drawHandler
	 * @param textWidth
	 * @param wrappedText the already wrapped text
	 * @return the needed y space in respect to the width which we can use
	 */
	private double getHeight(DrawHandler drawHandler, double textWidth, Collection<String> wrappedText) {
		if (style == StateInvariantStyle.CURLY_BRACKETS) {
			return wrappedText.size() * drawHandler.textHeightMaxWithSpace();
		}
		else {
			return wrappedText.size() * drawHandler.textHeightMaxWithSpace() + VERTICAL_BORDER_PADDING * 2;
		}
	}

	private Collection<String> splitText(DrawHandler drawHandler, double textWidth) {
		String wrappedLine;
		ArrayList<String> splittedLines = new ArrayList<String>(lines.length);
		for (String line : lines) {
			do {
				wrappedLine = TextSplitter.splitString(line, textWidth, drawHandler);
				splittedLines.add(wrappedLine);
				line = line.substring(wrappedLine.length()).trim();
			} while (!line.isEmpty());
		}
		return splittedLines;
	}

	public enum StateInvariantStyle {
		STATE, CURLY_BRACKETS
		// UML 2.5 also allows a NOTE as a StateInvariant but this is not supported by this diagram (yet)
	}

}
