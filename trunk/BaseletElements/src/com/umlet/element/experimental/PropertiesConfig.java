package com.umlet.element.experimental;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.XValues;
import com.umlet.element.experimental.facets.defaults.ElementStyleFacet.ElementStyleEnum;
import com.umlet.element.experimental.facets.defaults.LayerFacet;

public class PropertiesConfig {
	
	private Settings settings;
	
	private AlignHorizontal hAlign;
	private boolean hAlignGloballySet;
	private AlignVertical vAlign;
	private boolean vAlignGloballySet;
	private Double yPos;
	private double calculatedElementWidth;
	private int leftBuffer;
	private int rightBuffer;
	private Dimension gridElementSize;
	private ElementStyleEnum elementStyle;
	private Integer layer;
	private String group;

	public PropertiesConfig(Settings settings) {
		this.settings = settings;
	}

	void resetValues(Dimension gridElementSize) {
		hAlign = settings.getHAlign();
		hAlignGloballySet = false;
		vAlign = settings.getVAlign();
		vAlignGloballySet = false;
		yPos = null;
		calculatedElementWidth = settings.getMinElementWidthForAutoresize();
		leftBuffer = 0;
		rightBuffer = 0;
		this.gridElementSize = gridElementSize;
		elementStyle = settings.getElementStyle();
		layer = LayerFacet.DEFAULT_VALUE;
		group = "";
	}

	public PropertiesConfig(Settings settings, Dimension gridElementSize) {
		this(settings);
		resetValues(gridElementSize);
	}

	public AlignHorizontal gethAlign() {
		return hAlign;
	}

	public void sethAlign(AlignHorizontal hAlign) {
		if (!hAlignGloballySet) this.hAlign = hAlign;
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
		if (!hAlignGloballySet) this.hAlign = settings.getHAlign();
		if (!vAlignGloballySet) this.vAlign = settings.getVAlign();
	}

	public AlignVertical getvAlign() {
		return vAlign;
	}

	public void setvAlign(AlignVertical vAlign) {
		if (!vAlignGloballySet) this.vAlign = vAlign;
	}

	public double getyPos() {
		if (yPos == null) {
			yPos = settings.getYPosStart();
		}
		return yPos;
	}

	public void addToYPos(double inc) {
		if (yPos == null) { // get yPos from settings the first time it would be modified, because initialization in constructor would be too early (eg: it could depend on some settings of preparsefacets like fontsize)
			yPos = settings.getYPosStart();
		}
		yPos += inc;
	}

	public void addToLeftBuffer(int inc) {
		this.leftBuffer += inc;
	}

	public void addToRightBuffer(int inc) {
		this.rightBuffer += inc;
	}

	public void addToBuffer(int inc) {
		addToLeftBuffer(inc);
		addToRightBuffer(inc);
	}

	public Dimension getGridElementSize() {
		return gridElementSize;
	}

	public XValues getXLimits(double linePos) {
		XValues xLimits = settings.getXValues(linePos, getGridElementSize().height, getGridElementSize().width);
		xLimits.addLeft(leftBuffer);
		xLimits.subRight(rightBuffer);
		return xLimits;
	}

	public XValues getXLimitsForArea(double bottomYPos, double areaHeight) {
		XValues xLimitsTop = getXLimits(bottomYPos);
		XValues xLimitsBottom = getXLimits(bottomYPos - areaHeight);
		return xLimitsTop.intersect(xLimitsBottom);
	}

	public double getDividerPos(BaseDrawHandler drawer) {
		return getyPos() - drawer.textHeight() + 2;
	}

	public void updateCalculatedElementWidth(double width) {
		calculatedElementWidth = Math.max(calculatedElementWidth, width);
	}

	public double getCalculatedElementWidth() {
		return calculatedElementWidth;
	}

	public ElementStyleEnum getElementStyle() {
		return elementStyle;
	}

	public void setElementStyle(ElementStyleEnum elementStyle) {
		this.elementStyle = elementStyle;
	}

	public void setLayer(Integer layer) {
			this.layer = layer;
	}

	public Integer getLayer() {
		return layer;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}

}
