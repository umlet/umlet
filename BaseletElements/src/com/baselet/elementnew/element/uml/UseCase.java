package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualresizeCenter;

public class UseCase extends NewGridElement {

	private final StickingPolygonGenerator stickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			StickingPolygon p = new StickingPolygon(rect.x, rect.y);

			p.addPoint(rect.width / 4, 0);
			p.addPoint(rect.width * 3 / 4, 0);

			p.addPoint(rect.width, rect.height / 4);
			p.addPoint(rect.width, rect.height * 3 / 4);

			p.addPoint(rect.width * 3 / 4, rect.height);
			p.addPoint(rect.width / 4, rect.height);

			p.addPoint(0, rect.height * 3 / 4);
			p.addPoint(0, rect.height / 4, true);

			return p;
		}
	};

	@Override
	public ElementId getId() {
		return ElementId.UMLUseCase;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		drawer.drawEllipse(0, 0, getRealSize().width, getRealSize().height);
		state.setStickingPolygonGenerator(stickingPolygonGenerator);
	}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				return XValues.createForEllipse(y, height, width);
			}

			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLineFacet.INSTANCE);
			}
		};
	}
}
