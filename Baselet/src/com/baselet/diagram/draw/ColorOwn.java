package com.baselet.diagram.draw;

public class ColorOwn {

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
