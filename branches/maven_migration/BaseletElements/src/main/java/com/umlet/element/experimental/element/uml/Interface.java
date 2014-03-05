package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsAutoresize;

public class Interface extends NewGridElement {

	private int TOP_DISTANCE = 10;
	private int CIRCLE_SIZE = 20;

	@Override
	public ElementId getId() {
		return ElementId.UMLInterface;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		propCfg.addToYPos(TOP_DISTANCE + CIRCLE_SIZE);// space reserved for the top circle
		Rectangle circleRect = circleRect();
		drawer.drawCircle(circleRect.x + CIRCLE_SIZE/2, circleRect.y + CIRCLE_SIZE/2, CIRCLE_SIZE/2);
	}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		StickingPolygon p = new StickingPolygon(rect.x, rect.y);
		p.addRectangle(circleRect());
		return p;
	}

	private Rectangle circleRect() {
		int middlePos = getRealSize().getWidth() / 2 - CIRCLE_SIZE/2;
		return new Rectangle(middlePos, TOP_DISTANCE, CIRCLE_SIZE-1, CIRCLE_SIZE-1);
	}

	@Override
	protected Settings createSettings() {
		return new SettingsAutoresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLine.INSTANCE);
			}
		};
	}
}
