package com.umlet.element.experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.TextSplitter;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.DimensionFloat;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.ElementStyleEnum;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.GlobalSetting;
import com.umlet.element.experimental.settings.facets.Facet;

public class Properties {

	protected String panelAttributes = "";

	private BaseDrawHandler drawer;

	private List<String> propertiesTextToDraw;

	private PropertiesConfig propCfg;

	private Settings elementSettings;

	public Properties(String panelAttributes, BaseDrawHandler drawer) {
		this.panelAttributes = panelAttributes;
		this.drawer = drawer;
	}

	public String getPanelAttributes() {
		return panelAttributes;
	}

	private List<String> getPanelAttributesAsList() {
		return Arrays.asList(this.getPanelAttributes().split("\n"));
	}

	public void setPanelAttributes(String panelAttributes) {
		this.panelAttributes = panelAttributes;
	}

	public void initSettingsFromText(NewGridElement element) {
		propertiesTextToDraw = new ArrayList<String>();
		this.elementSettings = element.getSettings();
		this.propCfg = new PropertiesConfig(element.getSettings());

		// Handle preparse global facets which set main attributes like elementstyle, ...
		parseGlobalFacets(elementSettings.getPreparseGlobalFacets());
		
		// Determine element size which is necessary for some global facets (like {active})
		if (getElementStyle() == ElementStyleEnum.AUTORESIZE) {
			element.handleAutoresize(getExpectedElementDimensionsOnDefaultZoom(element));
		}
		this.propCfg.setGridElementSize(element.getRealSize());

		propertiesTextToDraw.clear(); // clear text to draw which was only necessary to determine text-dimensions for autoresize

		// Handle other global facets
		parseGlobalFacets(elementSettings.getGlobalFacets());
	}

	private void parseGlobalFacets(List<? extends Facet> facets) {
		for (String line : getPanelAttributesAsList()) {
			boolean drawText = true;
			for (Facet gf : facets) {
				if (gf.checkStart(line)) {
					gf.handleLine(line, drawer, propCfg);
					if (gf.replacesText(line)) drawText = false;
				}
			}
			if (drawText && !line.startsWith("//")) propertiesTextToDraw.add(line);
		}
	}

	public ElementStyleEnum getElementStyle() {
		return propCfg.getElementStyle();
	}

	public Integer getLayer() {
		if (propCfg == null) return null;
		return propCfg.getLayer();
	}

	public void updateSetting(GlobalSetting key, String newValue) {
		String newState = "";
		for (String line : getPanelAttributes().split("\n")) {
			if (!line.startsWith(key.toString())) newState += line + "\n";
		}
		newState = newState.substring(0, newState.length()-1); //remove last linebreak
		if (newValue != null) newState += "\n" + key.toString() + GlobalSetting.SEPARATOR + newValue; // null will not be added as a value
		this.setPanelAttributes(newState);
	}

	public String getSetting(GlobalSetting key) {
		for (String line : getPanelAttributesAsList()) {
			if (line.startsWith(key + GlobalSetting.SEPARATOR)) {
				String[] split = line.split(GlobalSetting.SEPARATOR, 2);
				if (split.length > 1) return split[1];
			}
		}
		return null;
	}

	public void drawPropertiesText() {
		propCfg.addToYPos(calcTopDisplacementToFitLine(calcStartPointFromVAlign(propCfg), propCfg));
		handleWordWrapAndIterate(elementSettings, propCfg, drawer);
	}

	private float calcTopDisplacementToFitLine(float startPoint, PropertiesConfig propCfg) {
		int BUFFER = 2; // a small buffer between text and outer border
		float displacement = startPoint;
		float textHeight = drawer.textHeight();
		boolean wordwrap = getElementStyle() == ElementStyleEnum.WORDWRAP;
		if (!wordwrap && !propertiesTextToDraw.isEmpty()) { // in case of wordwrap or no text, there is no top displacement
			String firstLine = propertiesTextToDraw.iterator().next();
			float availableWidthSpace = propCfg.getXLimitsForArea(displacement, textHeight).getSpace() - BUFFER;
			float accumulator = displacement;
			while(accumulator < propCfg.getGridElementSize().height && !TextSplitter.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
				accumulator += textHeight / 2;
				float previousWidthSpace = availableWidthSpace;
				availableWidthSpace = propCfg.getXLimitsForArea(accumulator, textHeight).getSpace() - BUFFER;
				// only set displacement if the last iteration resulted in a space gain (eg: for UseCase until the middle, for Class: stays on top because on a rectangle there is never a width-space gain)
				if (availableWidthSpace > previousWidthSpace) displacement = accumulator;
			}
		}
		return displacement;
	}

