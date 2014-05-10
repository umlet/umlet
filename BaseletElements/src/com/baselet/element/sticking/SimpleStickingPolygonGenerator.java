package com.baselet.element.sticking;

import com.baselet.diagram.draw.geom.Rectangle;

public class SimpleStickingPolygonGenerator implements StickingPolygonGenerator {

	public static final SimpleStickingPolygonGenerator INSTANCE = new SimpleStickingPolygonGenerator();

	private SimpleStickingPolygonGenerator() {}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		StickingPolygon p = new StickingPolygon(rect.x, rect.y);
		p.addRectangle(0, 0, rect.width, rect.height);
		return p;
	}
}
