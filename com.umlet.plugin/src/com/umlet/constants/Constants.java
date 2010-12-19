// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.constants;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.UIManager;

// class in extra package because of security of custom elements
public class Constants {
	/**** CONSTANTS LOADED FROM CONFIG ****/
	public static int defaultFontsize = 14;
	public static boolean start_maximized = false;
	public static boolean show_stickingpolygon = true;
	public static String ui_manager = UIManager.getSystemLookAndFeelClassName();
	public static int main_split_position = 600;
	public static int right_split_position = 400;
	public static Dimension umlet_size = new Dimension(960, 750);
	public static Point umlet_location = new Point(5, 5);

	/**************************************/

	public static String newline = "\n";
	public static final String defaultHelpText;

	// Text formatting labels
	public static final String underlineLabel = "_";
	public static final String boldLabel = "*";
	public static final String italicLabel = "/";

	// init values
	static {
		String ctrl = "Ctrl";
		if (System.getProperty("os.name").toUpperCase().startsWith("MAC")) {
			Constants.CTRLMETA_MASK = ActionEvent.META_MASK;
			Constants.CTRLMETA_DOWN_MASK = InputEvent.META_DOWN_MASK;
			ctrl = "\u2318";
		}
		else {
			Constants.CTRLMETA_MASK = ActionEvent.CTRL_MASK;
			Constants.CTRLMETA_DOWN_MASK = InputEvent.CTRL_DOWN_MASK;
		}

		defaultHelpText =
				"// Uncomment the following line to change the fontsize:" + newline +
				"// fontsize=14" + newline +
				"" + newline +
				"" + newline +
				"//////////////////////////////////////////////////////////////////////////////////////////////" + newline +
				"// Welcome to UMLet!" + newline +
				"//" + newline +
				"// Double-click on UML elements to add them to the diagram, or to copy them" + newline +
				"// Edit elements by modifying the text in this panel" + newline +
				"// Hold " + ctrl + " to select multiple elements" + newline +
				"// Use " + ctrl + "+mouse to select via lasso" + newline +
				"//" + newline +
				"// Use ± or " + ctrl + "+mouse wheel to zoom" + newline +
				"// Drag a whole relation at its central square icon" + newline +
				"//" + newline +
				"// Press " + ctrl + "+C to copy the whole diagram to the system clipboard (then just paste it to, eg, Word)" + newline +
				"// Edit the files in the \"palettes\" directory to create your own element palettes" + newline +
				"//" + newline +
				"// Select \"Custom Elements > New...\" to create new element types" + newline +
				"//////////////////////////////////////////////////////////////////////////////////////////////" + newline +
				"" + newline +
				"" + newline +
				"// This text will be stored with each diagram;  use it for notes.";
	}

	// CTRL key (for meta key enabling on mac)
	public static int CTRLMETA_MASK;
	public static int CTRLMETA_DOWN_MASK;

	public final static int defaultGridSize = 10;

	public static int printpadding = 20;

	public static final int Custom_Element_Compile_Interval = 500;
	public static int LEFT = 0, RIGHT = 2, CENTER = 1, TOP = 0, BOTTOM = 2;

	public static final String noautoresize = "autoresize=false";
	public static final String autoresize = "autoresize=";

	public static final Cursor lrCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static final Cursor tbCursor = new Cursor(Cursor.N_RESIZE_CURSOR);
	public static final Cursor nwCursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public static final Cursor neCursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public static final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	public static final Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	public static final Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static final Cursor textCursor = new Cursor(Cursor.TEXT_CURSOR);

	public static final String DELIMITER_ENTITIES = "~~~~~|||||~~~~~|||||";
	public static final String DELIMITER_STATE_AND_HIDDEN_STATE = "/////<<<<</////<<<<<";
	public static final String DELIMITER_FIELDS = "#####_____#####_____";
	public static final String DELIMITER_ADDITIONAL_ATTRIBUTES = ";";
	public static int INTERFACE_LINE_LENGTH = 40;

	public static int RESIZE_TOP = 1;
	public static int RESIZE_RIGHT = 2;
	public static int RESIZE_BOTTOM = 4;
	public static int RESIZE_LEFT = 8;

	public static Point normalize(Point p, int pixels) {
		Point ret = new Point();
		double d = Math.sqrt(p.x * p.x + p.y * p.y);
		ret.x = (int) (p.x / d * pixels);
		ret.y = (int) (p.y / d * pixels);
		return ret;
	}

	public static Vector<String> decomposeStringsWithEmptyLines(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, true, false);
	}

	public static Vector<String> decomposeStrings(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, true, true);
	}

	public static Vector<String> decomposeStringsWithComments(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, false, true);
	}

	public static Vector<String> decomposeStrings(String s) {
		return decomposeStrings(s, newline);
	}

	private static Vector<String> decomposeStringsWFilter(String s, String delimiter, boolean filterComments, boolean filterNewLines) {
		s = s.replaceAll("\r\n", delimiter); // compatibility to windows \r\n
		Vector<String> ret = new Vector<String>();
		for (;;) {
			int index = s.indexOf(delimiter);
			if (index < 0) {
				if (filterComments) {
					s = filterComment(s);
					if (s.startsWith("bg=") || s.startsWith("fg=") ||
							s.startsWith(Constants.autoresize)) s = ""; // filter color-setting strings

				}
				if (!s.equals("") || !filterNewLines) {
					ret.add(s);
				}
				return ret;
			}
			String tmp = s.substring(0, index);
			if (filterComments) {
				tmp = filterComment(tmp);
				if (tmp.startsWith("bg=") || tmp.startsWith("fg=") ||
						s.startsWith(Constants.autoresize)) tmp = ""; // filter color-setting strings
			}

			if (!tmp.equals("") || !filterNewLines) ret.add(tmp);
			s = s.substring(index + delimiter.length(), s.length());
		}
	}

	private static String filterComment(String s) {

		int pos = s.indexOf("//");
		char c;
		while (pos >= 0) {
			if (pos == 0) return "";
			c = s.charAt(pos - 1);
			if (s.length() > pos + 2) {
				if ((s.charAt(pos + 2) != '/') && (c != '/') && (c != ':')) return s.substring(0, pos);
			}
			else if ((c != '/') && (c != ':')) return s.substring(0, pos);

			pos = s.indexOf("//", pos + 1);
		}
		return s;
	}

	public static Vector<String> decomposeStringsIncludingEmptyStrings(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, false, false);
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

	public static BasicStroke getStroke(int strokeType, int lineThickness) {
		BasicStroke stroke = null;
		switch (strokeType) {
			case 0:
				stroke = new BasicStroke(lineThickness);
				break;
			case 1:
				float dash1[] = { 8.0f, 5.0f };
				stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash1, 0.0f);
				break;
			case 2:
				float dash2[] = { 1.0f, 2.0f };
				stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash2, 0.0f);
				;
			case 3:
				break;
		}
		return stroke;
	}

	public static Map<RenderingHints.Key, Object> getUxRenderingQualityHigh() {
		HashMap<RenderingHints.Key, Object> renderingHints = new HashMap<RenderingHints.Key, Object>();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		return renderingHints;
	}

	// public static Map<RenderingHints.Key, Object> getUxRenderingQualityLow() {
	// HashMap<RenderingHints.Key, Object> renderingHints = new HashMap<RenderingHints.Key, Object>();
	// return renderingHints;
	// }

	/**
	 * Calculates the angle of the line defined by the koordinates
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return angle (radians)
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

}
