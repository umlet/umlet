package com.umlet.element.experimental;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.umlet.element.experimental.Properties.SettingKey;
import com.umlet.element.experimental.helper.XPoints;
import com.umlet.element.experimental.settings.Settings;

public class PropertiesConfig {

	private AlignHorizontal hAlign;
	private boolean hAlignManuallyOverwritten = false;
	private AlignVertical vAlign;
	private boolean vAlignManuallyOverwritten = false;
	private float yPos = 0;
	private int gridElementHeight;
	private int gridElementWidth;
	private int leftBuffer = 0;
	private int rightBuffer = 0;
	private Settings specificSettings;
	
	public PropertiesConfig(Properties properties, Settings specificSettings, int gridElementHeight, int gridElementWidth) {
		try {
			hAlign = AlignHorizontal.valueOf(properties.getSetting(SettingKey.HorizontalAlign).toUpperCase());
			hAlignManuallyOverwritten = true;
		} catch (Exception e) {
			hAlign = specificSettings.getHAlign();
		}
		try {
			vAlign = AlignVertical.valueOf(properties.getSetting(SettingKey.VerticalAlign).toUpperCase());
			vAlignManuallyOverwritten = true;
		} catch (Exception e) {
			vAlign = specificSettings.getVAlign();
		}
		this.gridElementHeight = gridElementHeight;
		this.gridElementWidth = gridElementWidth;
		this.specificSettings = specificSettings;
	}

	public AlignHorizontal gethAlign() {
		return hAlign;
	}

	public void sethAlign(AlignHorizontal hAlign) {
		if (!hAlignManuallyOverwritten) this.hAlign = hAlign;
	}
	
	public void resetAlign() {
		if (!hAlignManuallyOverwritten) this.hAlign = specificSettings.getHAlign();
		if (!vAlignManuallyOverwritten) this.vAlign = specificSettings.getVAlign();
	}

	public boolean ishAlignManuallyOverwritten() {
		return hAlignManuallyOverwritten;
	}

	public AlignVertical getvAlign() {
		return vAlign;
	}

	public void setvAlign(AlignVertical vAlign) {
		if (!vAlignManuallyOverwritten) this.vAlign = vAlign;
	}

	public boolean isvAlignManuallyOverwritten() {
		return vAlignManuallyOverwritten;
	}
	
	public float getyPos() {
		return yPos;
	}

	public void addToYPos(float inc) {
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
	
	public int getGridElementWidth() {
		return gridElementWidth;
	}

	public XPoints getXLimits(float linePos) {
		XPoints xLimits = specificSettings.getXValues(linePos, gridElementHeight, gridElementWidth);
		xLimits.addLeft(leftBuffer);
		xLimits.subRight(rightBuffer);
		return xLimits;
	}
	
	public XPoints getXLimitsForArea(float linePos, float areaHeight) {
		XPoints xLimitsTop = getXLimits(linePos);
		XPoints xLimitsBottom = getXLimits(linePos - areaHeight);
		float leftAreaLimit = Math.max(xLimitsTop.getLeft(), xLimitsBottom.getLeft());
		float rightAreaLimit = Math.min(xLimitsTop.getRight(), xLimitsBottom.getRight());
		return new XPoints(leftAreaLimit, rightAreaLimit);
	}
	
}
