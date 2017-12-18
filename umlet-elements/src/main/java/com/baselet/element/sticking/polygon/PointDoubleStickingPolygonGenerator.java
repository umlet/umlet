package com.baselet.element.sticking.polygon;

import java.util.List;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;

public class PointDoubleStickingPolygonGenerator implements StickingPolygonGenerator {

	private List<PointDouble> points;

	public PointDoubleStickingPolygonGenerator(List<PointDouble> points) {
		super();
		this.points = points;
	}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		StickingPolygon p = new StickingPolygon(rect.x, rect.y);
		p.addPoint(points);
		return p;
	}
}
