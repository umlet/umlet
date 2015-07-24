package com.baselet.element.facet.specific.sequence_aio;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.specific.sequence_aio.gen.ParseException;
import com.baselet.element.facet.specific.sequence_aio.gen.SequenceAllInOneParser;
import com.baselet.element.facet.specific.sequence_aio.gen.TokenMgrException;
import com.baselet.gui.AutocompletionText;

public class SequenceAllInOneFacet extends Facet {

	public static final SequenceAllInOneFacet INSTANCE = new SequenceAllInOneFacet();

	private SequenceAllInOneFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		// consume every line that wasn't handled by another facet
		return true;
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Collections.emptyList();
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
		try {
			System.out.println(new Date());
			new SequenceAllInOneParser(strBuilder.toString()).start().generateDiagram().draw(drawer);
		} catch (ParseException e) {
			throw new SequenceDiagramException(e);
		} catch (TokenMgrException e) {
			throw new SequenceDiagramException(e);
		}

	}
}
