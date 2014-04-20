package com.baselet.element.sticking;

import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.element.uml.relation.PointDoubleIndexed;

public class PointChange {
	private PointDoubleIndexed point;
	private int diffX;
	private int diffY;
	
	public PointChange(PointDoubleIndexed point, int diffX, int diffY) {
		super();
		this.point = point;
		this.diffX = diffX;
		this.diffY = diffY;
	}

	public PointDoubleIndexed getPointHolder() {
		return point;
	}
	
	public int getDiffX() {
		return diffX;
	}

	public int getDiffY() {
		return diffY;
	}
	
	public PointDouble getChangedPoint() {
		return new PointDouble(point.getX()+diffX, point.getY()+diffY);
	}

	@Override
	public String toString() {
		return "PointChange [point=" + point + ", diffX=" + diffX + ", diffY=" + diffY + "]";
	}
	
}
