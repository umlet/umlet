package com.umlet.element.experimental;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.baselet.control.Constants.ElementStyle;
import com.baselet.control.DimensionFloat;
import com.baselet.control.TextManipulator;
import com.baselet.control.Utils;
import com.baselet.diagram.command.Resize;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.helper.XPoints;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.text.Facet;
import com.umlet.element.experimental.settings.text.GlobalSettings;

public class Properties {

	protected String panelAttributes = "";
	protected String panelAttributesAdditional = "";

	private BaseDrawHandler drawer;

	protected GlobalSettings settings;

	private List<String> propertiesTextToDraw;

	private PropertiesConfig propCfg;

	private Settings elementSettings;

	public Properties(String panelAttributes, String panelAttributesAdditional, BaseDrawHandler drawer) {
		this.panelAttributes = panelAttributes;
		this.panelAttributesAdditional = panelAttributesAdditional;
		this.drawer = drawer;
	}

	public String getPanelAttributes() {
		return panelAttributes;
	}

	public String getPanelAttributesAdditional() {
		return panelAttributesAdditional;
	}

	public void setPanelAttributes(String panelAttributes) {
		this.panelAttributes = panelAttributes;
	}

	public void setPanelAttributesAdditional(String panelAttributesAdditional) {
		this.panelAttributesAdditional = panelAttributesAdditional;
	}

	private Vector<String> getPropertiesText() {
		return decomposePropertiesText(this.getPanelAttributes(), Constants.NEWLINE, false, true);
	}

	public Vector<String> getPropertiesTextFiltered() {
		return decomposePropertiesText(this.getPanelAttributes(), Constants.NEWLINE, true, false);
	}

	private Vector<String> decomposePropertiesText(String fullString, String delimiter, boolean filterComments, boolean filterNewLines) {
		Vector<String> returnVector = new Vector<String>();
		String compatibleFullString = fullString.replaceAll("\r\n", delimiter); // compatibility to windows \r\n

		for (String line : compatibleFullString.split("\\" + delimiter)) {
			if (filterComments && (line.matches(GlobalSettings.getFilterRegex()))) continue;
			else if (filterNewLines && line.isEmpty()) continue;
			else returnVector.add(line);
		}

		return returnVector;
	}

	private void applyProperties() {
		Color fgColor = Utils.getColor(getSetting(SettingKey.ForegroundColor));
		if (fgColor == null) { // if fg is not set or invalid
			fgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		}
		drawer.setForegroundColor(fgColor);

		float bgAlpha = Constants.ALPHA_MIDDLE_TRANSPARENCY;
		Color bgColor = Utils.getColor(getSetting(SettingKey.BackgroundColor));
		if (bgColor == null) { // if bg is not set or invalid, the background is white at full transparency
			bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
			bgAlpha = Constants.ALPHA_FULL_TRANSPARENCY;
		}
		drawer.setBackground(bgColor, bgAlpha);

		drawer.setLineType(getSetting(SettingKey.LineType));

		Float fontSize = getSettingFloat(SettingKey.FontSize);
		if (fontSize != null) drawer.setFontSize(fontSize);
	}

	public void initSettingsFromText(NewGridElement element) {
		settings = new GlobalSettings(getPropertiesText());
		applyProperties();

		propertiesTextToDraw = getPropertiesTextFiltered();
		this.elementSettings = element.getSettings();
		handleAutoresize(element);
		this.propCfg = new PropertiesConfig(this, element.getSettings(), element.getRealSize());
	}

	private void handleAutoresize(NewGridElement element) {
		if (ElementStyle.AUTORESIZE.toString().equalsIgnoreCase(getSetting(SettingKey.ElementStyle))) {
			DimensionFloat dim = getExpectedElementDimensions(element);
			int BUFFER = 10; // buffer to make sure the text is inside the border
			float width = Math.max(20, dim.getWidth() + BUFFER);
			float height = Math.max(20, dim.getHeight() + BUFFER);
			// use resize command to move sticked relations correctly with the element
			int diffw = (int) (width-element.getRealSize().width);
			int diffh = (int) (height-element.getRealSize().height);
			float zoomFactor = element.getHandler().getZoomFactor();
			new Resize(element, 0, 0, (int) (diffw * zoomFactor), (int) (diffh * zoomFactor)).execute(element.getHandler());
		}
	}

	public String getSetting(SettingKey key) {
		return settings.getSetting(key);
	}

	public Float getSettingFloat(SettingKey key) {
		return settings.getSettingFloat(key);
	}

	public void updateSetting(SettingKey key, String newValue) {
		this.setPanelAttributes(settings.updateSetting(key, newValue, this.getPanelAttributes()));
	}

