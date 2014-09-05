package com.baselet.elementnew.facet.common;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.PropertiesParserState;

public class SeparatorLineWithHalignChangeFacet extends SeparatorLineFacet {

	public static SeparatorLineWithHalignChangeFacet INSTANCE = new SeparatorLineWithHalignChangeFacet();

	private SeparatorLineWithHalignChangeFacet() {}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		state.sethAlign(AlignHorizontal.LEFT);
		super.handleLine(line, drawer, state);
	}

}
