package com.baselet.elementnew;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baselet.control.TextSplitter;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.Facet.Priority;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.elementnew.facet.common.ElementStyleFacet.ElementStyleEnum;
import com.baselet.elementnew.settings.Settings;

public class PropertiesParser {

	public static void drawPropertiesText(NewGridElement element, PropertiesParserState state) {
		List<String> propertiesText = element.getPanelAttributesAsList();
		autoresizeAnalysis(element, state.getSettings(), propertiesText); // at first handle autoresize (which possibly changes elementsize)
		state.resetValues(element.getRealSize()); // now that the element size is known, reset the state with it
		List<String> propTextWithoutGobalFacets = parseGlobalFacets(propertiesText, state.getSettings().getGlobalFacets(), element.getDrawer(), state); // must be before element.drawCommonContent (because bg=... and other settings are set here)
		element.resetMetaDrawerAndDrawCommonContent(); // draw common content like border around classes
		drawPropertiesWithoutGlobalFacets(propTextWithoutGobalFacets, state, element.getDrawer()); // iterate over propertiestext and draw text and resolve localfacets
	}

	private static void autoresizeAnalysis(NewGridElement element, Settings settings, List<String> propertiesText) {
		DrawHandler pseudoDrawer = element.getDrawer().getPseudoDrawHandler();
		PropertiesParserState tmpstate = new PropertiesParserState(settings, element.getRealSize()); // we use a tmpstate to parse global facets to see if autoresize is enabled
		List<String> tmpPropTextWithoutGlobalFacets = parseGlobalFacets(propertiesText, tmpstate.getSettings().getGlobalFacets(), pseudoDrawer, tmpstate);

		if (tmpstate.getElementStyle() == ElementStyleEnum.AUTORESIZE) { // only in case of autoresize element, we must proceed to calculate elementsize and resize it
			element.drawCommonContent(pseudoDrawer, tmpstate);
			drawPropertiesWithoutGlobalFacets(tmpPropTextWithoutGlobalFacets, tmpstate, pseudoDrawer);
			double textHeight = tmpstate.getyPos() - pseudoDrawer.textHeightMax(); // subtract last ypos step to avoid making element too high (because the print-text pos is always on the bottom)
			double width = tmpstate.getCalculatedElementWidth();
			element.handleAutoresize(new DimensionDouble(width, textHeight), tmpstate.gethAlign());
		}
	}

	private static List<String> parseGlobalFacets(List<String> propertiesText, Map<Priority, List<GlobalFacet>> globalFacetMap, DrawHandler drawer, PropertiesParserState state) {
		List<String> propertiesCopy = new ArrayList<String>(propertiesText);
		for (Priority priority : Priority.values()) {
			List<GlobalFacet> facets = globalFacetMap.get(priority);
			if (facets == null) {
				continue; // skip priorities without facets
			}
			for (Iterator<String> iter = propertiesCopy.iterator(); iter.hasNext();) {
				String line = iter.next();
				boolean drawText = parseFacets(facets, line, drawer, state);
				if (!drawText || line.startsWith("//")) {
					iter.remove();
				}
			}
		}
		return propertiesCopy;
	}

	private static void drawPropertiesWithoutGlobalFacets(List<String> propertiesTextWithoutGobalFacets, PropertiesParserState state, DrawHandler drawer) {
		double startPointFromVAlign = calcStartPointFromVAlign(propertiesTextWithoutGobalFacets, state, drawer);
		state.addToYPos(calcTopDisplacementToFitLine(propertiesTextWithoutGobalFacets, startPointFromVAlign, state, drawer));
		handleWordWrapAndIterate(propertiesTextWithoutGobalFacets, state.getSettings().getLocalFacets(), state, drawer);
	}

	private static double calcTopDisplacementToFitLine(List<String> propertiesText, double startPoint, PropertiesParserState state, DrawHandler drawer) {
		double displacement = startPoint;
		boolean wordwrap = state.getElementStyle() == ElementStyleEnum.WORDWRAP;
		if (!wordwrap && !propertiesText.isEmpty()) { // in case of wordwrap or no text, there is no top displacement
			int BUFFER = 2; // a small buffer between text and outer border
			double textHeight = drawer.textHeightMax();
			String firstLine = propertiesText.iterator().next();
			double availableWidthSpace = state.getXLimitsForArea(displacement, textHeight, true).getSpace() - BUFFER;
			double accumulator = displacement;
			int maxLoops = 1000;
			while (accumulator < state.getGridElementSize().height && !TextSplitter.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
				if (maxLoops-- < 0) {
					throw new RuntimeException("Endless loop during calculation of top displacement");
				}
				accumulator += textHeight / 2;
				double previousWidthSpace = availableWidthSpace;
				availableWidthSpace = state.getXLimitsForArea(accumulator, textHeight, true).getSpace() - BUFFER;
				// only set displacement if the last iteration resulted in a space gain (eg: for UseCase until the middle, for Class: stays on top because on a rectangle there is never a width-space gain)
				if (availableWidthSpace > previousWidthSpace) {
					displacement = accumulator;
				}
			}
		}
		return displacement;
	}

