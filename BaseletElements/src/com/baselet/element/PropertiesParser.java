package com.baselet.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.ElementStyle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.GlobalFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;

/**
 * The PropertiesParser analyzes the Properties and the Settings (therefore also the Facets) of a GridElement.
 * It changes several things of the GridElement:
 * a. The state of the DrawHandler of the element including printing the main-text of the element and executing several facet commands (e.g. -- should be drawn as a horizontal line)
 * b. The PropertiesParserState which is mainly used during the parsing, but also important for the GridElement to recognize the currently set ElementStyle, BackgroundColor and more.
 * c. The size of the GridElement if the ElementStyle facet is set to AUTORESIZE
 *
 * A summary of the process is the following:
 * 1. check if the ElementStyle property is set to AUTORESIZE.
 * 1.1. If yes a complete dummy-parse is required to calculate the expected element size. The new size of the element gets applied and the PropertiesParserState gets updated
 * 2. The Global Facets get parsed and applied in order of their PriorityEnum value (this largely reduces the size of the remaining properties for the other Facets) also Comments are removed here
 * 3. The common content of the element is drawn (e.g. the border) (therefore this must happen AFTER parsing the Global Facets because they can change the bg-color and so on)
 * 4. The remaining properties are parsed and the remaining Facets are applied. Also the main-text is printed if the element demands it (some elements like Relation don't have a main-text)
 */
public class PropertiesParser {

	public static void parseProperties_handleFacets_drawText(NewGridElement element, PropertiesParserState state) {
		List<String> propertiesText = element.getPanelAttributesAsList();
		autoresizeAnalysis(element, state.getSettings(), propertiesText); // at first handle autoresize (which possibly changes elementsize)
		state.resetValues(element.getRealSize()); // now that the element size is known, reset the state with it
		List<String> propTextWithoutGobalFacets = parseGlobalFacets(propertiesText, state.getSettings().getGlobalFacets(), state, element.getDrawer()); // must be before element.drawCommonContent (because bg=... and other settings are set here)
		element.resetMetaDrawerAndDrawCommonContent(); // draw common content like border around classes
		drawPropertiesWithoutGlobalFacets(propTextWithoutGobalFacets, element.getDrawer(), state); // iterate over propertiestext and draw text and resolve localfacets
	}

	private static void autoresizeAnalysis(NewGridElement element, Settings settings, List<String> propertiesText) {
		DrawHandler pseudoDrawer = element.getDrawer().getPseudoDrawHandler();
		PropertiesParserState tmpstate = new PropertiesParserState(settings, element.getRealSize()); // we use a tmpstate to parse global facets to see if autoresize is enabled
		List<String> tmpPropTextWithoutGlobalFacets = parseGlobalFacets(propertiesText, tmpstate.getSettings().getGlobalFacets(), tmpstate, pseudoDrawer);

		if (tmpstate.getElementStyle() == ElementStyle.AUTORESIZE) { // only in case of autoresize element, we must proceed to calculate elementsize and resize it
			element.drawCommonContent(pseudoDrawer, tmpstate);
			drawPropertiesWithoutGlobalFacets(tmpPropTextWithoutGlobalFacets, pseudoDrawer, tmpstate);
			double textHeight = tmpstate.getyPos() - pseudoDrawer.textHeightMax(); // subtract last ypos step to avoid making element too high (because the print-text pos is always on the bottom)
			double width = tmpstate.getCalculatedElementWidth();
			element.handleAutoresize(new DimensionDouble(width, textHeight), tmpstate.gethAlign());
		}
	}

	private static List<String> parseGlobalFacets(List<String> propertiesText, List<GlobalFacet> list, PropertiesParserState state, DrawHandler drawer) {
		List<String> propertiesCopy = new ArrayList<String>(propertiesText);
		for (GlobalFacet facet : list) {
			for (Iterator<String> iter = propertiesCopy.iterator(); iter.hasNext();) {
				String line = iter.next();
				boolean facetUsed = parseFacets(Arrays.asList(facet), line, drawer, state);
				if (facetUsed) {
					iter.remove();
				}
			}
		}
		state.informAndClearUsedFacets(drawer);
		return propertiesCopy;
	}

