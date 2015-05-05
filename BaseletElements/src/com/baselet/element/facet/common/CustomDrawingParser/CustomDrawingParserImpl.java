package com.baselet.element.facet.common.CustomDrawingParser;

import com.baselet.diagram.draw.DrawHandler;

public class CustomDrawingParserImpl extends CustomDrawingParser {

	private final double width;
	private final double height;
	private final DrawHandler drawHandler;

	public CustomDrawingParserImpl(CustomDrawingParserTokenManager tm, double width, double height, DrawHandler drawHandler) {
		super(tm);
		this.width = width;
		this.height = height;
		this.drawHandler = drawHandler;
	}

	public CustomDrawingParserImpl(String dsl, double width, double height, DrawHandler drawHandler) throws ParseException, TokenMgrException {
		super(dsl);
		this.width = width;
		this.height = height;
		this.drawHandler = drawHandler;
	}

	public CustomDrawingParserImpl(Provider stream, double width, double height, DrawHandler drawHandler) {
		super(stream);
		this.width = width;
		this.height = height;
		this.drawHandler = drawHandler;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public DrawHandler getDrawHandler() {
		return drawHandler;
	}

}
