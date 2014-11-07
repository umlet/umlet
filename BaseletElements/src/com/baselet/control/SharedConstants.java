package com.baselet.control;

/**
 * temporary separation of constants which are used by NewGridElement class (for an easier migration to a non-awt based gui)
 */
public class SharedConstants {

	public static final String UTF8_BOM = "\uFEFF";

	public static final String LEFT_QUOTATION = "<<";
	public static final String RIGHT_QUOTATION = ">>";

	public static final int DEFAULT_GRID_SIZE = 10;

	public static boolean show_stickingpolygon = true;

	public static boolean dev_mode = false;

	public static boolean stickingEnabled = true; // TODO temporarily implemented as "constant" to make it work for Swing and GWT

}
