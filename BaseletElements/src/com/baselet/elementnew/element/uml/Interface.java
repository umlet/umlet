package com.baselet.elementnew.element.uml;

import com.baselet.control.geom.Rectangle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.base.ElementId;
import com.baselet.elementnew.facet.PropertiesParserState;
import com.baselet.elementnew.facet.Settings;
import com.baselet.elementnew.settings.SettingsAutoresize;

public class Interface extends NewGridElement {

	private static final int TOP_DISTANCE = 10;
	private static final int CIRCLE_SIZE = 20;

	private final StickingPolygonGenerator interfacePolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			StickingPolygon p = new StickingPolygon(rect.x, rect.y);
			p.addRectangle(circleRect());
			return p;
		}
	};

	@Override
	public ElementId getId() {
		return ElementId.UMLInterface;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		state.addToYPos(TOP_DISTANCE + CIRCLE_SIZE);// space reserved for the top circle
		Rectangle circleRect = circleRect();
		drawer.drawCircle(circleRect.x + CIRCLE_SIZE * 0.5, circleRect.y + CIRCLE_SIZE * 0.5, CIRCLE_SIZE * 0.5);

		state.setStickingPolygonGenerator(interfacePolygonGenerator);
	}

	private Rectangle circleRect() {
		int middlePos = (int) (getRealSize().getWidth() * 0.5 - CIRCLE_SIZE * 0.5);
		return new Rectangle(middlePos, TOP_DISTANCE, CIRCLE_SIZE, CIRCLE_SIZE);
	}

	@Override
	protected Settings createSettings() {
		return new SettingsAutoresize();
	}
}
