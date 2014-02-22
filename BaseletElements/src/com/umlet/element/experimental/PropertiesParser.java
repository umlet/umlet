package com.umlet.element.experimental;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baselet.control.TextSplitter;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.Facet.Priority;
import com.umlet.element.experimental.facets.GlobalFacet;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;

public class PropertiesParser {

	public static void drawPropertiesText(NewGridElement element, PropertiesConfig propCfg) {
		Settings settings = propCfg.getSettings();
		BaseDrawHandler drawer = element.getDrawer();
		List<String> propertiesText = initSettingsFromText(element, propCfg); // must be before element.drawCommonContent (because bg=... and other settings are set here)
		element.resetMetaDrawerAndDrawCommonContent();
		propCfg.addToYPos(calcTopDisplacementToFitLine(propertiesText, calcStartPointFromVAlign(propertiesText, propCfg.getvAlign(), propCfg.getGridElementSize(), drawer, settings), propCfg, drawer));
		handleWordWrapAndIterate(propertiesText, settings.getLocalFacets(), propCfg, drawer);
		propCfg.executeDelayedDrawings();
	}

	private static List<String> initSettingsFromText(NewGridElement element, PropertiesConfig propCfg) {
		element.onParsingStart();
		List<String> propertiesText = new ArrayList<String>(element.getPanelAttributesAsList());
		propCfg.resetValues();

		parseGlobalFacets(propertiesText, propCfg.getSettings().getGlobalFacets(), element.getDrawer(), propCfg);

		if (propCfg.getElementStyle() == ElementStyleEnum.AUTORESIZE) {
			DimensionDouble expectedElementSizeOnDefaultZoom = getExpectedElementDimensionsOnDefaultZoom(propertiesText, element.getDrawer().getPseudoDrawHandler(), propCfg.getSettings(), element.getRealSize());
			element.handleAutoresize(expectedElementSizeOnDefaultZoom, propCfg.gethAlign());
		}
		propCfg.setGridElementSize(element.getRealSize());
		return propertiesText;
	}

	private static DimensionDouble getExpectedElementDimensionsOnDefaultZoom(List<String> propertiesText, BaseDrawHandler pseudoDrawer, Settings settings, Dimension elementRealSize) {
		// add all ypos changes to simulate the real ypos for xlimit calculation etc.
		PropertiesConfig tmpPropCfg = new PropertiesConfig(settings, elementRealSize);
		tmpPropCfg.addToYPos(calcTopDisplacementToFitLine(propertiesText, calcStartPointFromVAlign(propertiesText, tmpPropCfg.getvAlign(), tmpPropCfg.getGridElementSize(), pseudoDrawer, settings), tmpPropCfg, pseudoDrawer));
		handleWordWrapAndIterate(propertiesText, settings.getLocalFacets(), tmpPropCfg, pseudoDrawer);
	
		double textHeight = tmpPropCfg.getyPos()-pseudoDrawer.textHeight(); // subtract last ypos step to avoid making element too high (because the print-text pos is always on the bottom)
		double width = tmpPropCfg.getCalculatedElementWidth();
		return new DimensionDouble(width, textHeight);
	}

	private static void parseGlobalFacets(List<String> propertiesText, Map<Priority, List<GlobalFacet>> globalFacetMap, BaseDrawHandler drawer, PropertiesConfig propCfg) {
		for (Priority priority : Priority.values()) {
			List<GlobalFacet> facets = globalFacetMap.get(priority);
			if (facets == null) continue; // skip priorities without facets
			for (Iterator<String> iter = propertiesText.iterator(); iter.hasNext();) {
				String line = iter.next();
				boolean drawText = parseFacets(facets, line, drawer, propCfg);
				if (!drawText || line.startsWith("//")) iter.remove();
			}
		}
	}

	private static boolean parseFacets(List<? extends Facet> facets, String line, BaseDrawHandler drawer, PropertiesConfig propCfg) {
		boolean drawText = true;
		for (Facet f : facets) {
			if (f.checkStart(line)) {
				f.handleLine(line, drawer, propCfg);
				if (f.replacesText(line)) drawText = false;
			}
		}
		return drawText;
	}

