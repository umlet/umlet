package com.baselet.elementnew.element.uml.relation;

import com.baselet.diagram.draw.geom.PointDouble;

public class PointDoubleIndexed extends PointDouble {

	private int index;

	public PointDoubleIndexed(int index, double x, double y) {
		super(x, y);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PointDoubleIndexed other = (PointDoubleIndexed) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "p(i=" + index + " x=" + x + " y=" + y + ")";
	}

}
