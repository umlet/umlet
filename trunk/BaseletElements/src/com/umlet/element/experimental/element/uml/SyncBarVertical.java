package com.umlet.element.experimental.element.uml;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.StickingPolygonGenerator;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.common.LineThicknessFacet;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsNoText;

public class SyncBarVertical extends NewGridElement {

	private StickingPolygonGenerator syncBarStickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			StickingPolygon p = new StickingPolygon(rect.x, rect.y);
			double lt = getDrawer().getCurrentStyle().getLineThickness();
			double halfWidth = getRealSize().getWidth()/2;
			p.addRectangle(new Rectangle(halfWidth-lt/2, 0.0, lt, (double) getRealSize().getHeight()));
			return p;
		}
	};

	@Override
	public ElementId getId() {
		return ElementId.UMLSyncBarVertical;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		if (drawer.getCurrentStyle().getLineThickness() == LineThicknessFacet.DEFAULT_LINE_THICKNESS) {
			drawer.setLineThickness(5);
		}
		Dimension s = getRealSize();
		drawer.drawLine(s.getWidth()/2, 0, s.getWidth()/2, s.getHeight());
		propCfg.setStickingPolygonGenerator(syncBarStickingPolygonGenerator);
	}

	@Override
	protected Settings createSettings() {
		return new SettingsNoText() {
			@Override
			public List<? extends Facet> createFacets() {
				return Collections.<Facet>emptyList();
			}
		};
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> returnSet = super.getResizeArea(x, y);
		returnSet.remove(Direction.LEFT);
		returnSet.remove(Direction.RIGHT);
		return returnSet;
	}
}
