package com.baselet.element.facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.control.basics.XValues;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.ElementStyle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.sticking.polygon.SimpleStickingPolygonGenerator;
import com.baselet.element.sticking.polygon.StickingPolygonGenerator;

/**
 * The PropertiesParserState contains the mutable state of the parser which changes constantly while parsing
 * the most important Facets have explicit fields, for any other Facet, the generic facetResponse map should be used
 *
 */
public class PropertiesParserState {

	private final Settings settings;

	private AlignHorizontal hAlign;
	private boolean hAlignGloballySet;
	private AlignVertical vAlign;
	private boolean vAlignGloballySet;
	private double yPos;
	private double calculatedElementWidth;
	private double topBuffer;
	private double leftBuffer;
	private double rightBuffer;
	private Dimension gridElementSize;
	private ElementStyle elementStyle;
	private final Map<Class<? extends Facet>, Object> facetResponse = new HashMap<Class<? extends Facet>, Object>();
	private final List<Facet> usedFacets = new ArrayList<Facet>();

	public PropertiesParserState(Settings settings) {
		this.settings = settings;
	}

	public void resetValues(Dimension gridElementSize) {
		hAlign = settings.getHAlign();
		hAlignGloballySet = false;
		vAlign = settings.getVAlign();
		vAlignGloballySet = false;
		yPos = 0;
		calculatedElementWidth = 0;
		topBuffer = 0;
		leftBuffer = 0;
		rightBuffer = 0;
		this.gridElementSize = gridElementSize;
		elementStyle = settings.getElementStyle();
		facetResponse.clear();
	}

	public PropertiesParserState(Settings settings, Dimension gridElementSize) {
		this(settings);
		resetValues(gridElementSize);
	}

	public AlignHorizontal gethAlign() {
		return hAlign;
	}

	public void sethAlign(AlignHorizontal hAlign) {
		if (!hAlignGloballySet) {
			this.hAlign = hAlign;
		}
	}

	public void sethAlignGlobally(AlignHorizontal hAlign) {
		hAlignGloballySet = true;
		this.hAlign = hAlign;
	}

	public void setvAlignGlobally(AlignVertical vAlign) {
		vAlignGloballySet = true;
		this.vAlign = vAlign;
	}

	public void resetAlign() {
		if (!hAlignGloballySet) {
			hAlign = settings.getHAlign();
		}
		if (!vAlignGloballySet) {
			vAlign = settings.getVAlign();
		}
	}

	public AlignVertical getvAlign() {
		return vAlign;
	}

	public void setvAlign(AlignVertical vAlign) {
		if (!vAlignGloballySet) {
			this.vAlign = vAlign;
		}
	}

	public double getyPos() {
		return yPos;
	}

	public void addToYPos(double inc) {
		yPos += inc;
	}

	public void addToTopBuffer(double inc) {
		topBuffer += inc;
	}

	public void setMinTopBuffer(double minimum) {
		topBuffer = Math.max(topBuffer, minimum);
	}

	public void addToLeftBuffer(double inc) {
		leftBuffer += inc;
	}

	public void addToRightBuffer(double inc) {
		rightBuffer += inc;
	}

	public void addToHorizontalBuffer(double inc) {
		addToLeftBuffer(inc);
		addToRightBuffer(inc);
	}

	public Dimension getGridElementSize() {
		return gridElementSize;
	}

	public double getTopBuffer() {
		return topBuffer;
	}

	public XValues getXLimits(double linePos) {
		XValues xLimits = settings.getXValues(linePos, getGridElementSize().height, getGridElementSize().width);
		xLimits.addLeft(leftBuffer);
		xLimits.subRight(rightBuffer);
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

	private StickingPolygonGenerator stickingPolygonGenerator = SimpleStickingPolygonGenerator.INSTANCE;

	public StickingPolygonGenerator getStickingPolygonGenerator() {
		return stickingPolygonGenerator;
	}

	public void setStickingPolygonGenerator(StickingPolygonGenerator stickingPolygonGenerator) {
		this.stickingPolygonGenerator = stickingPolygonGenerator;
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
}
