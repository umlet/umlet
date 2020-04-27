package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

public class Continuation implements LifelineSpanningTickSpanningOccurrence {
	/** the width of the ellipse which builds the left and right border (no text is drawn in this) */
	private static final double ROUND_PART_WIDTH = 20;
	/** the space between the text and the border line */
	private static final double VERTICAL_BORDER_PADDING = 5;

	private final int tick;
	private final String[] textLines;
	private final Lifeline[] coveredLifelines;

	public Continuation(int tick, String text, Lifeline[] coveredLifelines) {
		super();
		this.tick = tick;
		textLines = text.split("\n");
		this.coveredLifelines = Arrays.copyOf(coveredLifelines, coveredLifelines.length);
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
	public void draw(DrawHandler drawHandler, DrawingInfo drawingInfo) {
		double width = drawingInfo.getSymmetricWidth(getFirstLifeline(), getLastLifeline(), tick);
		double height = TextSplitter.getSplitStringHeight(textLines, width - ROUND_PART_WIDTH * 2, drawHandler) + VERTICAL_BORDER_PADDING * 2;
		double topY = drawingInfo.getVerticalStart(tick);
		topY += (drawingInfo.getTickHeight(tick) - height) / 2;
		double leftX = drawingInfo.getHDrawingInfo(getFirstLifeline()).getSymmetricHorizontalStart(tick);

		drawHandler.drawArc(leftX, topY, ROUND_PART_WIDTH * 2, height, 90, 180, true);
		width = width - ROUND_PART_WIDTH * 2;
		drawHandler.drawArc(leftX + width, topY, ROUND_PART_WIDTH * 2, height, 270, 180, true);
		drawHandler.drawLine(leftX + ROUND_PART_WIDTH, topY, leftX + width + ROUND_PART_WIDTH, topY);
		drawHandler.drawLine(leftX + ROUND_PART_WIDTH, topY + height, leftX + width + ROUND_PART_WIDTH, topY + height);
		TextSplitter.drawText(drawHandler, textLines, leftX + ROUND_PART_WIDTH, topY, width, height,
				AlignHorizontal.CENTER, AlignVertical.CENTER);

		for (Lifeline ll : coveredLifelines) {
			drawingInfo.getDrawingInfo(ll).addInterruptedArea(new Line1D(topY, topY + height));
		}
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		return TextSplitter.getTextMinWidth(textLines, drawHandler) + ROUND_PART_WIDTH * 2;
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler,
			HorizontalDrawingInfo hInfo, double defaultTickHeight
	// Line1D[] lifelinesHorizontalSpanning, double tickHeight
	) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		double neededHeight = TextSplitter.getSplitStringHeight(textLines,
				hInfo.getSymmetricWidth(getFirstLifeline(), getLastLifeline(), tick) - ROUND_PART_WIDTH * 2,
				drawHandler) + VERTICAL_BORDER_PADDING * 2;
		if (neededHeight > defaultTickHeight) {
			ret.put(tick, neededHeight - defaultTickHeight);
		}
		return ret;
	}

	@Override
	public ContainerPadding getPaddingInformation() {
		return null;
	}

}
