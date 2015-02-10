package com.baselet.element.old.relation;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;

public class Arrow extends Rectangle {
	private Point _arrowEndA;
	private Point _arrowEndB;

	// A.Mueller start
	private Point _crossEndA;
	private Point _crossEndB;
	private int _arcStart;
	private int _arcEnd;

	// A.Mueller end

	public Point getArrowEndA() {
		return _arrowEndA;
	}

	public Point getArrowEndB() {
		return _arrowEndB;
	}

	public void setArrowEndA(Point p) {
		_arrowEndA = p;
	}

	public void setArrowEndB(Point p) {
		_arrowEndB = p;
	}

	// A.Mueller start
	public void setCrossEndA(Point p) {
		_crossEndA = p;
	}

	public void setCrossEndB(Point p) {
		_crossEndB = p;
	}

	public int getArcStart() {
		return _arcStart;
	}

	public int getArcEnd() {
		return _arcEnd;
	}

	public void setArcStart(int a) {
		_arcStart = a;
	}

	public void setArcEnd(int a) {
		_arcEnd = a;
	}

	public Point getCrossEndA() {
		return _crossEndA;
	}

	public Point getCrossEndB() {
		return _crossEndB;
	}

	// A.Mueller end

	private String _arrowType = null;

	public String getString() {
		return _arrowType;
	}

	public Arrow(String arrowType) {
		super(0, 0, 1, 1);
		_arrowType = arrowType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + _arcEnd;
		result = prime * result + _arcStart;
		result = prime * result + (_arrowEndA == null ? 0 : _arrowEndA.hashCode());
		result = prime * result + (_arrowEndB == null ? 0 : _arrowEndB.hashCode());
		result = prime * result + (_arrowType == null ? 0 : _arrowType.hashCode());
		result = prime * result + (_crossEndA == null ? 0 : _crossEndA.hashCode());
		result = prime * result + (_crossEndB == null ? 0 : _crossEndB.hashCode());
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
		Arrow other = (Arrow) obj;
		if (_arcEnd != other._arcEnd) {
			return false;
		}
		if (_arcStart != other._arcStart) {
			return false;
		}
		if (_arrowEndA == null) {
			if (other._arrowEndA != null) {
				return false;
			}
		}
		else if (!_arrowEndA.equals(other._arrowEndA)) {
			return false;
		}
		if (_arrowEndB == null) {
			if (other._arrowEndB != null) {
				return false;
			}
		}
		else if (!_arrowEndB.equals(other._arrowEndB)) {
			return false;
		}
		if (_arrowType == null) {
			if (other._arrowType != null) {
				return false;
			}
		}
		else if (!_arrowType.equals(other._arrowType)) {
			return false;
		}
		if (_crossEndA == null) {
			if (other._crossEndA != null) {
				return false;
			}
		}
		else if (!_crossEndA.equals(other._crossEndA)) {
			return false;
		}
		if (_crossEndB == null) {
			if (other._crossEndB != null) {
				return false;
			}
		}
		else if (!_crossEndB.equals(other._crossEndB)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Type: " + _arrowType + " / Coordinates: " + x + "," + y;
	}
}
