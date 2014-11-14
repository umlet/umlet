package com.baselet.element.relation.helper;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.sticking.PointDoubleIndexed;

public class RelationPoint {
	public static final Rectangle DEFAULT_SIZE = new Rectangle(-RelationPointConstants.POINT_SELECTION_RADIUS, -RelationPointConstants.POINT_SELECTION_RADIUS, RelationPointConstants.POINT_SELECTION_RADIUS * 2, RelationPointConstants.POINT_SELECTION_RADIUS * 2);
	private final PointDoubleIndexed point;
	private Rectangle size;

	public RelationPoint(int index, double x, double y) {
		this(index, x, y, DEFAULT_SIZE);
	}

	public RelationPoint(int index, double x, double y, Rectangle size) {
		super();
		point = new PointDoubleIndexed(index, x, y);
		this.size = size;
	}

	public PointDoubleIndexed getPoint() {
		return point;
	}

	public Rectangle getSize() {
		return size;
	}

	public void setSize(Rectangle size) {
		this.size = size;
	}

	public Rectangle getSizeAbsolute() {
		return new Rectangle(point.getX() + size.getX(), point.getY() + size.getY(), (double) size.getWidth(), (double) size.getHeight());
	}

	@Override
	public String toString() {
		return "RelationPoint [point=" + point + ", size=" + size + "]";
	}

}
