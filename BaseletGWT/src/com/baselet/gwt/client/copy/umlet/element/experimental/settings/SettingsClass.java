package com.baselet.gwt.client.copy.umlet.element.experimental.settings;

import com.baselet.gwt.client.copy.control.enumerations.AlignHorizontal;
import com.baselet.gwt.client.copy.control.enumerations.AlignVertical;
import com.baselet.gwt.client.copy.diagram.draw.geom.LineHorizontal;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.facets.ActiveClass;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.facets.Facet;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.facets.InnerClass;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.facets.SeparatorLine;
import com.baselet.gwt.client.copy.umlet.element.experimental.settings.facets.TemplateClass;

public class SettingsClass extends Settings {

	@Override
	public LineHorizontal getXValues(float y, int height, int width) {
		return new LineHorizontal(0, width);
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
	public Facet[] createFacets() {
		return new Facet[]{new ActiveClass(), new InnerClass(), new SeparatorLine(true), new TemplateClass()};
	}

}
