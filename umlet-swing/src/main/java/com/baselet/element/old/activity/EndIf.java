package com.baselet.element.old.activity;

import java.awt.Graphics2D;

import com.baselet.diagram.DiagramHandler;

public class EndIf extends StopElement {

	private int h = (int) (40 * getZoom());
	private int w = (int) (40 * getZoom());

	public EndIf(DiagramHandler handler, Graphics2D g, String id) {
		super(handler, g, (int) (Const.PAD * handler.getZoomFactor()), id == null ? "EndIf" : id);
		setHeight(h);
		setWidth(w);
	}

	@Override
	public void paint() {

		int x = getPosition().x;
		int y = getPosition().y;
		getGraphics().drawLine(x, y - h / 2, x + w / 2, y);
		getGraphics().drawLine(x, y - h / 2, x - w / 2, y);
		getGraphics().drawLine(x, y + h / 2, x + w / 2, y);
		getGraphics().drawLine(x, y + h / 2, x - w / 2, y);

	}

	@Override
	public boolean arrowIn() {
		return true;
	}
}
