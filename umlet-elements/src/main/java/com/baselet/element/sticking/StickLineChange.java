package com.baselet.element.sticking;

import com.baselet.element.sticking.StickingPolygon.StickLine;

public class StickLineChange {
	StickLine oldLine;
	StickLine newLine;

	public StickLineChange(StickLine oldLine, StickLine newLine) {
		super();
		this.oldLine = oldLine;
		this.newLine = newLine;
	}

	public StickLine getNew() {
		return newLine;
	}

	public StickLine getOld() {
		return oldLine;
	}

	@Override
	public String toString() {
		return "StickLineChange [oldLine=" + oldLine + ", newLine=" + newLine + "]";
	}

}
