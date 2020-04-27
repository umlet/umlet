package com.baselet.control.enums;

public enum Direction {
	LEFT, RIGHT, UP, DOWN;

	public boolean isHorizontal() {
		return this == LEFT || this == RIGHT;
	}

	public Direction invert() {
		switch (this) {
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			default:
				throw new RuntimeException("missing invert mapping");
		}
	}
}
