package com.baselet.control.constants;

/**
 * temporary separation of constants which are used by NewGridElement class (for an easier migration to a non-awt based gui)
 */
public class SharedConstants {

	public static final String UTF8_BOM = "\uFEFF";

	public static final String LEFT_QUOTATION = "<<";
	public static final String RIGHT_QUOTATION = ">>";

	public static final int DEFAULT_GRID_SIZE = 10;

	public static boolean show_stickingpolygon = true;

	private static boolean dev_mode = false; // TODO should be moved to a shared config class

	public static void setDev_mode(boolean dev_mode) {
		SharedConstants.dev_mode = dev_mode;
	}

	public static boolean isDev_mode() {
		return dev_mode;
	}

	private static boolean stickingEnabled = true;

	public static void setStickingEnabled(boolean stickingEnabled) {
		SharedConstants.stickingEnabled = stickingEnabled;
	}

	public static boolean isStickingEnabled() {
		return stickingEnabled;
	}

}
