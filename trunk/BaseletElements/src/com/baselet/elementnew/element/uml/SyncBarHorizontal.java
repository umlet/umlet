package com.baselet.elementnew.element.uml;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.StickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.LineThicknessFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsNoText;

public class SyncBarHorizontal extends NewGridElement {

	private StickingPolygonGenerator syncBarStickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			StickingPolygon p = new StickingPolygon(rect.x, rect.y);
			double lt = getDrawer().getCurrentStyle().getLineThickness();
			double halfHeight = getRealSize().getHeight()/2;
			p.addRectangle(new Rectangle(0.0, halfHeight-lt/2, (double) getRealSize().getWidth(), lt));
			return p;
		}
	};

	@Override
	public ElementId getId() {
		return ElementId.UMLSyncBarHorizontal;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		if (drawer.getCurrentStyle().getLineThickness() == LineThicknessFacet.DEFAULT_LINE_THICKNESS) {
			drawer.setLineThickness(5);
		}
		Dimension s = getRealSize();
		drawer.drawLine(0, s.getHeight()/2, s.getWidth(), s.getHeight()/2);
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
		returnSet.remove(Direction.UP);
		returnSet.remove(Direction.DOWN);
		return returnSet;
	}
}
