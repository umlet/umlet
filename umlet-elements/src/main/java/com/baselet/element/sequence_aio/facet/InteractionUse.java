package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

public class InteractionUse implements LifelineSpanningTickSpanningOccurrence {

	private static final double TEXT_X_PADDING = 5;
	private static final double TEXT_Y_PADDING = 5;
	private static final double HEADER_BOTTOM_PADDING = 4;
	private static final String[] HEADER_TEXT = new String[] { "ref" };

	private final int tick;
	private final String[] textLines;
	private final Lifeline[] coveredLifelines;

	public InteractionUse(int tick, String text, Lifeline[] coveredLifelines) {
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
		double height = getHeight(drawHandler, width);
		double topY = drawingInfo.getVerticalStart(tick) + (drawingInfo.getTickHeight(tick) - height) / 2;
		PointDouble topLeft = new PointDouble(
				drawingInfo.getHDrawingInfo(getFirstLifeline()).getSymmetricHorizontalStart(tick), topY);

		drawHandler.drawRectangle(topLeft.x, topLeft.y, width, height);
		PointDouble pentagonSize = PentagonDrawingHelper.draw(drawHandler, HEADER_TEXT, width, topLeft);
		TextSplitter.drawText(drawHandler, textLines, topLeft.x + pentagonSize.x + TEXT_X_PADDING,
				topLeft.y, width - (pentagonSize.x + TEXT_X_PADDING) * 2, height,
				AlignHorizontal.CENTER, AlignVertical.CENTER);

		for (Lifeline ll : coveredLifelines) {
			drawingInfo.getDrawingInfo(ll).addInterruptedArea(new Line1D(topLeft.y, topLeft.y + height));
		}
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		return PentagonDrawingHelper.getPentagonMinimumWidth(drawHandler, HEADER_TEXT) * 2 + TEXT_X_PADDING * 2 + TextSplitter.getTextMinWidth(textLines, drawHandler);
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler,
			HorizontalDrawingInfo hInfo, double defaultTickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		double width = hInfo.getSymmetricWidth(getFirstLifeline(), getLastLifeline(), tick);
		if (getHeight(drawHandler, width) > defaultTickHeight) {
			ret.put(tick, getHeight(drawHandler, width) - defaultTickHeight);
		}
		return ret;
	}

	@Override
	public ContainerPadding getPaddingInformation() {
		return null;
	}

	private double getHeight(DrawHandler drawHandler, double width) {
		double textWidth = width - TEXT_X_PADDING * 2 - PentagonDrawingHelper.getPentagonMinimumWidth(drawHandler, HEADER_TEXT) * 2;
		return Math.max(PentagonDrawingHelper.getHeight(drawHandler, HEADER_TEXT, width) + HEADER_BOTTOM_PADDING,
				TextSplitter.getSplitStringHeight(textLines, textWidth, drawHandler) + TEXT_Y_PADDING * 2);
	}

}
