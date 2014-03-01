package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsAutoresize;

public class Timer extends NewGridElement {

	private static final int CLOCK_DIM = 40;

	@Override
	public ElementId getId() {
		return ElementId.UMLTimer;
	}
	
	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		StickingPolygon p = new StickingPolygon(rect.x, rect.y);
		p.addPoint(xClock(), 0);
		p.addPoint(x2Clock(), CLOCK_DIM);
		p.addPoint(xClock(), CLOCK_DIM);
		p.addPoint(x2Clock(), 0, true);
		return p;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		propCfg.addToYPos(CLOCK_DIM);
		propCfg.updateCalculatedElementWidth(CLOCK_DIM);
		drawer.drawLines(Arrays.asList(new PointDouble(xClock(), 0), new PointDouble(x2Clock(), CLOCK_DIM), new PointDouble(xClock(), CLOCK_DIM), new PointDouble(x2Clock(), 0), new PointDouble(xClock(), 0)));
	}

	private int x2Clock() {
		return xClock()+CLOCK_DIM;
	}

	private int xClock() {
		return (getRealSize().width-CLOCK_DIM)/2;
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
