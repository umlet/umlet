// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.constants;

import java.awt.BasicStroke;
import java.awt.Color;
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

import com.umlet.control.Umlet;
import com.umlet.gui.eclipse.EclipseGUI;

// class in extra package because of security of custom elements
public class Constants {

	/**** PLATTFORM AND JAVA SPECIFIC SETTINGS ****/
	public enum Os {
		WINDOWS, LINUX, UNIX, MAC, UNKNOWN
	}

	public enum JavaImplementation {
		OPEN, SUN
	}

	public enum UmletType {
		STANDALONE, ECLIPSE_PLUGIN
	}

	public static final Os OS;
	public static final JavaImplementation JAVAIMPL;
	public static final UmletType UMLETTYPE;

	static {
		// Initialize OS, JAVAIMPL AND UMLETTYPE variable
		if (System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) OS = Os.WINDOWS;
		else if (System.getProperty("os.name").toUpperCase().startsWith("MAC")) OS = Os.MAC;
		else if (System.getProperty("os.name").toUpperCase().startsWith("LINUX")) OS = Os.LINUX;
		else OS = Os.UNKNOWN;

		if (System.getProperty("java.runtime.name").toUpperCase().contains("OPEN")) JAVAIMPL = JavaImplementation.OPEN;
		else JAVAIMPL = JavaImplementation.SUN;

		if (Umlet.getInstance().getGUI() instanceof EclipseGUI) UMLETTYPE = UmletType.ECLIPSE_PLUGIN;
		else UMLETTYPE = UmletType.STANDALONE;
	}

	/**
	 * This method checks if the drawing of graphics should start at pixel (1,1) instead of (0,0) or not
	 */
	public static boolean displaceDrawingByOnePixel() {
		if (JAVAIMPL == JavaImplementation.OPEN) return true;
		else return false;
	}

	public static boolean isOkButtonOnLeftSide() {
		if (OS == Os.WINDOWS) return true;
		else return false;
	}

	// public static final ArrayList<JavaImplementation> displaceDrawingByOnePixel = new ArrayList<JavaImplementation>();
	// // A list with every os which uses Meta instead of Ctrl
	// public static final ArrayList<Os> replaceCtrlWithMeta = new ArrayList<Os>();
	// static {
	// displaceDrawingByOnePixel.add(JavaImplementation.OPEN);
	// replaceCtrlWithMeta.add(Os.MAC);
	// }

	/**** CTRL KEY - FOR META KEY ENABLING ON MAC ****/
	public final static String CTRLNAME;
	public final static int CTRLMETA_MASK;
	public final static int CTRLMETA_DOWN_MASK;

	static {
		if (OS == Os.MAC) {
			CTRLMETA_MASK = ActionEvent.META_MASK;
			CTRLMETA_DOWN_MASK = InputEvent.META_DOWN_MASK;
			CTRLNAME = "\u2318";
		}
		else {
			CTRLMETA_MASK = ActionEvent.CTRL_MASK;
			CTRLMETA_DOWN_MASK = InputEvent.CTRL_DOWN_MASK;
			CTRLNAME = "Ctrl";
		}
	}

	/**** NEWLINE CHARACTER AND DEFAULT HELP- AND MAILTEXT ****/
	public static final String NEWLINE = "\n";
	public static final String DEFAULT_HELPTEXT;
	public static final String DEFAULT_MAILTEXT;

	static {
		DEFAULT_HELPTEXT =
				"// Uncomment the following line to change the fontsize:" + NEWLINE +
				"// fontsize=14" + NEWLINE +
				"" + NEWLINE +
				"" + NEWLINE +
				"//////////////////////////////////////////////////////////////////////////////////////////////" + NEWLINE +
				"// Welcome to UMLet!" + NEWLINE +
				"//" + NEWLINE +
				"// Double-click on UML elements to add them to the diagram, or to copy them" + NEWLINE +
				"// Edit elements by modifying the text in this panel" + NEWLINE +
				"// Hold " + CTRLNAME + " to select multiple elements" + NEWLINE +
				"// Use " + CTRLNAME + "+mouse to select via lasso" + NEWLINE +
				"//" + NEWLINE +
				"// Use ± or " + CTRLNAME + "+mouse wheel to zoom" + NEWLINE +
				"// Drag a whole relation at its central square icon" + NEWLINE +
				"//" + NEWLINE +
				"// Press " + CTRLNAME + "+C to copy the whole diagram to the system clipboard (then just paste it to, eg, Word)" + NEWLINE +
				"// Edit the files in the \"palettes\" directory to create your own element palettes" + NEWLINE +
				"//" + NEWLINE +
				"// Select \"Custom Elements > New...\" to create new element types" + NEWLINE +
				"//////////////////////////////////////////////////////////////////////////////////////////////" + NEWLINE +
				"" + NEWLINE +
				"" + NEWLINE +
				"// This text will be stored with each diagram;  use it for notes.";

		DEFAULT_MAILTEXT =
				"Type your message here.." + NEWLINE +
				"" + NEWLINE +
				"__" + NEWLINE +
				"To edit the diagram, open the attached uxf-file with the free UML tool UMLet (http://www.umlet.com)";
	}

