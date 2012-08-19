package com.umlet.element.experimental;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.umlet.element.experimental.Properties.SettingKey;
import com.umlet.element.experimental.helper.XPoints;
import com.umlet.element.experimental.settings.Settings;

public class PropertiesConfig {

	private AlignHorizontal hAlign;
	private boolean hAlignFixed = false;
	private AlignVertical vAlign;
	private boolean vAlignFixed = false;
	private float yPos = 0;
	private int gridElementHeight;
	private int gridElementWidth;
	private int leftBuffer = 0;
	private int rightBuffer = 0;
	private Settings specificSettings;
	
	public PropertiesConfig(Properties properties, Settings specificSettings, int gridElementHeight, int gridElementWidth) {
		try {
			hAlign = AlignHorizontal.valueOf(properties.getSetting(SettingKey.HorizontalAlign).toUpperCase());
			hAlignFixed = true;
		} catch (Exception e) {
			hAlign = specificSettings.getHAlign();
		}
		try {
			vAlign = AlignVertical.valueOf(properties.getSetting(SettingKey.VerticalAlign).toUpperCase());
			vAlignFixed = true;
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
		this.hAlign = hAlign;
	}

	public AlignVertical getvAlign() {
		return vAlign;
	}

	public void setvAlign(AlignVertical vAlign) {
		this.vAlign = vAlign;
	}

	public boolean ishAlignFixed() {
		return hAlignFixed;
	}

	public boolean isvAlignFixed() {
		return vAlignFixed;
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
	
	public int getGridElementWidth() {
		return gridElementWidth;
	}

	public XPoints getXLimits(float linePos) {
		XPoints xLimits = specificSettings.getXValues(linePos, gridElementHeight, gridElementWidth);
		xLimits.addLeft(leftBuffer);
		xLimits.subRight(rightBuffer);
		return xLimits;
	}
	
}
