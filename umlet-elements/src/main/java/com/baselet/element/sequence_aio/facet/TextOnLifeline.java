package com.baselet.element.sequence_aio.facet;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

public class TextOnLifeline implements LifelineOccurrence {
	/** the minimum width, to avoid splitting text with very short words e.g. print "I am I" in one line instead of 3*/
	private static final double MIN_WIDTH = 50;

	private final String[] lines;

	/**
	 *
	 * @param text the text, lines need to be separated by \n
	 */
	public TextOnLifeline(String text) {
		super();
		lines = text.split("\n");
	}

	@Override
	public Line1D draw(DrawHandler drawHandler, PointDouble topLeft, PointDouble size) {
		double height = TextSplitter.getSplitStringHeight(lines, size.x, drawHandler);
		double topY = topLeft.y + (size.y - height) / 2;
		Line1D interruptedLifeline = new Line1D(topY, topY + height);
		TextSplitter.drawText(drawHandler, lines, topLeft.x, topY, size.x, height,
				AlignHorizontal.CENTER, AlignVertical.CENTER);
		return interruptedLifeline;

	}

	@Override
	public double getMinWidth(DrawHandler drawHandler) {
		return Math.max(MIN_WIDTH, TextSplitter.getTextMinWidth(lines, drawHandler));
	}

	@Override
	public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
		return TextSplitter.getSplitStringHeight(lines, size.x, drawHandler) - size.y;
	}

}
