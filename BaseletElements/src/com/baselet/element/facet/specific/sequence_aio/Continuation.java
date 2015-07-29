package com.baselet.element.facet.specific.sequence_aio;

import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.AdvancedTextSplitter;
import com.baselet.diagram.draw.DrawHandler;

public class Continuation implements LifelineSpanningTickSpanningOccurrence {
	/** the width of the ellipse which builds the left and right border (no text is drawn in this) */
	private static final double ROUND_PART_WIDTH = 20;
	/** the space between the text and the border line */
	private static final double VERTICAL_BORDER_PADDING = 5;

	private final int tick;
	private final String[] textLines;
	private final Lifeline[] coveredLifelines;

	public Continuation(int tick, String text, Lifeline[] coveredLifelines) {
		this(tick, text.split("\n"), coveredLifelines);
	}

	public Continuation(int tick, String[] textLines, Lifeline[] coveredLifelines) {
		super();
		this.tick = tick;
		this.textLines = textLines;
		this.coveredLifelines = coveredLifelines;
	}

	@Override
	public Lifeline getFirstLifeline() {
		return coveredLifelines[0];
	}

	@Override
	public Lifeline getLastLifeline() {
		return coveredLifelines[coveredLifelines.length - 1];
	}

	@Override
	public Map<Integer, Line1D[]> draw(DrawHandler drawHandler, double topY,
			Line1D[] lifelinesHorizontalSpanning, double tickHeight, double[] accumulativeAddiontalHeightOffsets) {
		double width = lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh()
						- lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow();
		double height = AdvancedTextSplitter.getSplitStringHeight(textLines, width - ROUND_PART_WIDTH * 2, drawHandler) + VERTICAL_BORDER_PADDING * 2;
		double relativeTopY = tick * tickHeight + accumulativeAddiontalHeightOffsets[tick];
		double usableTickHeight = (tick + 1) * tickHeight + accumulativeAddiontalHeightOffsets[tick + 1] - relativeTopY;
		relativeTopY += (usableTickHeight - height) / 2;
		topY += relativeTopY;
		double leftX = lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow();

		drawHandler.drawArc(leftX, topY, ROUND_PART_WIDTH * 2, height, 90, 180, true);
		width = width - ROUND_PART_WIDTH * 2;
		drawHandler.drawArc(leftX + width, topY, ROUND_PART_WIDTH * 2, height, 270, 180, true);
		drawHandler.drawLine(leftX + ROUND_PART_WIDTH, topY, leftX + width + ROUND_PART_WIDTH, topY);
		drawHandler.drawLine(leftX + ROUND_PART_WIDTH, topY + height, leftX + width + ROUND_PART_WIDTH, topY + height);
		AdvancedTextSplitter.drawText(drawHandler, textLines, leftX + ROUND_PART_WIDTH, topY, width, height,
				AlignHorizontal.CENTER, AlignVertical.CENTER);

		Map<Integer, Line1D[]> ret = new HashMap<Integer, Line1D[]>();
		for (int i = getFirstLifeline().getIndex(); i <= getLastLifeline().getIndex(); i++) {
			ret.put(i, new Line1D[] { new Line1D(topY, topY + height) });
		}
		return ret;
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		return AdvancedTextSplitter.getTextMinWidth(textLines, drawHandler) + ROUND_PART_WIDTH * 2;
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler,
			Line1D[] lifelinesHorizontalSpanning, double tickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		double neededHeight = AdvancedTextSplitter.getSplitStringHeight(textLines,
				lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh()
						- lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow() - ROUND_PART_WIDTH * 2,
				drawHandler) + VERTICAL_BORDER_PADDING * 2;
		if (neededHeight > tickHeight) {
			ret.put(tick, neededHeight - tickHeight);
		}
		return ret;
	}

}
