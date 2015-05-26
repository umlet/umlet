package com.baselet.element.facet.customdrawings;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.enums.FormatLabels;
import com.baselet.control.enums.Priority;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.customdrawings.gen.ParseException;
import com.baselet.gui.AutocompletionText;

public class CustomDrawingFacet extends Facet {

	public static final CustomDrawingFacet INSTANCE = new CustomDrawingFacet();
	public static final Logger logger = Logger.getLogger(CustomDrawingFacet.class);
	public static final String CODE_SEP_START = "customelement=";
	public static final String CODE_START_INFO = "indicates the start of custom drawing commands, has no close command.";

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		Object objIsActive = state.getFacetResponse(CustomDrawingFacet.class, false);
		if (objIsActive instanceof Boolean && (Boolean) objIsActive) {
			return true;
		}
		else if (CODE_SEP_START.equals(line)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		if (CODE_SEP_START.equals(line)) {
			Object objIsActive = state.getFacetResponse(CustomDrawingFacet.class, false);
			if (objIsActive instanceof Boolean && (Boolean) objIsActive) {
				// custom commands are already turned on -> error
				throw new RuntimeException(FormatLabels.BOLD.getValue() + "Invalid value: " + FormatLabels.BOLD.getValue() + CODE_SEP_START + "\nDuplicate command. This command may only occur once.");
			}
			state.setFacetResponse(CustomDrawingFacet.class, true);
		}
		else {
			state.getGridElementSize().getWidth();
			try {
				new CustomDrawingParserImpl(line, state.getGridElementSize().getWidth(), state.getGridElementSize().getHeight(), state.getDrawer()).parse();
			} catch (ParseException e) {
				// remove the trailing \n from the error message
				throw new RuntimeException(e.getMessage().substring(0, e.getMessage().length() - 1));
			}
		}
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> autocompletionList = new LinkedList<AutocompletionText>();
		autocompletionList.add(new AutocompletionText(CODE_SEP_START, CODE_START_INFO));

		autocompletionList.add(new AutocompletionText("drawLine(x1, y1, x2, y2)", "Draws a line from (x1, y1) to (x2, y2). Supports fg, lt and lw option after the last bracket."));
		autocompletionList.add(new AutocompletionText("drawRectangle(x, y, width, height)", "Draws a rectangle with the top left corner at (x, y). Supports fg, bg, lt and lw option after the last bracket."));
		autocompletionList.add(new AutocompletionText("drawRectangleRound(x, y, width, height, radius)", "Draws a rectangle with round corners with the top left corner at (x, y). Supports fg, bg, lt and lw option after the last bracket."));
		autocompletionList.add(new AutocompletionText("drawCircle(x, y, radius)", "Draws a circle with the center at (x, y) and the given radius. Supports fg, bg, lt and lw option after the last bracket."));
		autocompletionList.add(new AutocompletionText("drawEllipse(x, y, width, height)", "Draws an ellipse where the top left corner of the surrounding rectangle is at (x, y). Supports fg, bg, lt and lw option after the last bracket."));
		autocompletionList.add(new AutocompletionText("drawArc(x, y, width, height, start, extent, open)", "Draws an elliptical arc where the top left corner of the surrounding rectangle is at (x, y). This arc starts at <start> degrees and ends at <extent> degrees. If open is false 2 lines are drawn from the center to the arc. Supports fg, bg, lt and lw option after the last bracket."));
		autocompletionList.add(new AutocompletionText("drawText(\"This is text!\", x, y, CENTER)", "Draws the text at (x, y) with the given horizontal alignment. Supports fg option after the last bracket."));

		return autocompletionList;
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}
}
