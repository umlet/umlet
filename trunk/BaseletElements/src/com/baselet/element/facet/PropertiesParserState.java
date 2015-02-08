package com.baselet.element.facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.control.basics.XValues;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.enums.ElementStyle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.sticking.polygon.SimpleStickingPolygonGenerator;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;

/**
 * The PropertiesParserState contains the mutable state of the parser which changes constantly while parsing
 * the properties printing related facets have explicit fields (for better usabilty), for any other Facet, the generic facetResponse map should be used for communication between facets and/or the gridelement
 *
 */
public class PropertiesParserState {

	private final Settings settings;

	private Alignment alignment;
	private double textPrintPosition; // the current y position for drawing text, separator-lines and other properties-text-related stuff
	private double calculatedElementWidth;
	private Buffer buffer;
	private Dimension gridElementSize;
	private ElementStyle elementStyle;
	private StickingPolygonGenerator stickingPolygonGenerator = SimpleStickingPolygonGenerator.INSTANCE;
	private double totalTextBlockHeight;
	private final Map<Class<? extends Facet>, Object> facetResponse = new HashMap<Class<? extends Facet>, Object>();
	private final List<Facet> usedFacets = new ArrayList<Facet>();

	public PropertiesParserState(Settings settings) {
		this.settings = settings;
	}

	public void resetValues(Dimension gridElementSize) {
		alignment = new Alignment(settings);
		textPrintPosition = 0;
		calculatedElementWidth = 0;
		buffer = new Buffer();
		this.gridElementSize = gridElementSize;
		elementStyle = settings.getElementStyle();
		stickingPolygonGenerator = SimpleStickingPolygonGenerator.INSTANCE;
		totalTextBlockHeight = 0;
		facetResponse.clear();
		usedFacets.clear();
	}

	public Alignment getAlignment() {
		return alignment;
	}

	/**
	 * returns the current text print position including the top buffer
	 */
	public double getTextPrintPosition() {
		return textPrintPosition + buffer.getTop();
	}

	/**
	 * use whenever the text print position should be increased (e.g. if -- draws a horizontal line, some vertical space should be added, or everytime TextPrintFacet prints a line)
	 */
	public void increaseTextPrintPosition(double inc) {
		textPrintPosition += inc;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public Dimension getGridElementSize() {
		return gridElementSize;
	}

	public XValues getXLimits(double linePos) {
		XValues xLimits = settings.getXValues(linePos, getGridElementSize().height, getGridElementSize().width);
		xLimits.addLeft(buffer.getLeft());
		xLimits.subRight(buffer.getRight());
		return xLimits;
	}

	public XValues getXLimitsForArea(double bottomYPos, double areaHeight, boolean nanPriority) {
		XValues xLimitsTop = getXLimits(bottomYPos - areaHeight);
		XValues xLimitsBottom = getXLimits(bottomYPos);
		XValues xLimits = xLimitsTop.intersect(xLimitsBottom, nanPriority);
		return xLimits;
	}

	public void updateCalculatedElementWidth(double width) {
		calculatedElementWidth = Math.max(calculatedElementWidth, width);
	}

	public double getCalculatedElementWidth() {
		return calculatedElementWidth;
	}

	public ElementStyle getElementStyle() {
		return elementStyle;
	}

	public void setElementStyle(ElementStyle elementStyle) {
		this.elementStyle = elementStyle;
	}

	public Settings getSettings() {
		return settings;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFacetResponse(Class<? extends Facet> facetClass, T defaultValue) {
		T mapValue = (T) facetResponse.get(facetClass);
		if (mapValue == null) {
			return defaultValue;
		}
		return mapValue;
	}

	public <T> T getOrInitFacetResponse(Class<? extends Facet> facetClass, T defaultValue) {
		T mapValue = getFacetResponse(facetClass, defaultValue);
		setFacetResponse(facetClass, mapValue);
		return mapValue;
	}

	public void setFacetResponse(Class<? extends Facet> facetClass, Object value) {
		facetResponse.put(facetClass, value);
	}

	public StickingPolygonGenerator getStickingPolygonGenerator() {
		return stickingPolygonGenerator;
	}

	public void setStickingPolygonGenerator(StickingPolygonGenerator stickingPolygonGenerator) {
		this.stickingPolygonGenerator = stickingPolygonGenerator;
	}

	public void setTextBlockHeight(double textBlockHeight) {
		totalTextBlockHeight = textBlockHeight;
	}

	public double getTotalTextBlockHeight() {
		return totalTextBlockHeight;
	}

	public void addUsedFacet(Facet facet) {
		if (!usedFacets.contains(facet)) {
			usedFacets.add(facet);
		}
	}

	public void informAndClearUsedFacets(DrawHandler drawer) {
		for (Facet f : usedFacets) {
			f.parsingFinished(drawer, this);
		}
		usedFacets.clear();
	}

	public PropertiesParserState dummyCopy(Dimension gridElementSize) {
		PropertiesParserState copy = new PropertiesParserState(settings);
		copy.resetValues(gridElementSize);
		copy.setElementStyle(getElementStyle()); // elementstyle is important for calculation (because of wordwrap)
		return copy;
	}
}
