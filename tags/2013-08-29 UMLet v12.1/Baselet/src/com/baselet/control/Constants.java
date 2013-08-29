package com.baselet.control;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.baselet.diagram.draw.geom.Dimension;
import com.umlet.language.FieldOptions;
import com.umlet.language.MethodOptions;
import com.umlet.language.SignatureOptions;
import com.umlet.language.sorting.SortOptions;

public abstract class Constants extends NewGridElementConstants {

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
		public static String VERSION;
		public static String[] GRID_ELEMENT_PACKAGES = new String[] { "com.umlet.element", "com.umlet.element.custom", "com.plotlet.element", "com.baselet.element" };

		public static void init(String name, String version) {
			PROGRAM_NAME = ProgramName.valueOf(name.toUpperCase());
			WEBSITE = "http://www." + PROGRAM_NAME.toLowerCase() + ".com";

			if (Program.RUNTIME_TYPE == RuntimeType.STANDALONE) CONFIG_NAME = Program.PROGRAM_NAME.toLowerCase() + ".cfg";
			else CONFIG_NAME = Program.PROGRAM_NAME.toLowerCase() + "plugin.cfg";

			if (Program.PROGRAM_NAME == ProgramName.UMLET) EXTENSION = "uxf";
			else EXTENSION = "pxf";

			VERSION = version;
		}

	}

	public static class SystemInfo {

		public static final Os OS;
		public static final JavaImplementation JAVA_IMPL;
		public static final String JAVA_VERSION = java.lang.System.getProperty("java.specification.version");
		public static final Metakey META_KEY;

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

	//@formatter:off
	public static String getDefaultHelptext() {
		String returnString =
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
		if (ProgramName.UMLET.equals(Program.PROGRAM_NAME)) {
			returnString += "//" + NEWLINE + "// Select \"Custom Elements > New...\" to create new element types" + NEWLINE;
		}
		returnString += 
				"//////////////////////////////////////////////////////////////////////////////////////////////" + NEWLINE +
				"" + NEWLINE +
				"" + NEWLINE +
				"// This text will be stored with each diagram;  use it for notes.";
		return returnString;
	}

	public static String getDefaultMailtext() {
		return "Type your message here.." + NEWLINE +
				"" + NEWLINE +
				"__" + NEWLINE +
				"To edit the diagram, open the attached " + Program.EXTENSION + "-file with the free editing tool " + Program.PROGRAM_NAME + " (" + Program.WEBSITE + ")";
	}
	//@formatter:on


	/**** EXPORT FORMATS ****/
	public static final List<String> exportFormatList = Arrays.asList(new String[] { "bmp", "eps", "gif", "jpg", "pdf", "png", "svg" });

	/**** ZOOM VALUES ****/
	public static final ArrayList<String> zoomValueList = new ArrayList<String>();
	static {
		for (int i = 1; i <= 20; i++) {
			zoomValueList.add(i + "0%");
		}
	}

	/**** REGULAR EXPRESSIONS ****/

	public static final String REGEX_FLOAT = "(\\d+(\\.\\d+)?)";

	/**** OTHER CONSTANTS ****/
	
	public static final double EXPORT_DISPLACEMENT = 0.5;

	public static final String MANIFEST_BUNDLE_NAME = "Bundle-Name";
	public static final String MANIFEST_BUNDLE_VERSION = "Bundle-Version";
	public static final String LOG4J_PROPERTIES = "log4j.properties";

	public static final int NOTIFICATION_SHOW_TIME = 3000;
	public static final String CUSTOM_ELEMENT_CLASSNAME = "CustomElementImpl";
	public static final int DEFAULTGRIDSIZE = 10;
	public static final int INTERFACE_LINE_LENGTH = 40;

	public static final int CUSTOM_ELEMENT_COMPILE_INTERVAL = 500;

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
	public static final Color GRID_COLOR = new Color(235, 235, 235);

	public static final List<String> fontFamilyList = Arrays.asList(new String[] { Font.SANS_SERIF, Font.SERIF, Font.MONOSPACED });

	public static final List<LookAndFeelInfo> lookAndFeels = Arrays.asList(UIManager.getInstalledLookAndFeels());

	protected static final String DEFAULT_STRING = "Default";
	public static final Comparator<String> DEFAULT_FIRST_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String s1, String s2) { // Anything which starts with "Default" is always first
			if (s1.startsWith(Constants.DEFAULT_STRING) && !s2.startsWith(Constants.DEFAULT_STRING)) return -1;
			if (s2.startsWith(Constants.DEFAULT_STRING) && !s1.startsWith(Constants.DEFAULT_STRING)) return 1;
			else return s1.compareTo(s2);
		}
	};

	public static final int RECENT_FILES_LIST_LENGTH = 10;
	public static final RecentlyUsedFilesList recentlyUsedFilesList = new RecentlyUsedFilesList();

	public static final int MIN_MAIN_SPLITPANEL_SIZE = 100;
	public static final int MIN_MAIL_SPLITPANEL_SIZE = 250;

	public static final Font PANEL_HEADER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 11);
	public static final Font PANEL_CONTENT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 11);

	/**** VALUES LOADED FROM CONFIG ****/
	public static Integer defaultFontsize = (Program.PROGRAM_NAME == ProgramName.UMLET ? 14 : 10);
	public static String defaultFontFamily = Font.SANS_SERIF;
	public static boolean start_maximized = false;
	public static boolean show_grid = false;
	public static boolean enable_custom_elements = true;
	public static int main_split_position = 600;
	public static int right_split_position = 400;
	public static int mail_split_position = 250;
	public static Dimension program_size = new Dimension(960, 750);
	public static Point program_location = new Point(5, 5);
	public static String ui_manager;
	public static int printPadding = 20;
	public static boolean checkForUpdates = true;
	public static String openFileHome = System.getProperty("user.dir");
	public static String pdfExportFont = ""; // eg in Windows: "pdf_export_font = c:/windows/fonts/msgothic.ttc,1"
	public static boolean generateClassPackage = true;
	public static FieldOptions generateClassFields = FieldOptions.ALL;
	public static MethodOptions generateClassMethods = MethodOptions.ALL;
	public static SignatureOptions generateClassSignatures = SignatureOptions.ALL;
	public static SortOptions generateClassSortings = SortOptions.HEIGHT;
	public static String lastUsedPalette = DEFAULT_STRING + " - original main elements"; // since v12 DefaultNewElements are shown at first startup (instead of Constants.DEFAULT_STRING)

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

	public static final String ERROR_SAVING_FILE = "An error occured during saving: ";
	public static final String ERROR_SAVING_EMPTY_DIAGRAM = "You cannot save or export an empty diagram.";
	public static final String ERROR_PRINTING = "An error occured during printing.";

}
