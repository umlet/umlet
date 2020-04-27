package com.baselet.control.util;

import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.constants.Constants;
import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DoubleStroke;

public abstract class Utils {

	private Utils() {} // private constructor to avoid instantiation

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

	// TODO: Decomposing should be moved to Properties.class. At the moment OldGridElement uses this method and NewGridElement the one in Properties.class
	private static Vector<String> decomposeStringsWFilter(String fullString, String delimiter, boolean filterComments, boolean filterNewLines) {
		Vector<String> returnVector = new Vector<String>();
		String compatibleFullString = fullString.replaceAll("\r\n", delimiter); // compatibility to windows \r\n

		for (String line : compatibleFullString.split("\\" + delimiter)) {
			if (filterComments && line.matches("((//)|(fg=)|(bg=)|(autoresize=)|(layer=)|(group=)).*")) {
				continue;
			}
			else if (filterNewLines && line.isEmpty()) {
				continue;
			}
			else {
				returnVector.add(line);
			}
		}

		return returnVector;
	}

	public static String composeStrings(Vector<String> v, String delimiter) {
		String ret = null;
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				if (ret == null) {
					ret = v.elementAt(i);
				}
				else {
					ret = ret + delimiter + v.elementAt(i);
				}
			}
		}
		if (ret == null) {
			ret = "";
		}
		return ret;
	}

	public static Stroke getStroke(LineType lineType, float lineThickness) {
		// If the lineThickness is not supported, the default type is used
		if (lineThickness < 0) {
			lineThickness = (float) FacetConstants.LINE_WIDTH_DEFAULT;
		}

		Stroke stroke = null;
		if (lineType == LineType.SOLID) {
			stroke = new BasicStroke(lineThickness);
		}
		else if (lineType == LineType.DASHED) {
			stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 8.0f, 5.0f }, 0.0f);
		}
		else if (lineType == LineType.DOTTED) {
			stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 1.0f, 2.0f }, 0.0f);
		}
		else if (lineType == LineType.DOUBLE) {
			stroke = new DoubleStroke(lineThickness, 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, null, 0.0f);
		}
		else if (lineType == LineType.DOUBLE_DASHED) {
			stroke = new DoubleStroke(lineThickness, 4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 8.0f, 5.0f }, 0.0f);
		}
		else if (lineType == LineType.DOUBLE_DOTTED) {
			stroke = new DoubleStroke(lineThickness, 3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, new float[] { 1.0f, 2.0f }, 0.0f);
		}
		return stroke;
	}

	public static Map<RenderingHints.Key, Object> getUxRenderingQualityHigh(boolean subpixelRendering) {
		HashMap<RenderingHints.Key, Object> renderingHints = new HashMap<RenderingHints.Key, Object>();
		renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		if (subpixelRendering) {
			renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		else {
			renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		}
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
		if (x >= 0.0 && y >= 0.0) {
			res += 0.0;
		}
		else if (x < 0.0 && y >= 0.0) {
			res += Math.PI;
		}
		else if (x < 0.0 && y < 0.0) {
			res += Math.PI;
		}
		else if (x >= 0.0 && y < 0.0) {
			res += 2.0 * Math.PI;
		}
		return res;
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
		if (min > max) {
			return null;
		}
		int range = (int) Math.ceil((max - min) / step + 1);
		Double[] returnArray = new Double[range];
		for (int i = 0; i < range; i++) {
			returnArray[i] = min + i * step;
		}
		return returnArray;
	}

	public static Double[] createDoubleArrayFromTo(Double min, Double max) {
		return createDoubleArrayFromTo(min, max, 1D);
	}

	public static class BuildInfo {
		public String version;
		public String buildtime;
	}

	/**
	 * Read build info from the BuildInfo.propreties file at the root of the classpath
	 */
	public static BuildInfo readBuildInfo() {
		InputStream stream = Utils.class.getResourceAsStream("/BuildInfo.properties");
		if (stream == null) {
			throw new RuntimeException("Cannot load BuildInfo.properties");
		}
		else {
			Properties prop = new Properties();
			try {
				prop.load(stream);
				BuildInfo result = new BuildInfo();
				result.version = prop.getProperty("version");
				result.buildtime = prop.getProperty("buildtime");
				return result;
			} catch (IOException e) {
				throw new RuntimeException("Cannot load properties from file BuildInfo.properties", e);
			} finally {
				try {
					stream.close();
				} catch (IOException e) {/* nothing to do */}
			}
		}
	}
}
