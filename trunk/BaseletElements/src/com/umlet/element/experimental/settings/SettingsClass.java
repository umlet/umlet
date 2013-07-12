package com.umlet.element.experimental.settings;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.ActiveClass;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.InnerClass;
import com.umlet.element.experimental.facets.SeparatorLine;
import com.umlet.element.experimental.facets.TemplateClass;
import com.umlet.element.experimental.facets.DefaultGlobalTextFacet.ElementStyleEnum;

public class SettingsClass extends Settings {

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

}
