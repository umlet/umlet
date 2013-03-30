package com.baselet.gwt.client.copy.control;

import com.baselet.gwt.client.copy.diagram.draw.helper.ColorOwn;

/**
 * temporary separation of constants which are used by NewGridElement class (for an easier migration to a non-awt based gui)
 */
public class NewGridElementConstants {

	public static final int DEFAULT_LINE_THICKNESS = 1;
	
	public static final int RESIZE_TOP = 1, RESIZE_RIGHT = 2, RESIZE_BOTTOM = 4, RESIZE_LEFT = 8, RESIZE_NONE = 0;
	public static final int RESIZE_ALL = RESIZE_TOP | RESIZE_LEFT | RESIZE_BOTTOM | RESIZE_RIGHT;

	public static final int RESIZE_TOP_LEFT = RESIZE_TOP + RESIZE_LEFT;
	public static final int RESIZE_TOP_RIGHT = RESIZE_TOP + RESIZE_RIGHT;
	public static final int RESIZE_BOTTOM_LEFT = RESIZE_BOTTOM + RESIZE_LEFT;
	public static final int RESIZE_BOTTOM_RIGHT = RESIZE_BOTTOM + RESIZE_RIGHT;

	public static final ColorOwn DEFAULT_SELECTED_COLOR = ColorOwn.BLUE;
	public static final ColorOwn DEFAULT_FOREGROUND_COLOR = ColorOwn.BLACK;
	public static final ColorOwn DEFAULT_BACKGROUND_COLOR = ColorOwn.WHITE;
	public static final float ALPHA_NO_TRANSPARENCY = 1.0f;
	public static final float ALPHA_MIDDLE_TRANSPARENCY = 0.5f;
	public static final float ALPHA_NEARLY_FULL_TRANSPARENCY = 0.035f;
	public static final float ALPHA_FULL_TRANSPARENCY = 0.0f;

	public static boolean show_stickingpolygon = true;

}