	/**** TEXT FORMATTING LABELS ****/
	public static class FormatLabels {
		public static final String UNDERLINE = "_";
		public static final String BOLD = "*";
		public static final String ITALIC = "/";
	}

	/**** OTHER CONSTANTS ****/
	public static final int DEFAULTGRIDSIZE = 10;
	public static final int PRINTPADDING = 20;
	public static final int INTERFACE_LINE_LENGTH = 40;

	public static final int CUSTOM_ELEMENT_COMPILE_INTERVAL = 500;

	public static final int LEFT = 0, RIGHT = 2, CENTER = 1, TOP = 0, BOTTOM = 2;
	public static final int RESIZE_TOP = 1, RESIZE_RIGHT = 2, RESIZE_BOTTOM = 4, RESIZE_LEFT = 8;

	public static final String NOAUTORESIZE = "autoresize=false";
	public static final String AUTORESIZE = "autoresize=";

	public static final Cursor LR_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static final Cursor TB_CURSOR = new Cursor(Cursor.N_RESIZE_CURSOR);
	public static final Cursor NW_CURSOR = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public static final Cursor NE_CURSOR = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
	public static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);
	public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor CROSS_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static final Cursor TEXT_CURSOR = new Cursor(Cursor.TEXT_CURSOR);

	public static final String DELIMITER_ENTITIES = "~~~~~|||||~~~~~|||||";
	public static final String DELIMITER_STATE_AND_HIDDEN_STATE = "/////<<<<</////<<<<<";
	public static final String DELIMITER_FIELDS = "#####_____#####_____";
	public static final String DELIMITER_ADDITIONAL_ATTRIBUTES = ";";

	public static final int PASTE_DISPLACEMENT = 20;
	public static final String FONT = "SansSerif";
	public static final int MIN_MAIL_SPLIT_POSITION = 100;
	public static final Color GRID_COLOR = new Color(235, 235, 235);

	/**** CONFIG FILENAME ****/
	public static final String configFilename;
	static {
		if (UMLETTYPE == UmletType.STANDALONE) configFilename = "umlet.cfg";
		else if (UMLETTYPE == UmletType.ECLIPSE_PLUGIN) configFilename = "umletplugin.cfg";
		else configFilename = "";
	}

	/**** VALUES LOADED FROM CONFIG ****/
	public static int defaultFontsize = 14;
	public static boolean start_maximized = false;
	public static boolean show_stickingpolygon = true;
	public static boolean show_grid = false;
	public static int main_split_position = 600;
	public static int right_split_position = 400;
	public static int mail_split_position = 250;
	public static Dimension umlet_size = new Dimension(960, 750);
	public static Point umlet_location = new Point(5, 5);
	public static String ui_manager;

	static {
		// The default MacOS theme looks ugly, therefore we set metal
		if (OS == Os.MAC) ui_manager = "javax.swing.plaf.metal.MetalLookAndFeel";
		// The GTKLookAndFeel crashes the eclipse plugin therefore we set metal as default instead
		else if ((UMLETTYPE == UmletType.ECLIPSE_PLUGIN) && UIManager.getSystemLookAndFeelClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
			ui_manager = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
		else ui_manager = UIManager.getSystemLookAndFeelClassName();
	}

	public static String mail_smtp = "";
	public static boolean mail_smtp_auth = false;
	public static String mail_smtp_user = "";
	public static boolean mail_smtp_pw_store = false;
	public static String mail_smtp_pw = "";
	public static String mail_from = "";
	public static String mail_to = "";
	public static String mail_cc = "";
	public static String mail_bcc = "";
	public static boolean mail_uxf = true;
	public static boolean mail_gif = true;
	public static boolean mail_pdf = false;

	/**** STATIC HELPER METHODS ****/

	// public static File createRandomFile(String extension) {
	// File randomFile = new File("tmp.diagram." + new Date().getTime() + "." + extension);
	// randomFile.deleteOnExit();
	// return randomFile;
	// }

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
		return decomposeStrings(s, NEWLINE);
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
							s.startsWith(Constants.AUTORESIZE)) s = ""; // filter color-setting strings

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
						s.startsWith(Constants.AUTORESIZE)) tmp = ""; // filter color-setting strings
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

}
