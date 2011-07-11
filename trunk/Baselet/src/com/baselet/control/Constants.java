package com.baselet.control;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public abstract class Constants {

	private Constants() {} // private constructor to avoid instantiation

	public enum Os {
		WINDOWS, LINUX, UNIX, MAC, UNKNOWN
	}

	public enum JavaImplementation {
		OPEN, SUN
	}

	public enum RuntimeType {
		STANDALONE, ECLIPSE_PLUGIN, BATCH
	}

	public enum ProgramName {
		UMLET("UMLet"), PLOTLET("Plotlet");
		private String name;

		private ProgramName(String n) {
			name = n;
		}

		@Override
		public String toString() {
			return name;
		}

		public String toLowerCase() {
			return name.toLowerCase();
		}
	}

	public enum Metakey {
		CTRL, CMD;

		@Override
		public String toString() {
			if (this == CTRL) return "Ctrl";
			else return "Cmd";
		}

		// Use these masks as modifiers if you want to enforce ctrl or meta in addition to a keyevent
		public int getMask() {
			if (this == CTRL) return ActionEvent.CTRL_MASK;
			else return ActionEvent.META_MASK;
		}
		public int getMaskDown() {
			if (this == CTRL) return InputEvent.CTRL_DOWN_MASK;
			else return InputEvent.META_DOWN_MASK;
		}
	}

	/**** PROGRAM, PLATTFORM AND JAVA SPECIFIC SETTINGS ****/
	public static class Program {

		// Basically the RUNTIME_TYPE is STANDALONE until it gets overwritten after program startup
		public static RuntimeType RUNTIME_TYPE = RuntimeType.STANDALONE;
		public static ProgramName PROGRAM_NAME;
		public static String CONFIG_NAME;
		public static String EXTENSION;
		public static String WEBSITE;
		public static Double VERSION;
		public static String[] GRID_ELEMENT_PACKAGES = new String[] {"com.umlet.element", "com.umlet.element.custom", "com.plotlet.element", "com.baselet.element"};

		public static void init(ProgramName name, String version) {
			PROGRAM_NAME = name;
			WEBSITE = "http://www." + PROGRAM_NAME.toLowerCase() + ".com";

			if (Program.RUNTIME_TYPE == RuntimeType.STANDALONE) CONFIG_NAME = Program.PROGRAM_NAME.toLowerCase() + ".cfg";
			else CONFIG_NAME = Program.PROGRAM_NAME.toLowerCase() + "plugin.cfg";

			if (Program.PROGRAM_NAME == ProgramName.UMLET) EXTENSION = "uxf";
			else EXTENSION = "pxf";
			
			VERSION = Double.valueOf(version);
		}
	

	}

	public static class SystemInfo {

		public static final Os OS;
		public static final JavaImplementation JAVA_IMPL;
		public static final String JAVA_VERSION = java.lang.System.getProperty("java.specification.version");
		public final static Metakey META_KEY;

		static {
			String os = java.lang.System.getProperty("os.name").toUpperCase();
			if (os.startsWith("WINDOWS")) OS = Os.WINDOWS;
			else if (os.startsWith("MAC")) OS = Os.MAC;
			else if (os.startsWith("LINUX")) OS = Os.LINUX;
			else if (os.contains("UNIX") || os.contains("BSD")) OS = Os.UNIX;
			else OS = Os.UNKNOWN;

			if (java.lang.System.getProperty("java.runtime.name").toUpperCase().contains("OPEN")) JAVA_IMPL = JavaImplementation.OPEN;
			else JAVA_IMPL = JavaImplementation.SUN;
			
			if (SystemInfo.OS == Os.MAC) META_KEY = Metakey.CMD;
			else META_KEY = Metakey.CTRL;
		}
	}

	/**** NEWLINE CHARACTER AND DEFAULT HELP- AND MAILTEXT ****/
	public static final String NEWLINE = "\n";
	public static final String COMMENT = "//";
	public static String DEFAULT_HELPTEXT;
	public static String DEFAULT_MAILTEXT;

	static {
		DEFAULT_HELPTEXT =
			"// Uncomment the following line to change the fontsize and font:" + NEWLINE +
			"// fontsize=14" + NEWLINE +
			"// fontfamily=SansSerif //possible: SansSerif,Serif,Monospaced" + NEWLINE +
			"" + NEWLINE +
			"" + NEWLINE +
			"//////////////////////////////////////////////////////////////////////////////////////////////" + NEWLINE +
			"// Welcome to " + Program.PROGRAM_NAME + "!" + NEWLINE +
			"//" + NEWLINE +
			"// Double-click on elements to add them to the diagram, or to copy them" + NEWLINE +
			"// Edit elements by modifying the text in this panel" + NEWLINE +
			"// Hold " + SystemInfo.META_KEY + " to select multiple elements" + NEWLINE +
			"// Use " + SystemInfo.META_KEY + "+mouse to select via lasso" + NEWLINE +
			"//" + NEWLINE +
			"// Use +/- or " + SystemInfo.META_KEY + "+mouse wheel to zoom" + NEWLINE +
			"// Drag a whole relation at its central square icon" + NEWLINE +
			"//" + NEWLINE +
			"// Press " + SystemInfo.META_KEY + "+C to copy the whole diagram to the system clipboard (then just paste it to, eg, Word)" + NEWLINE +
			"// Edit the files in the \"palettes\" directory to create your own element palettes" + NEWLINE;
		if (Program.PROGRAM_NAME.equals(ProgramName.UMLET)) {
			DEFAULT_HELPTEXT += "//" + NEWLINE + "// Select \"Custom Elements > New...\" to create new element types" + NEWLINE;
		}
		DEFAULT_HELPTEXT +=
			"//////////////////////////////////////////////////////////////////////////////////////////////" + NEWLINE +
			"" + NEWLINE +
			"" + NEWLINE +
			"// This text will be stored with each diagram;  use it for notes.";

		DEFAULT_MAILTEXT =
			"Type your message here.." + NEWLINE +
			"" + NEWLINE +
			"__" + NEWLINE;
		DEFAULT_MAILTEXT += "To edit the diagram, open the attached " + Program.EXTENSION + "-file with the free editing tool " + Program.PROGRAM_NAME + " (" + Program.WEBSITE + ")";
	}

	/**** AVAILABLE COLORS ****/
	public static final HashMap<String, Color> colorMap = new HashMap<String, Color>();
	static {
		colorMap.put("black", Color.BLACK);
		colorMap.put("blue", Color.BLUE);
		colorMap.put("cyan", Color.CYAN);
		colorMap.put("dark_gray", Color.DARK_GRAY);
		colorMap.put("gray", Color.GRAY);
		colorMap.put("green", Color.GREEN);
		colorMap.put("light_gray", Color.LIGHT_GRAY);
		colorMap.put("magenta", Color.MAGENTA);
		colorMap.put("orange", Color.decode("#FFA500"));
		colorMap.put("pink", Color.PINK);
		colorMap.put("red", Color.RED);
		colorMap.put("white", Color.WHITE);
		colorMap.put("yellow", Color.YELLOW);
	}
	public static final Color DEFAULT_SELECTED_COLOR = Color.BLUE;
	public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	public static final float ALPHA_NO_TRANSPARENCY = 1.0f;
	public static final float ALPHA_MIDDLE_TRANSPARENCY = 0.5f;
	public static final float ALPHA_FULL_TRANSPARENCY = 0.0f;

	/**** EXPORT FORMATS ****/
	public static final List<String> exportFormatList = Arrays.asList(new String[] {"bmp", "eps", "gif", "jpg", "pdf", "png", "svg"});

	/**** ZOOM VALUES ****/
	public static final ArrayList<String> zoomValueList = new ArrayList<String>();
	static {
		for (int i = 1; i <= 20; i++) {
			zoomValueList.add(i + "0%");
		}
	}

	/**** OTHER CONSTANTS ****/
	public static final String CUSTOM_ELEMENT_CLASSNAME = "CustomElementImpl";
	public static final int DEFAULTGRIDSIZE = 10;
	public static final int PRINTPADDING = 20;
	public static final int INTERFACE_LINE_LENGTH = 40;

	public enum LineType {SOLID, DASHED, DOTTED, DOUBLE, DOUBLE_DASHED, DOUBLE_DOTTED};
	public static final int DEFAULT_LINE_THICKNESS = 1;

	public static final int CUSTOM_ELEMENT_COMPILE_INTERVAL = 500;

	public enum AlignHorizontal {LEFT, CENTER, RIGHT};
	public enum AlignVertical {TOP, CENTER, BOTTOM};
	public static final int RESIZE_TOP = 1, RESIZE_RIGHT = 2, RESIZE_BOTTOM = 4, RESIZE_LEFT = 8;
	public static final int RESIZE_TOP_LEFT = RESIZE_TOP + RESIZE_LEFT;
	public static final int RESIZE_TOP_RIGHT = RESIZE_TOP + RESIZE_RIGHT;
	public static final int RESIZE_BOTTOM_LEFT = RESIZE_BOTTOM + RESIZE_LEFT;
	public static final int RESIZE_BOTTOM_RIGHT = RESIZE_BOTTOM + RESIZE_RIGHT;

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

	public static final int PASTE_DISPLACEMENT_GRIDS = 2;
	public static final int MIN_MAIL_SPLIT_POSITION = 100;
	public static final Color GRID_COLOR = new Color(235, 235, 235);

	public static final Integer FONT_MIN = 9;
	public static final Integer FONT_MAX = 20;
	public static final List<Integer> fontSizeList = new ArrayList<Integer>();
	static {
		for (int i = FONT_MIN; i <= FONT_MAX; i++) {
			fontSizeList.add(i);
		}
	}

	public static final List<String> fontFamilyList = Arrays.asList(new String[] {Font.SANS_SERIF, Font.SERIF, Font.MONOSPACED});
	
	public static final List<LookAndFeelInfo> lookAndFeels = Arrays.asList(UIManager.getInstalledLookAndFeels());
	static {
		// The Eclipse Plugin doesn't work with GTKLookAndFeel, therefore we remove it from the choosable options
		if (Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN) {
			lookAndFeels.remove("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		}
	}
	
	public static final FontRenderContext FRC = new FontRenderContext(null, true, true);

	/**** VALUES LOADED FROM CONFIG ****/
	public static int defaultFontsize = (Program.PROGRAM_NAME == ProgramName.UMLET ? 14 : 10);
	public static String defaultFontFamily = Font.SANS_SERIF;
	public static boolean start_maximized = false;
	public static boolean show_stickingpolygon = true;
	public static boolean show_grid = false;
	public static boolean enable_custom_elements = true;
	public static int main_split_position = 600;
	public static int right_split_position = 400;
	public static int mail_split_position = 250;
	public static Dimension program_size = new Dimension(960, 750);
	public static Point program_location = new Point(5, 5);
	public static String ui_manager;

	static {
		// The default MacOS theme looks ugly, therefore we set metal
		if (SystemInfo.OS == Os.MAC) ui_manager = "javax.swing.plaf.metal.MetalLookAndFeel";
		// The GTKLookAndFeel crashes the eclipse plugin therefore we set metal as default instead
		else if ((Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN) && UIManager.getSystemLookAndFeelClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
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
	public static boolean mail_xml = true;
	public static boolean mail_gif = true;
	public static boolean mail_pdf = false;

	/**** ERROR MESSAGES ****/

	public static final String ERROR_SAVING_FILE = "An error occured during saving. Please check the filename and your write access.";
	public static final String ERROR_SAVING_EMPTY_DIAGRAM = "You cannot save or export an empty diagram.";
	public static final String ERROR_PRINTING = "An error occured during printing.";

}
