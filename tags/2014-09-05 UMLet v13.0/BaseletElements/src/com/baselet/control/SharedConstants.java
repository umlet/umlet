package com.baselet.control;

/**
 * temporary separation of constants which are used by NewGridElement class (for an easier migration to a non-awt based gui)
 */
public class SharedConstants {

	public static final String UTF8_BOM = "\uFEFF";

	public static final String LEFT_QUOTATION = "<<";
	public static final String RIGHT_QUOTATION = ">>";

	public enum RuntimeType {
		STANDALONE, ECLIPSE_PLUGIN, BATCH
	}

	/**** PROGRAM, PLATTFORM AND JAVA SPECIFIC SETTINGS ****/
	public static class Program {

		// Basically the RUNTIME_TYPE is STANDALONE until it gets overwritten after program startup
		public static RuntimeType RUNTIME_TYPE = RuntimeType.STANDALONE;
		public static String CONFIG_NAME;
		public static String NAME = "UMLet";
		public static String EXTENSION = "uxf";
		public static String WEBSITE;
		public static String VERSION;
		public static String[] GRID_ELEMENT_PACKAGES = new String[] { "com.umlet.element", "com.umlet.element.custom", "com.plotlet.element", "com.baselet.element" };

		public static void init(String version) {
			WEBSITE = "http://www." + NAME.toLowerCase() + ".com";

			if (Program.RUNTIME_TYPE == RuntimeType.STANDALONE) {
				CONFIG_NAME = NAME.toLowerCase() + ".cfg";
			}
			else {
				CONFIG_NAME = NAME.toLowerCase() + "plugin.cfg";
			}

			VERSION = version;
		}

	}

	public static final int DEFAULT_GRID_SIZE = 10;

	public static boolean show_stickingpolygon = true;

	public static boolean dev_mode = false;

	public static String program = ""; // TODO merge with Constants.Program

	public static String VERSION = "12.2"; // TODO merge with Constants.Program

	public static boolean stickingEnabled = true; // TODO temporarily implemented as "constant" to make it work for Swing and GWT

}
