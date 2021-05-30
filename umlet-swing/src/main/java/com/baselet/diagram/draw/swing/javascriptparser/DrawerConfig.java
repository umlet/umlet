package com.baselet.diagram.draw.swing.javascriptparser;

public class DrawerConfig {
	private String bgColor;
	private String fgColor;
	private String lineType;
	private Double lineWidth;
	private Double transparency;

	public DrawerConfig() {
		bgColor = null;
		fgColor = null;
		lineType = null;
		lineWidth = null;
		transparency = null;
	}

	public DrawerConfig(String bgColor, String fgColor, String lineType, Double lineWidth, Double transparency) {
		super();
		this.bgColor = bgColor;
		this.fgColor = fgColor;
		this.lineType = lineType;
		this.lineWidth = lineWidth;
		this.transparency = transparency;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getFgColor() {
		return fgColor;
	}

	public void setFgColor(String fgColor) {
		this.fgColor = fgColor;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Double getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(Double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public Double getTransparency() {
		return transparency;
	}

	public void setTransparency(Double transparency) {
		this.transparency = transparency;
	}

	@Override
	public String toString() {
		return "DrawerConfig [bgColor=" + bgColor + ", fgColor=" + fgColor + ", lineType=" + lineType + ", lineWidth=" + lineWidth + ", transparency=" + transparency + "]";
	}

}
