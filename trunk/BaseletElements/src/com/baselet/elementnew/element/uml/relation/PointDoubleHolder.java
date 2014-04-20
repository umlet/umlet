package com.baselet.elementnew.element.uml.relation;

import com.baselet.diagram.draw.geom.PointDouble;

public class PointDoubleHolder {

	private int index;
	private PointDouble point;

	public PointDoubleHolder(int index, PointDouble point) {
		super();
		this.index = index;
		this.point = point;
	}

	public PointDouble getPoint() {
		return point;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PointDoubleHolder other = (PointDoubleHolder) obj;
		if (index != other.index) return false;
		if (point == null) {
			if (other.point != null) return false;
		}
		else if (!point.equals(other.point)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "PointDoubleHolder [index=" + index + ", point=" + point + "]";
	}
	
}
