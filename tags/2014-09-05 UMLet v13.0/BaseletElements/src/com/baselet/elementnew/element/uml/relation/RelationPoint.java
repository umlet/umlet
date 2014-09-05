package com.baselet.elementnew.element.uml.relation;

import com.baselet.diagram.draw.geom.Rectangle;

public class RelationPoint {
	static final Rectangle DEFAULT_SIZE = new Rectangle(-RelationPointHandler.POINT_SELECTION_RADIUS, -RelationPointHandler.POINT_SELECTION_RADIUS, RelationPointHandler.POINT_SELECTION_RADIUS * 2, RelationPointHandler.POINT_SELECTION_RADIUS * 2);
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
