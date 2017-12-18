package com.baselet.control.constants;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.baselet.control.enums.Program;

public abstract class Constants extends SharedConstants {

	private Constants() {} // private constructor to avoid instantiation

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
				"// Welcome to " + Program.getInstance().getProgramName() + "!" + NEWLINE +
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
				"// Edit the files in the \"palettes\" directory to create your own element palettes" + NEWLINE +
				"//" + NEWLINE + "// Select \"Custom Elements > New...\" to create new element types" + NEWLINE;
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
				"To edit the diagram, open the attached " + Program.getInstance().getExtension() + "-file with the free editing tool " + Program.getInstance().getProgramName() + " (" + Program.getInstance().getWebsite() + ")";
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

	public static final String LOG4J_PROPERTIES = "log4j.properties";

	public static final String CUSTOM_ELEMENT_CLASSNAME = "CustomElementImpl";
	public static final int DEFAULTGRIDSIZE = 10;
	public static final int INTERFACE_LINE_LENGTH = 40;

	public static final int CUSTOM_ELEMENT_COMPILE_INTERVAL = 500;

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
		public int compare(String s1, String s2) {
			// "UML Common Elements" before "Default" before anything else
			for (String prefixFirst : Arrays.asList("UML Common Elements", Constants.DEFAULT_STRING)) {
				if (s1.startsWith(prefixFirst) && !s2.startsWith(prefixFirst)) {
					return -1;
				}
				if (s2.startsWith(prefixFirst) && !s1.startsWith(prefixFirst)) {
					return 1;
				}
			}
			// "Deprecated" after "Plot" after anything else
			for (String prefixLast : Arrays.asList("Deprecated", "Plots")) {
				if (s1.startsWith(prefixLast) && !s2.startsWith(prefixLast)) {
					return 1;
				}
				if (s2.startsWith(prefixLast) && !s1.startsWith(prefixLast)) {
					return -1;
				}
			}
			return s1.compareTo(s2);
		}
	};

	public static final int MIN_MAIN_SPLITPANEL_SIZE = 100;
	public static final int MIN_MAIL_SPLITPANEL_SIZE = 250;

}
