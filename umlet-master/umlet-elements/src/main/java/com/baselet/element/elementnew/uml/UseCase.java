package com.baselet.element.elementnew.uml;

import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.NewGridElement;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineFacet;
import com.baselet.element.settings.SettingsManualresizeCenter;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;

public class UseCase extends NewGridElement {

	private final StickingPolygonGenerator stickingPolygonGenerator = new StickingPolygonGenerator() {
		@Override
		public StickingPolygon generateStickingBorder(Rectangle rect) {
			StickingPolygon p = new StickingPolygon(rect.x, rect.y);

			p.addPoint(rect.width / 4.0, 0);
			p.addPoint(rect.width * 3.0 / 4, 0);

			p.addPoint(rect.width, rect.height / 4.0);
			p.addPoint(rect.width, rect.height * 3.0 / 4);

			p.addPoint(rect.width * 3.0 / 4, rect.height);
			p.addPoint(rect.width / 4.0, rect.height);

			p.addPoint(0, rect.height * 3.0 / 4);
			p.addPoint(0, (int) (rect.height / 4.0), true);

			return p;
		}
	};

	@Override
	public ElementId getId() {
		return ElementId.UMLUseCase;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
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
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), SeparatorLineFacet.INSTANCE);
			}
		};
	}
}
