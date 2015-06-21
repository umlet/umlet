package com.baselet.element.facet.specific.sequence_aio;

import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.FirstRunFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

/**
 * must be in first run, because the execution of Class.drawCommonContent() depends on the result of this facet
 */
public class SequenceAllInOneFacet extends FirstRunFacet {

	public static final SequenceAllInOneFacet INSTANCE = new SequenceAllInOneFacet();

	private SequenceAllInOneFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		// consume every line that wasn't handled by another facet
		return true;
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		// everything is done in parsingFinished
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		DrawHandler drawer = state.getDrawer();
		int height = state.getGridElementSize().getHeight();
		int width = state.getGridElementSize().getWidth();

		// pass the whole text to the parser
		StringBuilder strBuilder = new StringBuilder();
		for (String str : handledLines) {
			strBuilder.append(str);
			strBuilder.append('\n');
		}

	}

}
