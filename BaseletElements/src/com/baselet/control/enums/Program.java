package com.baselet.control.enums;

/**
 * PROGRAM, PLATTFORM AND JAVA SPECIFIC SETTINGS
 **/
public class Program {

	// Basically the RUNTIME_TYPE is STANDALONE until it gets overwritten after program startup
	public static RuntimeType RUNTIME_TYPE = RuntimeType.STANDALONE;
	public static String CONFIG_NAME;
	public static final String NAME = "UMLet";
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