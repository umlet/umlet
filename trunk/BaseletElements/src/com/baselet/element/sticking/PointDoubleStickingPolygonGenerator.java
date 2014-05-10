package com.baselet.element.sticking;

import java.util.List;

import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;

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
