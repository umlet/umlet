package com.baselet.control;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.swing.Converter;
import com.baselet.element.GridElement;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.Relation;
import com.umlet.element.relation.DoubleStroke;
import com.umlet.element.relation.RelationLinePoint;


public abstract class Utils {

	private Utils() {} // private constructor to avoid instantiation

	/**
	 * This method checks if the drawing of graphics should start at pixel (1,1) instead of (0,0) or not
	 */
	public static boolean displaceDrawingByOnePixel() {
		return Constants.SystemInfo.JAVA_IMPL == Constants.JavaImplementation.OPEN;
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

	//TODO: Decomposing should be moved to Properties.class. At the moment OldGridElement uses this method and NewGridElement the one in Properties.class
	private static Vector<String> decomposeStringsWFilter(String fullString, String delimiter, boolean filterComments, boolean filterNewLines) {
		Vector<String> returnVector = new Vector<String>();
		String compatibleFullString = fullString.replaceAll("\r\n", delimiter); // compatibility to windows \r\n

		for (String line : compatibleFullString.split("\\" + delimiter)) {
			if (filterComments && (line.matches("((//)|(fg=)|(bg=)|(autoresize=)|(layer=)).*"))) continue;
			else if (filterNewLines && line.isEmpty()) continue;
			else returnVector.add(line);
		}

		return returnVector;
	}

	@Deprecated //TODO remove after CustomElements are converted to NewGridElement
	public static List<String> splitString(String text, float width, DiagramHandler handler) {
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
			if ((handler.getFontHandler().getTextWidth(text.substring(firstCharInLine, i), false)) + 15 > width) {
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

	public static Stroke getStroke(LineType lineType, float lineThickness) {
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

	public static String replaceFileExtension(String fileName, String oldExtension, String newExtension) {
		if (fileName.endsWith("." + oldExtension)) fileName = fileName.substring(0, fileName.indexOf("." + oldExtension));
		return fileName + "." + newExtension;
	}
	
	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 * IMPORTANT: on overlapping elements, contains is called for all elements until the first one returns true, then the others contain methods are not called
	 */
	public static boolean contains(GridElement gridElement, Point p) {
		JComponent component = ((JComponent) gridElement.getComponent());
		java.awt.Rectangle rectangle = component.getVisibleRect();
		if (!rectangle.contains(p.x, p.y)) return false;

		for (GridElement other : Main.getHandlerForElement(gridElement).getDrawPanel().getAllEntitiesNotInGroup()) {
			JComponent otherComponent = ((JComponent) other.getComponent());
			if (other instanceof Relation) { // a relation is always on top
				// move point to coordinate system of other entity
				Point other_p = new Point(p.x + gridElement.getRectangle().x - other.getRectangle().x, p.y + gridElement.getRectangle().y - other.getRectangle().y);
				if (otherComponent.contains(Converter.convert(other_p))) return false;
			}

			// If the this visibleRect is equal to the other VisibleRect, true will be returned. Otherwise we need to check further
			else if (!component.getVisibleRect().equals(otherComponent.getVisibleRect())) {
				java.awt.Rectangle other_rectangle = otherComponent.getVisibleRect();
				// move bounds to coordinate system of this component
				other_rectangle.x += other.getRectangle().x - gridElement.getRectangle().x;
				other_rectangle.y += other.getRectangle().y - gridElement.getRectangle().y;
				if (other_rectangle.contains(p.x, p.y)) { 
					// when elements intersect, select the smaller element
					if (rectangle.intersects(other_rectangle) && smaller(other_rectangle, rectangle)) return false; 
				}
			}
		}
		return true;
	}

	private static boolean smaller(java.awt.Rectangle a, java.awt.Rectangle b) {
		int areaA = a.getSize().height * a.getSize().width;
		int areaB = b.getSize().height * b.getSize().width;
		if (areaA < areaB) return true;
		return false;
	}

	public static DimensionDouble getTextSize(String s, Font font, FontRenderContext frc) {
		if (s == null) {
			return null;
		} else if (s.isEmpty()) {
			return new DimensionDouble(0, 0);
		}

		// TextLayout trims the string, therefore replace spaces with dots in such cases (dots have exactly the same width as spaces, therefore we will get the expected width WITH spaces)
		if (s.startsWith(" ") || s.endsWith(" ")) {
			s = s.replace(' ', '.');
		}
		TextLayout tl = new TextLayout(s, font, frc);
		return new DimensionDouble((int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
	}


	public static Vector<RelationLinePoint> getStickingRelationLinePoints(DiagramHandler handler, StickingPolygon stickingPolygon) {
		Vector<RelationLinePoint> lpts = new Vector<RelationLinePoint>();
		Collection<Relation> rels = handler.getDrawPanel().getAllRelations();
		for (Relation r : rels) {
			if (!r.isPartOfGroup()) {
				PointDouble l1 = r.getAbsoluteCoorStart();
				PointDouble l2 = r.getAbsoluteCoorEnd();
				int c1 = stickingPolygon.isConnected(l1, handler.getGridSize());
				int c2 = stickingPolygon.isConnected(l2, handler.getGridSize());
				if (c1 >= 0) lpts.add(new RelationLinePoint(r, 0, c1));
				if (c2 >= 0) lpts.add(new RelationLinePoint(r, r.getLinePoints().size() - 1, c2));
			}
		}
		return lpts;
	}
	
}
