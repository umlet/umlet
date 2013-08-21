package com.umlet.element.experimental.element.uml;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.Settings;
import com.umlet.element.experimental.facets.ActiveClass;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.InnerClass;
import com.umlet.element.experimental.facets.SeparatorLine;
import com.umlet.element.experimental.facets.TemplateClass;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;


public class Class extends NewGridElement {

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
				return ElementStyleEnum.RESIZE;
			}
			@Override
			public Facet[] createFacets() {
				return new Facet[]{new InnerClass(), new SeparatorLine(true), new ActiveClass(), new TemplateClass()};
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLClass;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		drawer.drawRectangle(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawPropertiesText();
	}
}

