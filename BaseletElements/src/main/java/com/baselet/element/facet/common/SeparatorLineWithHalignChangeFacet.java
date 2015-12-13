package com.baselet.element.facet.common;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.element.facet.PropertiesParserState;

public class SeparatorLineWithHalignChangeFacet extends SeparatorLineFacet {

	public static final SeparatorLineWithHalignChangeFacet INSTANCE = new SeparatorLineWithHalignChangeFacet();

	private SeparatorLineWithHalignChangeFacet() {}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		state.getAlignment().setHorizontal(false, AlignHorizontal.LEFT);
		super.handleLine(line, state);
	}

}
