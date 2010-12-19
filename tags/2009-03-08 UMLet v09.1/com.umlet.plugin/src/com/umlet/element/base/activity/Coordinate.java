package com.umlet.element.base.activity;

public class Coordinate {
	public int x,y;
	public Coordinate(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public Coordinate clone() {
		return new Coordinate(this.x, this.y);
	}
}
