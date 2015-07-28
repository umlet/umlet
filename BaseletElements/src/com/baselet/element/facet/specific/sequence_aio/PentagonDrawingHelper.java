package com.baselet.element.facet.specific.sequence_aio;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.AdvancedTextSplitter;
import com.baselet.diagram.draw.DrawHandler;

/**
 * Helper class to draw the pentagon with text for the sequence diagram header and the combined fragments.
 * Only draws the bottom and right border of the pentagon since the top and left border should be covered by
 * the surrounding rectangle.
 *
 */
public class PentagonDrawingHelper {

	private static final double HEADER_PENTAGON_SLOPE_WIDTH = 20;
	/** how much from the overal header height is used for the slope */
	private static final double HEADER_PENTAGON_SLOPE_HEIGHT_PERCENTAGE = 0.4;
	/** minimum distance between the pentagon and the right border */
	private static final double HEADER_MIN_RIGHT_BORDER_GAP = 40;
	private static final double HEADER_TEXT_X_PADDING = 5;
	private static final double HEADER_TEXT_Y_PADDING = 3;

	/**
	 * @return how much space must be added to the text width.
	 */
	private static double getStaticWidthPadding() {
		return HEADER_TEXT_X_PADDING * 2
				+ HEADER_PENTAGON_SLOPE_WIDTH
				+ HEADER_MIN_RIGHT_BORDER_GAP;
	}

	/**
	 * @return how much space must be added to the text width.
	 */
	private static double getStaticHeightPadding() {
		return HEADER_TEXT_Y_PADDING * 2;
	}

	/**
	 * Returns the minimum width of the whole element (i.e. adding the padding and other spaces to the text width)
	 * @param drawHandler
	 * @param textLines
	 * @return the minimum width of the whole element, i.e. the minimum width of the diagram or combined fragment
	 */
	public static double getMinimumWidth(DrawHandler drawHandler, String[] textLines) {
		return AdvancedTextSplitter.getTextMinWidth(textLines, drawHandler)
				+ getStaticWidthPadding();
	}

	/**
	 * Returns the height of the pentagon (text height + padding).
	 * @param drawHandler
	 * @param textLines
	 * @param width the minimum width of the whole element, i.e. the minimum width of the diagram or combined fragment
	 * @return the minimum height of the whole header/pentagon
	 */
	public static double getHeight(DrawHandler drawHandler, String[] textLines, double width) {
		return AdvancedTextSplitter.getSplitStringHeight(textLines, width - getStaticWidthPadding(), drawHandler) + getStaticHeightPadding();
	}

	/**
	 * Draws the text, bottom and right border of the pentagon and returning the used height.
	 * @param drawHandler
	 * @param textLines
	 * @param width the minimum width of the whole element, i.e. the minimum width of the diagram or combined fragment
	 * @param topLeft
	 * @return the size (width,height) which was needed to draw the header
	 */
	public static PointDouble draw(DrawHandler drawHandler, String[] textLines, double width, PointDouble topLeft) {
		boolean splitIsNecessary = false;
		double textWidth = width - getStaticWidthPadding();
		for (String l : textLines) {
			if (AdvancedTextSplitter.splitStringAlgorithm(l, textWidth, drawHandler).length > 1) {
				splitIsNecessary = true;
				break;
			}
		}
		double height = getHeight(drawHandler, textLines, width); //
		// if no split is needed we can reduce the width of the title to the width of the largest line
		if (!splitIsNecessary) {
			textWidth = 0;
			for (String l : textLines) {
				textWidth = Math.max(textWidth, drawHandler.textWidth(l));
			}
			textWidth += drawHandler.textWidth("n");
		}
		AdvancedTextSplitter.drawText(drawHandler, textLines, topLeft.x + HEADER_TEXT_X_PADDING, topLeft.y, textWidth,
				height, AlignHorizontal.CENTER, AlignVertical.CENTER);
		LineType oldLt = drawHandler.getLineType();
		drawHandler.setLineType(LineType.SOLID);
		drawHandler.drawLines(new PointDouble[] {
				new PointDouble(topLeft.x, topLeft.y + height),
				new PointDouble(topLeft.x + textWidth + HEADER_TEXT_X_PADDING * 2, topLeft.y + height),
				new PointDouble(topLeft.x + textWidth + HEADER_TEXT_X_PADDING * 2 + HEADER_PENTAGON_SLOPE_WIDTH,
						topLeft.y + height * (1 - HEADER_PENTAGON_SLOPE_HEIGHT_PERCENTAGE)),
				new PointDouble(topLeft.x + textWidth + HEADER_TEXT_X_PADDING * 2 + HEADER_PENTAGON_SLOPE_WIDTH, topLeft.y) });
		drawHandler.setLineType(oldLt);
		return new PointDouble(textWidth + HEADER_TEXT_X_PADDING * 2 + HEADER_PENTAGON_SLOPE_WIDTH, height);
	}
}
