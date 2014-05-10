package com.baselet.elementnew.element.uml;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.StickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsAutoresize;

public class Actor extends NewGridElement {

	private final static double ACTOR_DIMENSION = 14;

	private StickingPolygonGenerator actorStickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			double hCenter = getRealSize().width / 2;
			int left = SharedUtils.realignToGrid(false, hCenter - armLength(), false);
			int right = SharedUtils.realignToGrid(false, hCenter + armLength(), true);
			int head = (int) headToLegLength();

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
		state.addToYPos(headToLegLength());
		state.updateCalculatedElementWidth(armLength() * 2);

		int hCenter = getRealSize().width / 2;
		drawer.drawCircle(hCenter, headRadius(), headRadius()); // Head
		drawer.drawLine(hCenter - armLength(), armHeight(), hCenter + armLength(), armHeight()); // Arms
		drawer.drawLine(hCenter, headRadius() * 2, hCenter, headToBodyLength()); // Body
		drawer.drawLine(hCenter, headToBodyLength(), hCenter - legSpan(), headToLegLength()); // Legs
		drawer.drawLine(hCenter, headToBodyLength(), hCenter + legSpan(), headToLegLength()); // Legs

		state.setStickingPolygonGenerator(actorStickingPolygonGenerator);
	}

	private static double headToLegLength() {
		return legSpan() * 2 + headToBodyLength();
	}

	private static double legSpan() {
		return ACTOR_DIMENSION;
	}

	private static double headToBodyLength() {
		return ACTOR_DIMENSION * 2 + headRadius() * 2;
	}

	private static double armHeight() {
		return armLength();
	}

	private static double armLength() {
		return ACTOR_DIMENSION * 1.5;
	}

	private static double headRadius() {
		return ACTOR_DIMENSION / 2;
	}
}
