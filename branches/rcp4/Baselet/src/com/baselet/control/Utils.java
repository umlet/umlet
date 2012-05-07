package com.baselet.control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.baselet.control.Constants.LineType;
import com.baselet.diagram.DiagramHandler;
import com.umlet.element.relation.DoubleStroke;


public abstract class Utils {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private Utils() {} // private constructor to avoid instantiation

	/**
	 * This method checks if the drawing of graphics should start at pixel (1,1) instead of (0,0) or not
	 */
	public static boolean displaceDrawingByOnePixel() {
		if (Constants.SystemInfo.JAVA_IMPL == Constants.JavaImplementation.OPEN) return true;
		else return false;
	}

	// Not used
	public static File createRandomFile(String extension) {
		File randomFile = new File(Path.homeProgram() + "tmp.diagram." + new Date().getTime() + "." + extension);
		randomFile.deleteOnExit();
		return randomFile;
	}

	public static Point normalize(Point p, int pixels) {
		Point ret = new Point();
		double d = Math.sqrt(p.x * p.x + p.y * p.y);
		ret.x = (int) (p.x / d * pixels);
		ret.y = (int) (p.y / d * pixels);
		return ret;
	}

	public static Vector<String> decomposeStringsIncludingEmptyStrings(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, false, false);
	}

	public static Vector<String> decomposeStringsWithEmptyLines(String s) {
		return Utils.decomposeStringsWFilter(s, Constants.NEWLINE, true, false);
	}

	public static Vector<String> decomposeStringsWithComments(String s) {
		return Utils.decomposeStringsWFilter(s, Constants.NEWLINE, false, true);
	}

	public static Vector<String> decomposeStrings(String s, String delimiter) {
		return Utils.decomposeStringsWFilter(s, delimiter, true, true);
	}

	public static Vector<String> decomposeStrings(String s) {
		return decomposeStrings(s, Constants.NEWLINE);
	}

	private static Vector<String> decomposeStringsWFilter(String fullString, String delimiter, boolean filterComments, boolean filterNewLines) {
		Vector<String> returnVector = new Vector<String>();
		String compatibleFullString = fullString.replaceAll("\r\n", delimiter); // compatibility to windows \r\n

		for (String line : compatibleFullString.split("\\" + delimiter)) {
			if (filterComments && (line.matches("((//)|(fg=)|(bg=)|(autoresize=)).*"))) continue;
			else if (filterNewLines && line.isEmpty()) continue;
			else returnVector.add(line);
		}

		return returnVector;
	}

	public static List<String> splitString(String text, int width) {
		DiagramHandler handler = Main.getInstance().getDiagramHandler();
		StringBuilder stringBuilder = new StringBuilder(text);
		int lastEmptyChar = -1; // is -1 if there was no ' ' in this line
		int firstCharInLine = 0;

		for (int i = 0; i < text.length(); i++) {
			if (stringBuilder.charAt(i) == ' ') {
				lastEmptyChar = i;
			}
			else if (stringBuilder.charAt(i) == '\n') {
				lastEmptyChar = -1;
				firstCharInLine = i + 1;
			}
			if ((handler.getFontHandler().getTextWidth(text.substring(firstCharInLine, i)) + 15 * handler.getZoomFactor()) > width) {
				if (lastEmptyChar != -1) {
					stringBuilder.setCharAt(lastEmptyChar, '\n');
					firstCharInLine = lastEmptyChar + 1;
					lastEmptyChar = -1;
				}
				else {
					stringBuilder.insert(i, '\n');
					firstCharInLine = i+1;
				}
			}
		}
		return Arrays.asList(stringBuilder.toString().split("\\n"));
	}

	public static String composeStrings(Vector<String> v, String delimiter) {
		String ret = null;
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				if (ret == null) {
					ret = new String(v.elementAt(i));
				}
				else {
					ret = ret + delimiter + v.elementAt(i);
				}
			}
		}
		if (ret == null) ret = "";
		return ret;
	}

	public static Stroke getStroke(LineType lineType, int lineThickness) {
		// If the lineThickness is not supported, the default type is used
		if (lineThickness < 0) lineThickness = Constants.DEFAULT_LINE_THICKNESS;

		Stroke stroke = null;
		if (lineType == LineType.SOLID) stroke = new BasicStroke(lineThickness);
		else if (lineType == LineType.DASHED) stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 8.0f, 5.0f }, 0.0f);
		else if (lineType == LineType.DOTTED) stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 1.0f, 2.0f }, 0.0f);
		else if (lineType == LineType.DOUBLE) stroke = new DoubleStroke(lineThickness, 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, null, 0.0f);
		else if (lineType == LineType.DOUBLE_DASHED) stroke = new DoubleStroke(lineThickness, 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 8.0f, 5.0f }, 0.0f);
		else if (lineType == LineType.DOUBLE_DOTTED) stroke = new DoubleStroke(lineThickness, 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 1.0f, 2.0f }, 0.0f);
		return stroke;
	}

	public static Map<RenderingHints.Key, Object> getUxRenderingQualityHigh(boolean subpixelRendering) {
		HashMap<RenderingHints.Key, Object> renderingHints = new HashMap<RenderingHints.Key, Object>();
		renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		if (subpixelRendering) renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		else renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		return renderingHints;
	}

	/**
	 * Calculates and returns the angle of the line defined by the coordinates
	 */
	public static double getAngle(double x1, double y1, double x2, double y2) {
		double res;
		double x = x2 - x1;
		double y = y2 - y1;
		res = Math.atan(y / x);
		if ((x >= 0.0) && (y >= 0.0)) res += 0.0;
		else if ((x < 0.0) && (y >= 0.0)) res += Math.PI;
		else if ((x < 0.0) && (y < 0.0)) res += Math.PI;
		else if ((x >= 0.0) && (y < 0.0)) res += 2.0 * Math.PI;
		return res;
	}

	/**
	 * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
	 * 
	 * @param colorString
	 *            String which describes the color
	 * @return Color which is related to the String or null if it is no valid colorString
	 */
	public static Color getColor(String colorString) {
		Color returnColor = null;
		for (String color : Constants.colorMap.keySet()) {
			if (colorString.equals(color)) {
				returnColor = Constants.colorMap.get(color);
				break;
			}
		}
		if (returnColor == null) {
			try {
				returnColor = Color.decode(colorString);
			} catch (NumberFormatException e) {
				//only print for debugging because message would be printed, when typing the color
				log.debug("Invalid color:" + colorString);
			}
		}
		return returnColor;
	}

	public static String getClassName() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
		//		return new RuntimeException().getStackTrace()[1].getClassName(); //ALSO POSSIBLE
	}
	
	public static Class<? extends StackTraceElement> getClassObject() {
		return Thread.currentThread().getStackTrace()[2].getClass();
	}

	/**
	 * eg: createDoubleArrayFromTo(5, 6, 0.1) = [5, 5.1, 5.2, ..., 5.9, 6] <br/>
	 * eg: createDoubleArrayFromTo(10, 20, 3) = [10, 13, 16, 19, 22] <br/>
	 * 
	 * @param min	first value of the result array
	 * @param max	if this value is reached (or passed if it's not dividable through "step") the array is finished
	 * @param step	the stepsize of the array
	 */
	public static Double[] createDoubleArrayFromTo(Double min, Double max, Double step) {
		if (min > max) return null;
		int range = (int) Math.ceil(((max-min)/step)+1);
		Double[] returnArray = new Double[range];
		for (int i = 0; i < range; i++) {
			returnArray[i] = min + i*step;
		}
		return returnArray;
	}

	public static Double[] createDoubleArrayFromTo(Double min, Double max) {
		return createDoubleArrayFromTo(min, max, 1D);
	}

	public static Color darkenColor(String inColor, int factor) {
		return darkenColor(getColor(inColor), factor);
	}

	public static Color darkenColor(Color inColor, int factor) {
		int r = Math.max(0, inColor.getRed() - factor);
		int g = Math.max(0, inColor.getGreen() - factor);
		int b = Math.max(0, inColor.getBlue() - factor);

		return new Color(r,g,b);
	}

}
