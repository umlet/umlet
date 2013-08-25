package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.Settings;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

public class UseCase extends NewGridElement {

	@Override
	public ElementId getId() {
		return ElementId.UMLUseCase;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		drawer.drawEllipse(0, 0, getRealSize().width-1, getRealSize().height-1);
	}


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


	@Override
	protected Settings createSettings() {
		return new Settings() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				double b = height/2.0f;
				double a = width/2.0f;
				double x = Math.sqrt((1-(Math.pow(b-y, 2) / Math.pow(b, 2)))*Math.pow(a, 2));
				return new XValues(a-x, a+x);
			}
			@Override
			public AlignVertical getVAlign() {
				return AlignVertical.CENTER;
			}
			@Override
			public AlignHorizontal getHAlign() {
				return AlignHorizontal.CENTER;
			}
			@Override
			public ElementStyleEnum getElementStyle() {
				return ElementStyleEnum.RESIZE;
			}
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLine.INSTANCE);
			}
		};
	}
}

