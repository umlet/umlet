package com.baselet.gwt.client.view.widgets;

public class FilenameAndScaleHolder {
	private String filename;
	private double scaling;

	public FilenameAndScaleHolder(String filename) {
		this.filename = filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public double getScaling() {
		return scaling;
	}

	public void setScaling(double scaling) {
		this.scaling = scaling;
	}
}
