package com.baselet.element.elementnew.uml;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.draw.DrawHelper;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.settings.SettingsAutoresize;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;

public class Actor extends NewGridElement {

	private final static double ACTOR_DIMENSION = 14;

	private final StickingPolygonGenerator actorStickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			double dimension = ACTOR_DIMENSION;
			double hCenter = getRealSize().width / 2.0;
			int left = SharedUtils.realignToGrid(false, hCenter - DrawHelper.armLength(dimension), false);
			int right = SharedUtils.realignToGrid(false, hCenter + DrawHelper.armLength(dimension), true);
			int head = (int) DrawHelper.headToLegLength(dimension);

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
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		double dimension = ACTOR_DIMENSION;
		state.updateMinimumSize(DrawHelper.armLength(dimension) * 2, DrawHelper.headToLegLength(dimension));

		DrawHelper.drawActor(drawer, getRealSize().width / 2, 0, dimension);

		state.setStickingPolygonGenerator(actorStickingPolygonGenerator);
	}
}
