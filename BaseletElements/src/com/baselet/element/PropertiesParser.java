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
 * 1. check if the ElementStyle property is set to AUTORESIZE.
 * 1.1. If yes a complete dummy-parse is required to calculate the expected element size. The new size of the element gets applied and the PropertiesParserState gets updated
 * 2. The First-Run Facets get parsed and applied in order of their PriorityEnum value (this largely reduces the size of the remaining properties for the Second Run)
 * 3. The common content of the element is drawn (e.g. the border) (therefore this must happen AFTER parsing the First-Run Facets because they can change the bg-color and so on)
 * 4. The remaining properties are parsed and the Second-Run Facets are applied.
 */
public class PropertiesParser {

	public static void parsePropertiesAndHandleFacets(NewGridElement element, PropertiesParserState state) {
		List<String> propertiesText = element.getPanelAttributesAsList();
		autoresizeAnalysis(element, state, propertiesText); // at first handle autoresize (which possibly changes elementsize)
		state.resetValues(element.getRealSize()); // now that the element size is known, reset the state with it
		List<String> propertiesAfterFirstRun = parseFacets(state.getSettings().getFacetsForFirstRun(), propertiesText, state); // must be before element.drawCommonContent (because bg=... and other settings are set here)
		state.setTextBlockHeight(calcTextBlockHeight(propertiesAfterFirstRun, state)); // calc the total size of the textblock (necessary for text printing with valign!=top)
		element.resetMetaDrawerAndDrawCommonContent(); // draw common content like border around classes
		parseFacets(state.getSettings().getFacetsForSecondRun(), propertiesAfterFirstRun, state); // iterate over propertiestext and draw text and resolve second-run facets
	}

	private static void autoresizeAnalysis(NewGridElement element, PropertiesParserState state, List<String> propertiesText) {
		PropertiesParserState tmpstate = state.dummyCopy(element.getRealSize()); // we use a tmpstate to parse first-run facets to see if autoresize is enabled
		List<String> tmpPropertiesAfterFirstRun = parseFacets(tmpstate.getSettings().getFacetsForFirstRun(), propertiesText, tmpstate);

		if (tmpstate.getElementStyle() == ElementStyle.AUTORESIZE) { // only in case of autoresize element, we must proceed to calculate elementsize and resize it
			element.drawCommonContent(tmpstate);
			parseFacets(tmpstate.getSettings().getFacetsForSecondRun(), tmpPropertiesAfterFirstRun, tmpstate);
			double textHeight = tmpstate.getTextPrintPosition() - tmpstate.getDrawer().textHeightMax(); // subtract last ypos step to avoid making element too high (because the print-text pos is always on the bottom)
			double width = tmpstate.getCalculatedElementWidth();
			element.handleAutoresize(new DimensionDouble(width, textHeight), tmpstate.getAlignment().getHorizontal());
		}
	}

	private static double calcTextBlockHeight(List<String> propertiesText, PropertiesParserState state) {
		PropertiesParserState tmpstate = state.dummyCopy(state.getGridElementSize()); // a dummy state copy is used for calculation to make sure the textBlockHeight calculation doesn't change the real state
		parseFacets(tmpstate.getSettings().getFacetsForSecondRun(), propertiesText, tmpstate);
		return tmpstate.getTextPrintPosition() - tmpstate.getBuffer().getTop();
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
