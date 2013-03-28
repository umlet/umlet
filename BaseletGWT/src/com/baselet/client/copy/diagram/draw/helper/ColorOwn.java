package com.baselet.client.copy.diagram.draw.helper;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class ColorOwn {

	private final static Logger log = Logger.getLogger(ColorOwn.class);
	
	public static final ColorOwn RED = new ColorOwn(255, 0, 0, 255);
	public static final ColorOwn GREEN = new ColorOwn(0, 255, 0, 255);
	public static final ColorOwn BLUE = new ColorOwn(0, 0, 255, 255);
	public static final ColorOwn YELLOW = new ColorOwn(255, 255, 0, 255);
	public static final ColorOwn MAGENTA = new ColorOwn(255, 0, 255, 255);
	public static final ColorOwn WHITE = new ColorOwn(255, 255, 255, 255);
	public static final ColorOwn BLACK = new ColorOwn(0, 0, 0, 255);
	public static final ColorOwn ORANGE = new ColorOwn(255, 165, 0, 255);
	public static final ColorOwn CYAN = new ColorOwn(0, 255, 255, 255);
	public static final ColorOwn DARK_GRAY = new ColorOwn(70, 70, 70, 255);
	public static final ColorOwn GRAY = new ColorOwn(120, 120, 120, 255);
	public static final ColorOwn LIGHT_GRAY = new ColorOwn(200, 200, 200, 255);
	public static final ColorOwn PINK = new ColorOwn(255, 175, 175, 255);

	public static final HashMap<String, ColorOwn> COLOR_MAP = new HashMap<String, ColorOwn>();
	static {
		COLOR_MAP.put("black", ColorOwn.BLACK);
		COLOR_MAP.put("blue", ColorOwn.BLUE);
		COLOR_MAP.put("cyan", ColorOwn.CYAN);
		COLOR_MAP.put("dark_gray", ColorOwn.DARK_GRAY);
		COLOR_MAP.put("gray", ColorOwn.GRAY);
		COLOR_MAP.put("green", ColorOwn.GREEN);
		COLOR_MAP.put("light_gray", ColorOwn.LIGHT_GRAY);
		COLOR_MAP.put("magenta", ColorOwn.MAGENTA);
		COLOR_MAP.put("orange", ColorOwn.ORANGE);
		COLOR_MAP.put("pink", ColorOwn.PINK);
		COLOR_MAP.put("red", ColorOwn.RED);
		COLOR_MAP.put("white", ColorOwn.WHITE);
		COLOR_MAP.put("yellow", ColorOwn.YELLOW);
	}
	
	private int red;
	private int green;
	private int blue;
	private int alpha;

	public ColorOwn(int red, int green, int blue, int alpha) {
		init(red, green, blue, alpha);
	}

	private void init(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public ColorOwn(String hex) {
		int i = Integer.decode(hex);
		int r = (i >> 16) & 0xFF;
		int g = (i >> 8) & 0xFF;
		int b = i & 0xFF;
		init(r, g, b, 255);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public int getAlpha() {
		return alpha;
	}
	
	public ColorOwn darken(int factor) {
		red -= factor;
		green -= factor;
		blue -= factor;
		return this;
	}

	/**
	 * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
	 * 
	 * @param colorString
	 *            String which describes the color
	 * @return Color which is related to the String or null if it is no valid colorString
	 */
	public static ColorOwn forString(String colorString) {
		if (colorString == null) return null;
		ColorOwn returnColor = null;
		for (String color : COLOR_MAP.keySet()) {
			if (colorString.equalsIgnoreCase(color)) {
				returnColor = COLOR_MAP.get(color);
				break;
			}
		}
		if (returnColor == null) {
			try {
				returnColor = new ColorOwn(colorString);
			} catch (NumberFormatException e) {
				//only print for debugging because message would be printed, when typing the color
				log.debug("Invalid color:" + colorString);
			}
		}
		return returnColor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + alpha;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ColorOwn other = (ColorOwn) obj;
		if (alpha != other.alpha) return false;
		if (blue != other.blue) return false;
		if (green != other.green) return false;
		if (red != other.red) return false;
		return true;
	}

}
