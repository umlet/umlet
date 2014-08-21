package com.baselet.elementnew.element.uml;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsAutoresize;

public class Actor extends NewGridElement {

	private final static double ACTOR_DIMENSION = 14;

	private final StickingPolygonGenerator actorStickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			double dimension = ACTOR_DIMENSION;
			double hCenter = getRealSize().width / 2;
			int left = SharedUtils.realignToGrid(false, hCenter - armLength(dimension), false);
			int right = SharedUtils.realignToGrid(false, hCenter + armLength(dimension), true);
			int head = (int) headToLegLength(dimension);

			StickingPolygon p = new StickingPolygon(rect.x, rect.y);
			p.addPoint(left, 0);
			p.addPoint(right, 0);
			p.addPoint(right, head);
			p.addPoint(left, head, true);
			return p;
		}
	};

	@Override
	protected Settings createSettings() {
		return new SettingsAutoresize();
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLActor;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		double dimension = ACTOR_DIMENSION;
		state.addToYPos(headToLegLength(dimension));
		state.updateCalculatedElementWidth(armLength(dimension) * 2);

		drawActor(drawer, getRealSize().width / 2, 0, dimension);

		state.setStickingPolygonGenerator(actorStickingPolygonGenerator);
	}

	public static void drawActor(DrawHandler drawer, int hCenter, int yTop, double dimension) {
		drawer.drawCircle(hCenter, yTop + headRadius(dimension), headRadius(dimension)); // Head
		drawer.drawLine(hCenter - armLength(dimension), yTop + armHeight(dimension), hCenter + armLength(dimension), yTop + armHeight(dimension)); // Arms
		drawer.drawLine(hCenter, yTop + headRadius(dimension) * 2, hCenter, yTop + headToBodyLength(dimension)); // Body
		drawer.drawLine(hCenter, yTop + headToBodyLength(dimension), hCenter - legSpan(dimension), yTop + headToLegLength(dimension)); // Legs
		drawer.drawLine(hCenter, yTop + headToBodyLength(dimension), hCenter + legSpan(dimension), yTop + headToLegLength(dimension)); // Legs
	}

	private static double headToLegLength(double dimension) {
		return legSpan(dimension) * 2 + headToBodyLength(dimension);
	}

	private static double legSpan(double dimension) {
		return dimension;
	}

	private static double headToBodyLength(double dimension) {
		return dimension * 2 + headRadius(dimension) * 2;
	}

	private static double armHeight(double dimension) {
		return armLength(dimension);
	}

	private static double armLength(double dimension) {
		return dimension * 1.5;
	}

	private static double headRadius(double dimension) {
		return dimension / 2;
	}
}
