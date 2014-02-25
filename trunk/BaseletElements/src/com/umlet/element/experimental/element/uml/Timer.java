package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
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
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		int xClock = (getRealSize().width-CLOCK_DIM)/2;
		int x2Clock = xClock+CLOCK_DIM;
		drawer.drawLines(Arrays.asList(new PointDouble(xClock, 0), new PointDouble(x2Clock, CLOCK_DIM), new PointDouble(xClock, CLOCK_DIM), new PointDouble(x2Clock, 0), new PointDouble(xClock, 0)));
	}

	@Override
	protected Settings createSettings() {
		return new SettingsAutoresize() {
			@Override
			public double getYPosStart() {
				return CLOCK_DIM;
			}
			@Override
			public double getMinElementWidthForAutoresize() {
				return CLOCK_DIM;
			}
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLine.INSTANCE);
			}
		};
	}
}
