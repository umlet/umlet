package com.baselet.diagram.draw.helper;

public enum Transparency {
	FOREGROUND(0),
	NO(255),
	BACKGROUND(50),
	SELECTION_BACKGROUND(20);
	
	private int alpha;

	private Transparency(int alpha) {
		this.alpha = alpha;
	}
	
	public int getAlpha() {
		return alpha;
	}
}