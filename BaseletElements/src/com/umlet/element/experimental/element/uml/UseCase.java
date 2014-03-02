package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.StickingPolygonGenerator;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.common.SeparatorLineFacet;
import com.umlet.element.experimental.facet.specific.TitleFacet;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsManualresize;

public class UseCase extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLUseCase;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		drawer.drawEllipse(0, 0, getRealSize().width-1, getRealSize().height-1);
		propCfg.setStickingPolygonGenerator(new StickingPolygonGenerator() {
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
		});
	}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresize() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				return XValues.createForEllipse(y, height, width);
			}
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLineFacet.INSTANCE, TitleFacet.INSTANCE);
			}
		};
	}
}

