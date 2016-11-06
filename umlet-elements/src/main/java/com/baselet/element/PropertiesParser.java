package com.baselet.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.enums.ElementStyle;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;

/**
 * The PropertiesParser analyzes the Properties and the Facets of a GridElement and handles AUTORESIZE
 * The Facets typically manipulates the DrawHandler state by executing the drawing methods (e.g. printing the main-text of the element, transforming -- to a horizontal line, ...)
 *
 * A summary of the process is the following:
 * 1. Do a complete parser run (without drawing anything) to calculate the textblock height and possibly start the AUTORESIZE calculation.
 * 2. The First-Run Facets get parsed and applied in order of their PriorityEnum value (this reduces the size of the remaining properties for the Second Run)
 * 3. The common content of the element is drawn (e.g. the border) (therefore this must happen AFTER parsing the First-Run Facets because they can change the bg-color and so on)
 * 4. The remaining properties are parsed and the Second-Run Facets are applied.
 */
public class PropertiesParser {

	public static void parsePropertiesAndHandleFacets(NewGridElement element, PropertiesParserState state) {
		List<String> propertiesText = element.getPanelAttributesAsList();
		doPreparsing(element, state, propertiesText); // at first handle autoresize (which possibly changes elementsize) and calc the textblock size
		parseFacets(element, state, propertiesText, true);
	}

	private static void doPreparsing(NewGridElement element, PropertiesParserState state, List<String> propertiesText) {
		state.resetValues(element.getRealSize(), state.getTotalTextBlockHeight(), false); // assume certain values and disable drawing for the preparsing step
		parseFacets(element, state, propertiesText, false);

		if (state.getElementStyle() == ElementStyle.AUTORESIZE) { // only in case of autoresize element, calculate the elementsize
			double width = state.getMinimumWidth();
			double height = state.getTextPrintPosition();
			element.handleAutoresize(new DimensionDouble(width, height), state.getAlignment().getHorizontal());
		}

		double textblockHeight = state.getTextPrintPosition() - state.getBuffer().getTop();
		state.resetValues(element.getRealSize(), textblockHeight, true); // now that the element size and textblock height is known, the state is reset once again with enabled drawing
	}

	private static void parseFacets(NewGridElement element, PropertiesParserState state, List<String> propertiesText, boolean drawMetaDrawer) {
		List<String> propertiesAfterFirstRun = parseFacets(state.getSettings().getFacetsForFirstRun(), propertiesText, state); // must be before element.drawCommonContent (because bg=... and other settings are set here)
		element.resetMetaDrawerAndDrawCommonContent(state, drawMetaDrawer); // draw common content like border around classes
		parseFacets(state.getSettings().getFacetsForSecondRun(), propertiesAfterFirstRun, state); // iterate over propertiestext and draw text and resolve second-run facets
	}

	private static List<String> parseFacets(List<? extends Facet> facets, List<String> properties, PropertiesParserState state) {
		Map<Facet, List<String>> facetUsageMap = new HashMap<Facet, List<String>>();
		for (Facet f : facets) { // at parsing start every facet has an empty usage list
			facetUsageMap.put(f, new ArrayList<String>());
		}

		List<String> unusedProperties = new ArrayList<String>(properties);
		for (Iterator<String> iter = unusedProperties.iterator(); iter.hasNext();) {
			String line = iter.next();
			for (Facet f : facets) {
				if (f.checkStart(line, state)) {
					f.handleLine(line, state);
					facetUsageMap.get(f).add(line);
					iter.remove();
					break; // once a facet has consumed a line, no other facet can
				}
			}
		}
		for (Facet f : facets) {
			f.parsingFinished(state, facetUsageMap.get(f));
		}
		return unusedProperties;
	}
}
