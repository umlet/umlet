package com.baselet.element.settings;

import java.util.List;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.element.facet.ElementStyleEnum;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineFacet;

public class SettingsText extends Settings {

	@Override
	public ElementStyleEnum getElementStyle() {
		return ElementStyleEnum.WORDWRAP;
	}

	@Override
	protected List<Facet> createFacets() {
		return listOf(Settings.MANUALRESIZE, SeparatorLineFacet.INSTANCE);
	}

	@Override
	public AlignHorizontal getHAlign() {
		return AlignHorizontal.LEFT;
	}

}