	private static double calcTopDisplacementToFitLine(List<String> propertiesText, double startPoint, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		int BUFFER = 2; // a small buffer between text and outer border
		double displacement = startPoint;
		double textHeight = drawer.textHeight();
		boolean wordwrap = propCfg.getElementStyle() == ElementStyleEnum.WORDWRAP;
		if (!wordwrap && !propertiesText.isEmpty()) { // in case of wordwrap or no text, there is no top displacement
			String firstLine = propertiesText.iterator().next();
			double availableWidthSpace = propCfg.getXLimitsForArea(displacement, textHeight).getSpace() - BUFFER;
			double accumulator = displacement;
			int maxLoops = 1000;
			while(accumulator < propCfg.getGridElementSize().height && !TextSplitter.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
				if (maxLoops-- < 0) {
					throw new RuntimeException("Endless loop during calculation of top displacement");
				}
				accumulator += textHeight / 2;
				double previousWidthSpace = availableWidthSpace;
				availableWidthSpace = propCfg.getXLimitsForArea(accumulator, textHeight).getSpace() - BUFFER;
				// only set displacement if the last iteration resulted in a space gain (eg: for UseCase until the middle, for Class: stays on top because on a rectangle there is never a width-space gain)
				if (availableWidthSpace > previousWidthSpace) displacement = accumulator;
			}
		}
		return displacement;
	}

	private static void handleWordWrapAndIterate(List<String> propertiesText, List<Facet> facets, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		boolean wordwrap = propCfg.getElementStyle() == ElementStyleEnum.WORDWRAP;
		for (String line : propertiesText) {
			if (wordwrap && !line.trim().isEmpty()) { // empty lines are skipped (otherwise they would get lost)
				String wrappedLine;
				while (propCfg.getyPos() < propCfg.getGridElementSize().height && !line.trim().isEmpty()) {
					double spaceForText = propCfg.getXLimitsForArea(propCfg.getyPos(), drawer.textHeight()).getSpace() - drawer.getDistanceHorizontalBorderToText() * 2;
					wrappedLine = TextSplitter.splitString(line, spaceForText, drawer);
					handleLine(facets, wrappedLine, propCfg, drawer);
					line = line.substring(wrappedLine.length()).trim();
				}
			}
			else handleLine(facets, line, propCfg, drawer);
		}
	}

	private static void handleLine(List<Facet> facets, String line, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		boolean drawText = parseFacets(facets, line, drawer, propCfg);
		if (drawText) {
			XValues xLimitsForText = propCfg.getXLimitsForArea(propCfg.getyPos(), drawer.textHeight());
			Double spaceNotUsedForText = propCfg.getGridElementSize().width - xLimitsForText.getSpace();
			if (!spaceNotUsedForText.equals(Double.NaN)) { // NaN is possible if xlimits calculation contains e.g. a division by zero
				propCfg.updateCalculatedElementWidth(spaceNotUsedForText + drawer.textWidth(line));
			}
			drawer.print(line, calcHorizontalTextBoundaries(xLimitsForText, propCfg, drawer), propCfg.getyPos(), propCfg.gethAlign());
			propCfg.addToYPos(drawer.textHeightWithSpace());
		}
	}

	private static double calcHorizontalTextBoundaries(XValues xLimitsForText, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		double x;
		if (propCfg.gethAlign() == AlignHorizontal.LEFT) {
			x = xLimitsForText.getLeft() + drawer.getDistanceHorizontalBorderToText();
		} else if (propCfg.gethAlign() == AlignHorizontal.CENTER) {
			x = propCfg.getGridElementSize().width / 2.0;
		} else /*if (propCfg.gethAlign() == AlignHorizontal.RIGHT)*/ {
			x = xLimitsForText.getRight() - drawer.getDistanceHorizontalBorderToText();
		}
		return x;
	}

	private static double calcStartPointFromVAlign(List<String> propertiesText, AlignVertical vAlign, Dimension gridElementSize, BaseDrawHandler drawer, Settings settings) {
		double returnVal = drawer.textHeight(); // print method is located at the bottom of the text therefore add text height (important for UseCase etc where text must not reach out of the border)
		if (vAlign == AlignVertical.TOP) {
			returnVal += drawer.textHeight()/2;
		}
		else if (vAlign == AlignVertical.CENTER) {
			returnVal += (gridElementSize.height - getTextBlockHeight(propertiesText, gridElementSize, drawer, settings))/2;
		}
		else /*if (propCfg.getvAlign() == AlignVertical.BOTTOM)*/ {
			returnVal += gridElementSize.height - getTextBlockHeight(propertiesText, gridElementSize, drawer, settings) - drawer.textHeight()/2;
		}
		return returnVal;
	}

	private static double getTextBlockHeight(List<String> propertiesText, Dimension gridElementSize, BaseDrawHandler drawer, Settings settings) {
		PropertiesConfig tmpPropCfg = new PropertiesConfig(settings, gridElementSize);
		handleWordWrapAndIterate(propertiesText, settings.getLocalFacets(), tmpPropCfg, drawer.getPseudoDrawHandler());
		return tmpPropCfg.getyPos();
	}

}
