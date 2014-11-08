package com.baselet.control.config;

import java.awt.Font;
import java.awt.Point;

import com.baselet.diagram.draw.geom.Dimension;

/**
 * VALUES LOADED FROM CONFIG
 * TODO Entries should be moved to Config.java
**/
public class ConfigConst {

	public static Integer defaultFontsize = 14;
	public static String defaultFontFamily = Font.SANS_SERIF;
	public static boolean start_maximized = false;
	public static boolean show_grid = false;
	public static boolean enable_custom_elements = true;
	public static int main_split_position = 600;
	public static int right_split_position = 400;
	public static int mail_split_position = 250;
	public static Dimension program_size = new Dimension(960, 750);
	public static Point program_location = new Point(5, 5);
	public static int printPadding = 20;
	public static boolean checkForUpdates = true;
	public static String pdfExportFont = ""; // eg in Windows: "pdf_export_font = c:/windows/fonts/msgothic.ttc,1"
	public static String lastUsedPalette;

}
