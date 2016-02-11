package com.baselet.diagram.draw.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ColorOwn {

	public static final String EXAMPLE_TEXT = "color string (green,...) or code (#3c7a00,...)";

	public static enum Transparency {
		FOREGROUND(255), FULL_TRANSPARENT(0), DEPRECATED_WARNING(175), BACKGROUND(125), SELECTION_BACKGROUND(20);

		private int alpha;

		private Transparency(int alpha) {
			this.alpha = alpha;
		}

		public int getAlpha() {
			return alpha;
		}
	}

	public static final ColorOwn RED = new ColorOwn(255, 0, 0, Transparency.FOREGROUND);
	public static final ColorOwn GREEN = new ColorOwn(0, 255, 0, Transparency.FOREGROUND);
	public static final ColorOwn BLUE = new ColorOwn(0, 0, 255, Transparency.FOREGROUND);
	public static final ColorOwn YELLOW = new ColorOwn(255, 255, 0, Transparency.FOREGROUND);
	public static final ColorOwn MAGENTA = new ColorOwn(255, 0, 255, Transparency.FOREGROUND);
	public static final ColorOwn WHITE = new ColorOwn(255, 255, 255, Transparency.FOREGROUND);
	public static final ColorOwn BLACK = new ColorOwn(0, 0, 0, Transparency.FOREGROUND);
	public static final ColorOwn ORANGE = new ColorOwn(255, 165, 0, Transparency.FOREGROUND);
	public static final ColorOwn CYAN = new ColorOwn(0, 255, 255, Transparency.FOREGROUND);
	public static final ColorOwn DARK_GRAY = new ColorOwn(70, 70, 70, Transparency.FOREGROUND);
	public static final ColorOwn GRAY = new ColorOwn(120, 120, 120, Transparency.FOREGROUND);
	public static final ColorOwn LIGHT_GRAY = new ColorOwn(200, 200, 200, Transparency.FOREGROUND);
	public static final ColorOwn PINK = new ColorOwn(255, 175, 175, Transparency.FOREGROUND);

	public static final ColorOwn TRANSPARENT = WHITE.transparency(Transparency.FULL_TRANSPARENT); // color white is important because EPS export doesn't support transparency, therefore background will be white
	public static final ColorOwn SELECTION_FG = BLUE;
	public static final ColorOwn SELECTION_BG = new ColorOwn(0, 0, 255, Transparency.SELECTION_BACKGROUND);
	public static final ColorOwn STICKING_POLYGON = new ColorOwn(100, 180, 255, Transparency.FOREGROUND);
	public static final ColorOwn SYNTAX_HIGHLIGHTING = new ColorOwn(0, 100, 255, Transparency.FOREGROUND);
	public static final ColorOwn DEFAULT_FOREGROUND = BLACK;
	public static final ColorOwn DEFAULT_BACKGROUND = TRANSPARENT;

	public static final Map<String, ColorOwn> COLOR_MAP;

	static {
		HashMap<String, ColorOwn> colorMap = new HashMap<String, ColorOwn>();
		colorMap.put("black", ColorOwn.BLACK);
		colorMap.put("blue", ColorOwn.BLUE);
		colorMap.put("cyan", ColorOwn.CYAN);
		colorMap.put("dark_gray", ColorOwn.DARK_GRAY);
		colorMap.put("gray", ColorOwn.GRAY);
		colorMap.put("green", ColorOwn.GREEN);
		colorMap.put("light_gray", ColorOwn.LIGHT_GRAY);
		colorMap.put("magenta", ColorOwn.MAGENTA);
		colorMap.put("orange", ColorOwn.ORANGE);
		colorMap.put("pink", ColorOwn.PINK);
		colorMap.put("red", ColorOwn.RED);
		colorMap.put("white", ColorOwn.WHITE);
		colorMap.put("yellow", ColorOwn.YELLOW);
		colorMap.put("none", ColorOwn.TRANSPARENT);
		COLOR_MAP = Collections.unmodifiableMap(colorMap);
	}

	/* fields should be final to avoid changing parts of existing color object (otherwise unexpected visible changes can happen) */
	private final int red;
	private final int green;
	private final int blue;
	private final int alpha;

	public ColorOwn(int red, int green, int blue, Transparency transparency) {
		this(red, green, blue, transparency.getAlpha());
	}

	public ColorOwn(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public ColorOwn(String hex) {
		int i = Integer.decode(hex);
		red = i >> 16 & 0xFF;
		green = i >> 8 & 0xFF;
		blue = i & 0xFF;
		alpha = Transparency.FOREGROUND.getAlpha();
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

	public ColorOwn transparency(Transparency transparency) {
		return transparency(transparency.getAlpha());
	}

	public ColorOwn transparency(int alpha) {
		return new ColorOwn(getRed(), getGreen(), getBlue(), alpha);
	}

	public ColorOwn darken(int factor) {
		return new ColorOwn(Math.max(0, getRed() - factor), Math.max(0, getGreen() - factor), Math.max(0, getBlue() - factor), getAlpha());
	}

	/**
	 * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
	 *
	 * @param colorString
	 *            String which describes the color
	 * @return Color which is related to the String or null if it is no valid colorString
	 */
	public static ColorOwn forStringOrNull(String colorString, Transparency transparency) {
		try {
			return forString(colorString, transparency);
		} catch (StyleException e) {
			return null;
		}
	}

	public static ColorOwn forString(String colorString, Transparency transparency) {
		return forString(colorString, transparency.alpha);
	}

	/**
	 * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
	 *
	 * @param colorString
	 *            String which describes the color
	 * @return Color which is related to the String or null if it is no valid colorString
	 */
	public static ColorOwn forString(String colorString, int transparency) {
		boolean error = false;
		ColorOwn returnColor = null;
		if (colorString == null) {
			error = true;
		}
		else {
			for (Entry<String, ColorOwn> c : COLOR_MAP.entrySet()) {
				if (colorString.equalsIgnoreCase(c.getKey())) {
					returnColor = c.getValue();
					break;
				}
			}
			if (returnColor == null) {
				try {
					returnColor = new ColorOwn(colorString);
				} catch (NumberFormatException e) {
					error = true;
				}
			}
			if (returnColor != null) {
				returnColor = returnColor.transparency(transparency);
			}
		}
		if (error) {
			throw new StyleException("value must be a " + EXAMPLE_TEXT);
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ColorOwn other = (ColorOwn) obj;
		if (alpha != other.alpha) {
			return false;
		}
		if (blue != other.blue) {
			return false;
		}
		if (green != other.green) {
			return false;
		}
		if (red != other.red) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ColorOwn [red=" + red + ", green=" + green + ", blue=" + blue + ", alpha=" + alpha + "]";
	}

}
