package com.baselet.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;

public class SharedUtils {

	private static final Logger log = LoggerFactory.getLogger(SharedUtils.class);

	public static int realignToGrid(double d) {
		return realignTo(true, d, false, SharedConstants.DEFAULT_GRID_SIZE);
	}

	/**
	 * realigns a rectangle to the grid enlarging any side if necessary (eg: x=9, width=10, x2=19 will be realigned to x=0, width=20, x2=20)
	 *
	 * @param rectangle rectangle to realign
	 * @return a realigned copy of the input rectangle
	 * @param realignHeightAndWidth if true height and width are also realigned if necessary
	 */
	public static Rectangle realignToGrid(Rectangle rectangle, boolean realignHeightAndWidth) {
		int x = realignToGrid(false, rectangle.getX(), false);
		int y = realignToGrid(false, rectangle.getY(), false);
		if (realignHeightAndWidth) {
			int width = realignToGrid(false, rectangle.getX() - x + rectangle.getWidth(), true); // IMPORTANT: the difference from original x and realigned x must be added, otherwise the upper example would return x=0, width=10, x2=10. x2 would be too small
			int height = realignToGrid(false, rectangle.getY() - y + rectangle.getHeight(), true);
			return new Rectangle(x, y, width, height);
		}
		else {
			return new Rectangle(x, y, rectangle.getWidth(), rectangle.getHeight());
		}
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

	public static String mapToString(Map<?, ?> map) {
		return mapToString("\n", ",", map);
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

	public static Double[][] cloneArray(Double[][] src) {
		int length = src.length;
		Double[][] target = new Double[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}

	public static String[] cloneArray(String[] src) {
		String[] target = new String[src.length];
		System.arraycopy(src, 0, target, 0, src.length);
		return target;
	}

	public static int[] cloneArray(int[] src) {
		int[] target = new int[src.length];
		System.arraycopy(src, 0, target, 0, src.length);
		return target;
	}

	/**
	 * if the user types "\n" it should be translated to a linebreak. He can avoid this by typing \\n (escaping the linebreak)
	 */
	public static String[] splitAtLineEndChar(String text) {
		String rep = StringStyle.replaceNotEscaped(text, "\\n", "\n");
		if (rep.contains("\n")) {
			String[] split = rep.split("\n"); // split uses a RegEx therefore escape the linebreak char
			return split;
		}
		return new String[] { text };
	}

}