	public void drawPropertiesText() {
		propCfg.addToYPos(calcTopDisplacementToFitLine(calcStartPointFromVAlign(propCfg), propCfg));
		handleWordWrapAndIterate(elementSettings, propCfg, drawer);
	}

	private float calcTopDisplacementToFitLine(float startPoint, PropertiesConfig propCfg) {
		int BUFFER = 2; // a small buffer between text and outer border
		float displacement = startPoint;
		float textHeight = drawer.textHeight();
		boolean wordwrap = ElementStyle.WORDWRAP.toString().equalsIgnoreCase(getSetting(SettingKey.ElementStyle));
		if (!wordwrap && !propertiesTextToDraw.isEmpty()) { // in case of wordwrap or no text, there is no top displacement
			String firstLine = propertiesTextToDraw.iterator().next();
			float availableWidthSpace = propCfg.getXLimitsForArea(displacement, textHeight).getSpace() - BUFFER;
			float accumulator = displacement;
			while(accumulator < propCfg.getGridElementSize().height && !TextManipulator.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
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
		boolean wordwrap = ElementStyle.WORDWRAP.toString().equalsIgnoreCase(getSetting(SettingKey.ElementStyle));
		for (String line : propertiesTextToDraw) {
			if (wordwrap) {
				String wrappedLine;
				while (propCfg.getyPos() < propCfg.getGridElementSize().height && !line.trim().isEmpty()) {
					wrappedLine = TextManipulator.splitString(line, propCfg.getXLimitsForArea(propCfg.getyPos(), drawer.textHeight()).getSpace(), drawer);
					handleLine(elementSettings, wrappedLine, propCfg, drawer);
					line = line.trim().substring(wrappedLine.length());
				}
			}
			else handleLine(elementSettings, line, propCfg, drawer);
		}
	}

	private void handleLine(Settings elementSettings, String line, PropertiesConfig propCfg, BaseDrawHandler drawer) {
		boolean drawText = true;
		for (Facet facet : elementSettings.getFacets()) {
			if (facet.checkStart(line)) {
				facet.handleLine(line, drawer, propCfg);
				if (facet.replacesText(line)) {
					drawText = false;
				}
			}
		}
		if (drawText) {
			XPoints xLimitsForText = propCfg.getXLimitsForArea(propCfg.getyPos(), drawer.textHeight());
			Float spaceNotUsedForText = propCfg.getGridElementSize().width - xLimitsForText.getSpace();
			if (!spaceNotUsedForText.equals(Float.NaN)) { // NaN is possible if xlimits calculation contains e.g. a division by zero
				propCfg.calcMaxTextWidth(spaceNotUsedForText + drawer.textWidth(line));
			}
			drawer.print(line, calcHorizontalTextBoundaries(xLimitsForText, propCfg), propCfg.getyPos(), propCfg.gethAlign());
			propCfg.addToYPos(drawer.textHeightWithSpace());
		}
	}

	private float calcHorizontalTextBoundaries(XPoints xLimitsForText, PropertiesConfig propCfg) {
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
		float returnVal = drawer.textHeight(); // print method is located at the bottom of the text therefore add text height
		if (propCfg.getvAlign() == AlignVertical.TOP) {
			returnVal += drawer.textHeight()/2;
		}
		else if (propCfg.getvAlign() == AlignVertical.CENTER) {
			returnVal += Math.max((propCfg.getGridElementSize().height - getTextBlockHeight())/2, drawer.textHeightWithSpace());
		}
		else /*if (propCfg.getvAlign() == AlignVertical.BOTTOM)*/ {
			returnVal += Math.max(propCfg.getGridElementSize().height - getTextBlockHeight(), drawer.textHeightWithSpace());
		}
		return returnVal;
	}

	public float getTextBlockHeight() {
		PropertiesConfig tmpPropCfg = new PropertiesConfig(this, elementSettings, propCfg.getGridElementSize());
		handleWordWrapAndIterate(elementSettings, tmpPropCfg, drawer.getPseudoDrawHandler());
		return tmpPropCfg.getyPos();
	}

	private DimensionFloat getExpectedElementDimensions(NewGridElement element) {
		// add all ypos changes to simulate the real ypos for xlimit calculation etc.
		PropertiesConfig tmpPropCfg = new PropertiesConfig(this, element.getSettings(), element.getRealSize());
		tmpPropCfg.addToYPos(calcTopDisplacementToFitLine(calcStartPointFromVAlign(tmpPropCfg), tmpPropCfg));
		handleWordWrapAndIterate(elementSettings, tmpPropCfg, drawer.getPseudoDrawHandler());

		float textHeight = tmpPropCfg.getyPos()-drawer.textHeight(); // subtract last ypos step (because the print-text pos is always on the bottom)
		return new DimensionFloat(tmpPropCfg.getMaxTextWidth(), textHeight);
	}

}
