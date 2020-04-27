package com.baselet.element.sticking.polygon;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;

public class NoStickingPolygonGenerator implements StickingPolygonGenerator {

	public static final NoStickingPolygonGenerator INSTANCE = new NoStickingPolygonGenerator();

	private NoStickingPolygonGenerator() {}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		return new StickingPolygon(rect.x, rect.y);
	}
}
