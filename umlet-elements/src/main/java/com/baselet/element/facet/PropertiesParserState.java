package com.baselet.element.facet;

import java.util.HashMap;
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
	private final DrawHandler drawer;

	private Alignment alignment;
	private double textPrintPosition; // the current y position for drawing text, separator-lines and other properties-text-related stuff
	private double minimumWidth;
	private Buffer buffer;
	private Dimension gridElementSize;
	private ElementStyle elementStyle;
	private StickingPolygonGenerator stickingPolygonGenerator = SimpleStickingPolygonGenerator.INSTANCE;
	private double totalTextBlockHeight;
	private final Map<Class<? extends Facet>, Object> facetResponse = new HashMap<Class<? extends Facet>, Object>();

	public PropertiesParserState(Settings settings, DrawHandler drawer) {
		this.settings = settings;
		this.drawer = drawer;
	}

	public void resetValues(Dimension gridElementSize, double totalTextBlockHeight, boolean enableDrawing) {
		alignment = new Alignment(settings);
		textPrintPosition = 0;
		minimumWidth = 0;
		buffer = new Buffer();
		this.gridElementSize = gridElementSize;
		elementStyle = settings.getElementStyle();
		stickingPolygonGenerator = SimpleStickingPolygonGenerator.INSTANCE;
		this.totalTextBlockHeight = totalTextBlockHeight;
		facetResponse.clear();
		drawer.setEnableDrawing(enableDrawing);
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

	public void updateMinimumWidth(double width) {
		minimumWidth = Math.max(minimumWidth, width);
	}

	public void updateMinimumSize(double width, double height) {
		updateMinimumWidth(width);
		getBuffer().setTopMin(height);
	}

	public double getMinimumWidth() {
		return minimumWidth;
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

	public DrawHandler getDrawer() {
		return drawer;
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

	public double getTotalTextBlockHeight() {
		return totalTextBlockHeight;
	}
}
