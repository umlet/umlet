package com.baselet.elementnew.element.uml.relation;

import com.baselet.diagram.draw.geom.PointDouble;

public class PointDoubleHolder {

	private PointDouble point;

	public PointDoubleHolder(double x, double y) {
		super();
		this.point = new PointDouble(x, y);
	}

	public PointDouble getPoint() {
		return point;
	}
	
	public void setPoint(PointDouble point) {
		this.point = point;
	}
	
}
