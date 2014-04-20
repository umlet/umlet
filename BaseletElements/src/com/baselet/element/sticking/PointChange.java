package com.baselet.element.sticking;

import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.element.uml.relation.PointDoubleHolder;

public class PointChange {
	private PointDoubleHolder point;
	private int diffX;
	private int diffY;
	
	public PointChange(PointDoubleHolder point, int diffX, int diffY) {
		super();
		this.point = point;
		this.diffX = diffX;
		this.diffY = diffY;
	}

	public PointDoubleHolder getPoint() {
		return point;
	}

	public int getDiffX() {
		return diffX;
	}

	public int getDiffY() {
		return diffY;
	}
	
	public PointDouble getChangedPoint() {
		return new PointDouble(point.getPoint().getX()+diffX, point.getPoint().getY()+diffY);
	}

	@Override
	public String toString() {
		return "PointChange [point=" + point + ", diffX=" + diffX + ", diffY=" + diffY + "]";
	}
	
}
