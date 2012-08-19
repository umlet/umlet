package com.umlet.element.experimental.helper;

import com.umlet.element.experimental.PropertiesConfig;

public class LineHandlerPrint implements LineHandler {

	private PropertiesConfig propCfg;
	
	public LineHandlerPrint(PropertiesConfig propCfg) {
		super();
		this.propCfg = propCfg;
	}

	@Override
	public boolean countOnly() {
		return false;
	}

	@Override
	public void addToYPos(float inc) {
		propCfg.addToYPos(inc);
	}

}
