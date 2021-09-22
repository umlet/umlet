package com.baselet.element.facet.advancedelements;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.enums.Priority;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class AdvancedDrawingsFacet extends Facet {

	public static final AdvancedDrawingsFacet INSTANCE = new AdvancedDrawingsFacet();
	public static final Logger logger = LoggerFactory.getLogger(AdvancedDrawingsFacet.class);
	public static final String CODE_SEP_START = "_umletcode=";
	public static final String CODE_START_INFO = "indicates the start of advanced elements commands, has no close command.";

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		Object objIsActive = state.getFacetResponse(AdvancedDrawingsFacet.class, false);
		if (objIsActive instanceof Boolean && (Boolean) objIsActive) {
			return true;
		}
		else if (CODE_SEP_START.equals(line)) {
			state.setFacetResponse(AdvancedDrawingsFacet.class, true);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		// everything is done in parsingFinished
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		if (handledLines != null && !handledLines.isEmpty()) {
			state.getDrawer().getJavascriptCodeParser().parse(getCodeString(handledLines), state.getGridElementSize().getWidth(), state.getGridElementSize().getHeight());
		}
	}

	private String getCodeString(List<String> handledLines) {
		handledLines.remove(0); // remove '_umletcode=' prefix
		StringBuilder code = new StringBuilder();
		for (String str : handledLines) {
			code.append(str);
			code.append('\n');
		}
		return code.toString();
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> autocompletionList = new LinkedList<AutocompletionText>();
		autocompletionList.add(new AutocompletionText(CODE_SEP_START, CODE_START_INFO));

		return autocompletionList;
	}

	@Override
	public boolean handleOnFirstRun() {
		return true;
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

}
