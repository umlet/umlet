package com.baselet.element.facet.common;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.PropertiesParserState;

public class SeparatorLineWithHalignChangeFacet extends SeparatorLineFacet {

	public static final SeparatorLineWithHalignChangeFacet INSTANCE = new SeparatorLineWithHalignChangeFacet();

	private SeparatorLineWithHalignChangeFacet() {}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		state.sethAlign(AlignHorizontal.LEFT);
		super.handleLine(line, drawer, state);
	}

}
