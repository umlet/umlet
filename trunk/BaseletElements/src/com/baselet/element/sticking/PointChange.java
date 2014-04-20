package com.baselet.element.sticking;

import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.element.uml.relation.PointDoubleHolder;

public class PointChange {
	private PointDoubleHolder pointHolder;
	private int diffX;
	private int diffY;
	
	public PointChange(PointDoubleHolder point, int diffX, int diffY) {
		super();
		this.pointHolder = point;
		this.diffX = diffX;
		this.diffY = diffY;
	}

	public PointDoubleHolder getPointHolder() {
		return pointHolder;
	}
	
	public int getDiffX() {
		return diffX;
	}

	public int getDiffY() {
		return diffY;
	}
	
	public PointDouble getChangedPoint() {
		return new PointDouble(pointHolder.getPoint().getX()+diffX, pointHolder.getPoint().getY()+diffY);
	}

	@Override
	public String toString() {
		return "PointChange [point=" + pointHolder + ", diffX=" + diffX + ", diffY=" + diffY + "]";
	}
	
}