	private static void drawPropertiesWithoutGlobalFacets(List<String> propertiesTextWithoutGobalFacets, DrawHandler drawer, PropertiesParserState state) {
		double startPointFromVAlign = calcStartPointFromVAlign(propertiesTextWithoutGobalFacets, drawer, state);
		state.addToYPos(PropertiesParserUtils.calcTopDisplacementToFitLine(propertiesTextWithoutGobalFacets, startPointFromVAlign, state, drawer));
		handleProperties(propertiesTextWithoutGobalFacets, state.getSettings().getLocalFacets(), drawer, state);
	}

	private static double calcStartPointFromVAlign(List<String> propertiesText, DrawHandler drawer, PropertiesParserState state) {
		double returnVal = drawer.textHeightMax(); // print method is located at the bottom of the text therefore add text height (important for UseCase etc where text must not reach out of the border)
		if (state.getvAlign() == AlignVertical.TOP) {
			returnVal += drawer.getDistanceBorderToText() + state.getTopBuffer();
		}
		else if (state.getvAlign() == AlignVertical.CENTER) {
			returnVal += (state.getGridElementSize().height - getTextBlockHeight(propertiesText, drawer, state)) / 2 + state.getTopBuffer() / 2;
		}
		else /* if (state.getvAlign() == AlignVertical.BOTTOM) */{
			returnVal += state.getGridElementSize().height - getTextBlockHeight(propertiesText, drawer, state) - drawer.textHeightMax() / 4; // 1/4 of textheight is a good value for large fontsizes and "deep" characters like "y"
		}
		return returnVal;
	}

	private static double getTextBlockHeight(List<String> propertiesText, DrawHandler drawer, PropertiesParserState state) {
		PropertiesParserState tmpstate = new PropertiesParserState(state.getSettings(), state.getGridElementSize()); // a dummy state copy is used for calculation to make sure the textBlockHeight calculation doesn't change the real state
		tmpstate.setElementStyle(state.getElementStyle()); // elementstyle is important for calculation (because of wordwrap)
		handleProperties(propertiesText, tmpstate.getSettings().getLocalFacets(), drawer.getPseudoDrawHandler(), tmpstate);
		return tmpstate.getyPos();
	}

	private static void handleProperties(List<String> propertiesText, List<Facet> facets, DrawHandler drawer, PropertiesParserState state) {
		boolean wordwrap = state.getElementStyle() == ElementStyle.WORDWRAP;
		for (String line : propertiesText) {
			if (wordwrap && !line.trim().isEmpty()) { // empty lines are skipped (otherwise they would get lost)
				handleLineWithWordWrap(facets, line, drawer, state);
			}
			else {
				parseFacets(facets, line, drawer, state);
			}
		}
		state.informAndClearUsedFacets(drawer);
	}

	private static void handleLineWithWordWrap(List<Facet> facets, String line, DrawHandler drawer, PropertiesParserState state) {
		String wrappedLine;
		while (state.getyPos() < state.getGridElementSize().height && !line.trim().isEmpty()) {
			double spaceForText = state.getXLimitsForArea(state.getyPos(), drawer.textHeightMax(), false).getSpace() - drawer.getDistanceBorderToText() * 2;
			wrappedLine = TextSplitter.splitString(line, spaceForText, drawer);
			parseFacets(facets, wrappedLine, drawer, state);
			line = line.substring(wrappedLine.length()).trim();
		}
	}

	private static boolean parseFacets(List<? extends Facet> facets, String line, DrawHandler drawer, PropertiesParserState state) {
		for (Facet f : facets) {
			if (f.checkStart(line, state)) {
				f.handleLine(line, drawer, state);
				state.addUsedFacet(f);
				return true;
			}
		}
		return false;
	}

}
