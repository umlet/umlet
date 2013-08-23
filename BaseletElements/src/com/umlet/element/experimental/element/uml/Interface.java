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

public class Interface extends NewGridElement {

	private int TOP_DISTANCE = 10;
	private int CIRCLE_SIZE = 20;

	@Override
	public ElementId getId() {
		return ElementId.UMLInterface;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		properties.drawPropertiesText();
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
		return new Settings() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				return new XValues(0, width);
			}
			@Override
			public AlignVertical getVAlign() {
				return AlignVertical.TOP;
			}
			@Override
			public AlignHorizontal getHAlign() {
				return AlignHorizontal.CENTER;
			}
			@Override
			public ElementStyleEnum getElementStyle() {
				return ElementStyleEnum.AUTORESIZE;
			}
			@Override
			public double getYPosStart() {
				return TOP_DISTANCE + CIRCLE_SIZE; // space reserved for the top circle
			}
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLine.INSTANCE);
			}
		};
	}
}
