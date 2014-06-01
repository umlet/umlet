package com.baselet.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;

public class SharedUtils {

	private static final Logger log = Logger.getLogger(SharedUtils.class);

	public static int realignToGrid(double d) {
		return realignTo(true, d, false, SharedConstants.DEFAULT_GRID_SIZE);
	}

	/**
	 * realigns a rectangle to the grid enlarging any side if necessary (eg: x=9, width=10, x2=19 will be realigned to x=0, width=20, x2=20)
	 * 
	 * @param rectangle rectangle to realign
	 * @return a realigned copy of the input rectangle
	 */
	public static Rectangle realignToGrid(Rectangle rectangle) {
		int x = realignToGrid(false, rectangle.getX(), false);
		int y = realignToGrid(false, rectangle.getY(), false);
		int width = realignToGrid(false, rectangle.getX() - x + rectangle.getWidth(), true); // IMPORTANT: the difference from original x and realigned x must be added, otherwise the upper example would return x=0, width=10, x2=10. x2 would be too small
		int height = realignToGrid(false, rectangle.getY() - y + rectangle.getHeight(), true);
		return new Rectangle(x, y, width, height);
	}

	/**
	 * rounds eg: 5 to 10, 4 to 0, -5 to -10, -4 to 0
	 */
	public static int realignToGridRoundToNearest(boolean logRealign, double val) {
		boolean roundUp;
		if (Math.abs(val % SharedConstants.DEFAULT_GRID_SIZE) < SharedConstants.DEFAULT_GRID_SIZE / 2) {
			roundUp = val < 0;
		}
		else {
			roundUp = val >= 0;
		}
		return realignTo(logRealign, val, roundUp, SharedConstants.DEFAULT_GRID_SIZE);
	}

	public static int realignToGrid(boolean logRealign, double val, boolean roundUp) {
		return realignTo(logRealign, val, roundUp, SharedConstants.DEFAULT_GRID_SIZE);
	}

	/**
	 * returns the integer which is nearest to val but on the grid (round down)
	 * 
	 * @param logRealign
	 *            if true a realign is logged as an error
	 * @param val
	 *            value which should be rounded to the grid
	 * @param roundUp
	 *            if true the realign rounds up instead of down
	 * @return value on the grid
	 */
	public static int realignTo(boolean logRealign, double val, boolean roundUp, int gridSize) {
		double alignedVal = val;
		double mod = val % gridSize;
		if (mod != 0) {
			alignedVal -= mod; // ExampleA: 14 - 4 = 10 // ExampleB: -14 - -4 = -10 // (positive vals get round down, negative vals get round up)
			if (val > 0 && roundUp) { // eg ExampleA: 10 + 10 = 20 (for positive vals roundUp must be specifically handled by adding gridSize)
				alignedVal += gridSize;
			}
			if (val < 0 && !roundUp) { // eg ExampleB: -10 - 10 = -20 (for negative vals roundDown must be specifically handled by subtracting gridSize)
				alignedVal -= gridSize;
			}
			if (logRealign) {
				log.error("realignToGrid from " + val + " to " + alignedVal);
			}
		}
		return (int) alignedVal;
	}

	public static Rectangle getGridElementsRectangle(Collection<GridElement> gridElements) {
		int x = Integer.MAX_VALUE;
		int y = Integer.MAX_VALUE;
		int x2 = Integer.MIN_VALUE;
		int y2 = Integer.MIN_VALUE;
		for (GridElement ge : gridElements) {
			x = Math.min(ge.getRectangle().getX(), x);
			y = Math.min(ge.getRectangle().getY(), y);
			x2 = Math.max(ge.getRectangle().getX2(), x2);
			y2 = Math.max(ge.getRectangle().getY2(), y2);
		}
		return new Rectangle(x, y, x2 - x, y2 - y);
	}

	public static String listToString(String sep, Collection<?> list) {
		return listToStringHelper(new StringBuilder(), sep, list).toString();
	}

	private static StringBuilder listToStringHelper(StringBuilder sb, String sep, Collection<?> list) {
		for (Object line : list) {
			sb.append(line).append(sep);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - sep.length());
		}
		return sb;
	}

	public static String mapToString(String mapSep, String listSep, Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<?, ?> e : map.entrySet()) {
			sb.append(e.getKey()).append(": ");
			if (e.getValue() instanceof Collection<?>) {
				listToStringHelper(sb, listSep, (Collection<?>) e.getValue());
			}
			else {
				sb.append(e.getValue().toString());
			}
			sb.append(mapSep);
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - mapSep.length());
		}
		return sb.toString();
	}

	public static <T> List<T> mergeLists(List<T> listA, List<T> listB, List<T> listC) {
		List<T> returnList = new ArrayList<T>(listA);
		returnList.addAll(listB);
		returnList.addAll(listC);
		return Collections.unmodifiableList(returnList);
	}

}
