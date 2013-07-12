package com.umlet.element.experimental.element.generic;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.facets.DefaultGlobalTextFacet.ElementStyleEnum;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.SeparatorLine;
import com.umlet.element.experimental.settings.Settings;

public class Text extends NewGridElement {

	private static final Settings settings = new Settings() {
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
			return AlignHorizontal.LEFT;
		}
		@Override
		public ElementStyleEnum getElementStyle() {
			return ElementStyleEnum.WORDWRAP;
		}
		@Override
		public Facet[] createFacets() {
			return new Facet[]{new SeparatorLine(true)};
		}
	};

	@Override
	public ElementId getId() {
		return ElementId.Text;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		properties.drawPropertiesText();
	}

	@Override
	public Settings getSettings() {
		return settings;
	}

}

