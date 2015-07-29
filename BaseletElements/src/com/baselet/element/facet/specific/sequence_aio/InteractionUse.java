package com.baselet.element.facet.specific.sequence_aio;

import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.AdvancedTextSplitter;
import com.baselet.diagram.draw.DrawHandler;

public class InteractionUse implements LifelineSpanningTickSpanningOccurrence {

	private static final double TEXT_X_PADDING = 5;
	private static final double TEXT_Y_PADDING = 5;
	private static final double HEADER_BOTTOM_PADDING = 4;
	private static final String[] HEADER_TEXT = new String[] { "ref" };

	private final int tick;
	private final String[] textLines;
	private final Lifeline[] coveredLifelines;

	public InteractionUse(int tick, String text, Lifeline[] coveredLifelines) {
		this(tick, text.split("\n"), coveredLifelines);
	}

	public InteractionUse(int tick, String[] textLines, Lifeline[] coveredLifelines) {
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
		double height = getHeight(drawHandler, width);
		double relativeTopY = tick * tickHeight + accumulativeAddiontalHeightOffsets[tick];
		double usableTickHeight = (tick + 1) * tickHeight + accumulativeAddiontalHeightOffsets[tick + 1] - relativeTopY;
		relativeTopY += (usableTickHeight - height) / 2;
		PointDouble topLeft = new PointDouble(lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow(), topY + relativeTopY);

		drawHandler.drawRectangle(topLeft.x, topLeft.y, width, height);
		PointDouble pentagonSize = PentagonDrawingHelper.draw(drawHandler, HEADER_TEXT, width, topLeft);
		AdvancedTextSplitter.drawText(drawHandler, textLines, topLeft.x + pentagonSize.x + TEXT_X_PADDING,
				topLeft.y, width - (pentagonSize.x + TEXT_X_PADDING) * 2, height,
				AlignHorizontal.CENTER, AlignVertical.CENTER);

		Map<Integer, Line1D[]> ret = new HashMap<Integer, Line1D[]>();
		for (int i = getFirstLifeline().getIndex(); i <= getLastLifeline().getIndex(); i++) {
			ret.put(i, new Line1D[] { new Line1D(topLeft.y, topLeft.y + height) });
		}
		return ret;
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		return PentagonDrawingHelper.getPentagonMinimumWidth(drawHandler, HEADER_TEXT) * 2
				+ TEXT_X_PADDING * 2
				+ AdvancedTextSplitter.getTextMinWidth(textLines, drawHandler);
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler,
			Line1D[] lifelinesHorizontalSpanning, double tickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		double width = lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh()
						- lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow();
		if (getHeight(drawHandler, width) > tickHeight) {
			ret.put(tick, getHeight(drawHandler, width) - tickHeight);
		}
		return ret;
	}

	private double getHeight(DrawHandler drawHandler, double width) {
		double textWidth = width - TEXT_X_PADDING * 2
							- PentagonDrawingHelper.getPentagonMinimumWidth(drawHandler, HEADER_TEXT) * 2;
		return Math.max(PentagonDrawingHelper.getHeight(drawHandler, HEADER_TEXT, width) + HEADER_BOTTOM_PADDING,
				AdvancedTextSplitter.getSplitStringHeight(textLines, textWidth, drawHandler) + TEXT_Y_PADDING * 2);
	}

}