	private static double calcStartPointFromVAlign(List<String> propertiesText, PropertiesParserState p, DrawHandler drawer) {
		double returnVal = drawer.textHeightMax(); // print method is located at the bottom of the text therefore add text height (important for UseCase etc where text must not reach out of the border)
		if (p.getvAlign() == AlignVertical.TOP) {
			returnVal += drawer.getDistanceBorderToText() + p.getTopBuffer();
		}
		else if (p.getvAlign() == AlignVertical.CENTER) {
			returnVal += (p.getGridElementSize().height - getTextBlockHeight(propertiesText, p, drawer)) / 2 + p.getTopBuffer() / 2;
		}
		else /* if (state.getvAlign() == AlignVertical.BOTTOM) */{
			returnVal += p.getGridElementSize().height - getTextBlockHeight(propertiesText, p, drawer) - drawer.textHeightMax() / 4; // 1/4 of textheight is a good value for large fontsizes and "deep" characters like "y"
		}
		return returnVal;
	}

	private static double getTextBlockHeight(List<String> propertiesText, PropertiesParserState originalstate, DrawHandler drawer) {
		PropertiesParserState tmpstate = new PropertiesParserState(originalstate.getSettings(), originalstate.getGridElementSize());
		tmpstate.setElementStyle(originalstate.getElementStyle()); // elementstyle is important for calculation (because of wordwrap)
		handleWordWrapAndIterate(propertiesText, tmpstate.getSettings().getLocalFacets(), tmpstate, drawer.getPseudoDrawHandler());
		return tmpstate.getyPos();
	}

	private static void handleWordWrapAndIterate(List<String> propertiesText, List<Facet> facets, PropertiesParserState state, DrawHandler drawer) {
		boolean wordwrap = state.getElementStyle() == ElementStyleEnum.WORDWRAP;
		for (String line : propertiesText) {
			if (wordwrap && !line.trim().isEmpty()) { // empty lines are skipped (otherwise they would get lost)
				String wrappedLine;
				while (state.getyPos() < state.getGridElementSize().height && !line.trim().isEmpty()) {
					double spaceForText = state.getXLimitsForArea(state.getyPos(), drawer.textHeightMax(), false).getSpace() - drawer.getDistanceBorderToText() * 2;
					wrappedLine = TextSplitter.splitString(line, spaceForText, drawer);
					handleLine(facets, wrappedLine, state, drawer);
					line = line.substring(wrappedLine.length()).trim();
				}
			}
			else {
				handleLine(facets, line, state, drawer);
			}
		}
	}

	private static void handleLine(List<Facet> facets, String line, PropertiesParserState state, DrawHandler drawer) {
		boolean drawText = parseFacets(facets, line, drawer, state);
		if (drawText) {
			XValues xLimitsForText = state.getXLimitsForArea(state.getyPos(), drawer.textHeightMax(), false);
			Double spaceNotUsedForText = state.getGridElementSize().width - xLimitsForText.getSpace();
			if (!spaceNotUsedForText.equals(Double.NaN)) { // NaN is possible if xlimits calculation contains e.g. a division by zero
				state.updateCalculatedElementWidth(spaceNotUsedForText + drawer.textWidth(line));
			}
			if (state.getSettings().printText()) {
				drawer.print(line, calcHorizontalTextBoundaries(xLimitsForText, state, drawer), state.getyPos(), state.gethAlign());
			}
			state.addToYPos(drawer.textHeightMaxWithSpace());
		}
	}

	private static boolean parseFacets(List<? extends Facet> facets, String line, DrawHandler drawer, PropertiesParserState state) {
		boolean drawText = true;
		for (Facet f : facets) {
			if (f.checkStart(line, state)) {
				f.handleLine(line, drawer, state);
				if (f.replacesText(line)) {
					drawText = false;
				}
			}
		}
		return drawText;
	}

	private static double calcHorizontalTextBoundaries(XValues xLimitsForText, PropertiesParserState state, DrawHandler drawer) {
		double x;
		if (state.gethAlign() == AlignHorizontal.LEFT) {
			x = xLimitsForText.getLeft() + drawer.getDistanceBorderToText();
		}
		else if (state.gethAlign() == AlignHorizontal.CENTER) {
			x = xLimitsForText.getSpace() / 2.0 + xLimitsForText.getLeft();
		}
		else /* if (state.gethAlign() == AlignHorizontal.RIGHT) */{
			x = xLimitsForText.getRight() - drawer.getDistanceBorderToText();
		}
		return x;
	}

}
