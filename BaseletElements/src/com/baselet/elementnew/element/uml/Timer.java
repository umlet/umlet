package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.StickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsAutoresize;

public class Timer extends NewGridElement {

	private static final int CLOCK_DIM = 40;

	private final StickingPolygonGenerator timerStickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			StickingPolygon p = new StickingPolygon(rect.x, rect.y);
			p.addPoint(xClock(), 0);
			p.addPoint(x2Clock(), CLOCK_DIM);
			p.addPoint(xClock(), CLOCK_DIM);
			p.addPoint(x2Clock(), 0, true);
			return p;
		}
	};
	
	@Override
	public ElementId getId() {
		return ElementId.UMLTimer;
	}
	
	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesConfig propCfg) {
		propCfg.addToYPos(CLOCK_DIM);
		propCfg.updateCalculatedElementWidth(CLOCK_DIM);
		drawer.drawLines(Arrays.asList(new PointDouble(xClock(), 0), new PointDouble(x2Clock(), CLOCK_DIM), new PointDouble(xClock(), CLOCK_DIM), new PointDouble(x2Clock(), 0), new PointDouble(xClock(), 0)));

		propCfg.setStickingPolygonGenerator(timerStickingPolygonGenerator);
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
				return Arrays.asList(SeparatorLineFacet.INSTANCE);
			}
		};
	}
}
