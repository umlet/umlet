package com.baselet.element.sticking;

import com.baselet.control.basics.geom.PointDouble;

public class PointDoubleIndexed extends PointDouble {

	private final Integer index;

	public PointDoubleIndexed(Integer index, double x, double y) {
		super(x, y);
		this.index = index;
	}

	public Integer getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (index == null ? 0 : index.hashCode());
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
		if (index == null) {
			if (other.index != null) {
				return false;
			}
		}
		else if (!index.equals(other.index)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "p(i=" + index + " x=" + x + " y=" + y + ")";
	}

}