	private void handleWordWrapAndIterate(Settings elementSettings, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		boolean wordwrap = getElementStyle() == ElementStyleEnum.WORDWRAP;
		for (String line : propertiesTextToDraw) {
			if (wordwrap) {
				String wrappedLine;
				while (propCfg.getyPos() < propCfg.getGridElementSize().height && !line.trim().isEmpty()) {
					Float spaceForText = propCfg.getXLimitsForArea(propCfg.getyPos(), drawer.textHeight()).getSpace() - drawer.getDistanceBetweenTexts() * 2;
					wrappedLine = TextSplitter.splitString(line, spaceForText, drawer);
					handleLine(elementSettings, wrappedLine, propCfg, drawer);
					line = line.substring(wrappedLine.length()).trim();
				}
			}
			else handleLine(elementSettings, line, propCfg, drawer);
		}
	}

	private void handleLine(Settings elementSettings, String line, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		boolean drawText = true;
		for (Facet facet : elementSettings.getLocalFacets()) {
			if (facet.checkStart(line)) {
				facet.handleLine(line, drawer, propCfg);
				if (facet.replacesText(line)) {
					drawText = false;
				}
			}
		}
		if (drawText) {
			XValues xLimitsForText = propCfg.getXLimitsForArea(propCfg.getyPos(), drawer.textHeight());
			Float spaceNotUsedForText = propCfg.getGridElementSize().width - xLimitsForText.getSpace();
			if (!spaceNotUsedForText.equals(Float.NaN)) { // NaN is possible if xlimits calculation contains e.g. a division by zero
				propCfg.calcMaxTextWidth(spaceNotUsedForText + drawer.textWidth(line));
			}
			drawer.print(line, calcHorizontalTextBoundaries(xLimitsForText, propCfg), propCfg.getyPos(), propCfg.gethAlign());
			propCfg.addToYPos(drawer.textHeightWithSpace());
		}
	}

	private float calcHorizontalTextBoundaries(XValues xLimitsForText, PropertiesConfig propCfg) {
		float x;
		if (propCfg.gethAlign() == AlignHorizontal.LEFT) {
			x = xLimitsForText.getLeft() + drawer.getDistanceBetweenTexts();
		} else if (propCfg.gethAlign() == AlignHorizontal.CENTER) {
			x = propCfg.getGridElementSize().width / 2;
		} else /*if (propCfg.gethAlign() == AlignHorizontal.RIGHT)*/ {
			x = xLimitsForText.getRight() - drawer.getDistanceBetweenTexts();
		}
		return x;
	}

	private float calcStartPointFromVAlign(PropertiesConfig propCfg) {
		float returnVal = drawer.textHeight(); // print method is located at the bottom of the text therefore add text height (important for UseCase etc where text must not reach out of the border)
		if (propCfg.getvAlign() == AlignVertical.TOP) {
			returnVal += drawer.textHeight()/2;
		}
		else if (propCfg.getvAlign() == AlignVertical.CENTER) {
			returnVal += (propCfg.getGridElementSize().height - getTextBlockHeight(propCfg))/2;
		}
		else /*if (propCfg.getvAlign() == AlignVertical.BOTTOM)*/ {
			returnVal += propCfg.getGridElementSize().height - getTextBlockHeight(propCfg) - drawer.textHeight()/2;
		}
		return returnVal;
	}

	public float getTextBlockHeight(PropertiesConfig propCfg) {
		PropertiesConfig tmpPropCfg = new PropertiesConfig(elementSettings, propCfg.getGridElementSize());
		handleWordWrapAndIterate(elementSettings, tmpPropCfg, drawer.getPseudoDrawHandler());
		return tmpPropCfg.getyPos();
	}

	private DimensionFloat getExpectedElementDimensionsOnDefaultZoom(NewGridElement element) {
		// add all ypos changes to simulate the real ypos for xlimit calculation etc.
		PropertiesConfig tmpPropCfg = new PropertiesConfig(element.getSettings(), element.getRealSize());
		tmpPropCfg.addToYPos(calcTopDisplacementToFitLine(calcStartPointFromVAlign(tmpPropCfg), tmpPropCfg));
		handleWordWrapAndIterate(elementSettings, tmpPropCfg, drawer.getPseudoDrawHandler());

		float textHeight = tmpPropCfg.getyPos()-drawer.textHeight(); // subtract last ypos step (because the print-text pos is always on the bottom)
		return new DimensionFloat(tmpPropCfg.getMaxTextWidth(), textHeight);
	}

}
