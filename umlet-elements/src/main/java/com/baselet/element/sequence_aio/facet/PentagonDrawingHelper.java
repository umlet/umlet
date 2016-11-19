package com.baselet.element.sequence_aio.facet;

import java.util.Collection;
import java.util.Collections;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

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
		return TextSplitter.getTextMinWidth(textLines, drawHandler)
				+ getStaticWidthPadding();
	}

	/**
	 * Returns the minimum width of the whole pentagon (i.e. adding the padding and other spaces to the text width but not the border gap)
	 * @param drawHandler
	 * @param textLines
	 * @return the minimum width of the whole element, i.e. the minimum width of the diagram or combined fragment
	 */
	public static double getPentagonMinimumWidth(DrawHandler drawHandler, String[] textLines) {
		return TextSplitter.getTextMinWidth(textLines, drawHandler)
				+ getStaticWidthPadding() - HEADER_MIN_RIGHT_BORDER_GAP;
	}

	/**
	 * Returns the height of the pentagon (text height + padding).
	 * @param drawHandler
	 * @param textLines
	 * @param width the minimum width of the whole element, i.e. the minimum width of the diagram or combined fragment
	 * @return the minimum height of the whole header/pentagon
	 */
	public static double getHeight(DrawHandler drawHandler, String[] textLines, double width) {
		return TextSplitter.getSplitStringHeight(textLines, width - getStaticWidthPadding(), drawHandler) + getStaticHeightPadding();
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
		return draw(drawHandler, textLines, width, topLeft, Collections.<Line1D> emptyList());
	}

	/**
	 * Draws the text, bottom and right border of the pentagon and returning the used height.
	 * @param drawHandler
	 * @param textLines
	 * @param width the minimum width of the whole element, i.e. the minimum width of the diagram or combined fragment
	 * @param topLeft
	 * @param slopeNotPermittedAreas X-Intervals on which the slope can not be drawn in absolute values,
	 * i.e. the slope starts at the end of an interval if it would reach into one
	 * @return the size (width,height) which was needed to draw the header
	 */
	public static PointDouble draw(DrawHandler drawHandler, String[] textLines, double width, PointDouble topLeft,
			Collection<Line1D> slopeNotPermittedAreas) {
		boolean splitIsNecessary = false;
		double textWidth = width - getStaticWidthPadding();
		for (String l : textLines) {
			if (TextSplitter.splitStringAlgorithm(l, textWidth, drawHandler).length > 1) {
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
		TextSplitter.drawText(drawHandler, textLines, topLeft.x + HEADER_TEXT_X_PADDING, topLeft.y, textWidth,
				height, AlignHorizontal.LEFT, AlignVertical.CENTER);
		LineType oldLt = drawHandler.getLineType();
		drawHandler.setLineType(LineType.SOLID);
		double slopeStartX = topLeft.x + textWidth + HEADER_TEXT_X_PADDING * 2;
		double slopeEndX = slopeStartX + HEADER_PENTAGON_SLOPE_WIDTH;
		// based on the slope widht and the Lifeline gaps and the fact that the execution specification
		// is very small on the left side it should be enough to jump once
		for (Line1D line : slopeNotPermittedAreas) {
			if (line.getLow() > slopeEndX) {
				break;
			}
			else if (line.isIntersecting(new Line1D(slopeStartX, slopeEndX))) {
				slopeStartX = line.getHigh();
				slopeEndX = slopeStartX + HEADER_PENTAGON_SLOPE_WIDTH;
				break;
			}
		}
		drawHandler.drawLines(new PointDouble[] {
				new PointDouble(topLeft.x, topLeft.y + height),
				new PointDouble(slopeStartX, topLeft.y + height),
				new PointDouble(slopeEndX, topLeft.y + height * (1 - HEADER_PENTAGON_SLOPE_HEIGHT_PERCENTAGE)),
				new PointDouble(slopeEndX, topLeft.y) });
		drawHandler.setLineType(oldLt);
		return new PointDouble(slopeEndX - topLeft.x, height);
	}
}
