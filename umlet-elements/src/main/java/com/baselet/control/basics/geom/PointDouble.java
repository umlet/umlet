package com.baselet.control.basics.geom;

/**
 * an immutable point with double coordinates
 * must be immutable because equals and hashcode is overwritten and sets and maps can contain PointDouble as keys
 */
public class PointDouble {

	public final Double x;
	public final Double y;

	public PointDouble(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public double distance(PointDouble o) {
		double distX = o.getX() - getX();
		double distY = o.getY() - getY();
		return Math.sqrt(distX * distX + distY * distY);
	}

	public PointDouble copy() {
		return new PointDouble(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (x == null ? 0 : x.hashCode());
		result = prime * result + (y == null ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PointDouble other = (PointDouble) obj;
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		}
		else if (!x.equals(other.x)) {
			return false;
		}
		if (y == null) {
			if (other.y != null) {
				return false;
			}
		}
		else if (!y.equals(other.y)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "p(x=" + x + " y=" + y + ")";
	}

}
