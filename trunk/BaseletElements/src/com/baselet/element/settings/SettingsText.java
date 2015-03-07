package com.baselet.element.settings;

import java.util.List;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.ElementStyle;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineFacet;

public class SettingsText extends Settings {

	@Override
	public ElementStyle getElementStyle() {
		return ElementStyle.WORDWRAP;
	}

	@Override
	protected List<Facet> createFacets() {
		return listOf(Settings.MANUALRESIZE_WITH_STYLE, SeparatorLineFacet.INSTANCE);
	}

	@Override
	public AlignHorizontal getHAlign() {
		return AlignHorizontal.LEFT;
	}

}
