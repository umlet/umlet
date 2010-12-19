package com.umlet.element.base.activity;

import java.awt.Graphics2D;

import com.umlet.control.diagram.DiagramHandler;

public class If extends StartElement {

	private int h = (int) (40 * getZoom());
	private int w = (int) (40 * getZoom());

	public If(DiagramHandler handler, Graphics2D g, String id) {
		super(handler, g, Const.PAD, id == null ? "If" : id);
		this.setHeight(h);
		this.setWidth(w);
	}

	@Override
	public void paint() {
		int x = this.getPosition().x;
		int y = this.getPosition().y;
		this.getGraphics().drawLine(x, y - h / 2, x + w / 2, y);
		this.getGraphics().drawLine(x, y - h / 2, x - w / 2, y);
		this.getGraphics().drawLine(x, y + h / 2, x + w / 2, y);
		this.getGraphics().drawLine(x, y + h / 2, x - w / 2, y);
	}

	@Override
	public boolean arrowIn() {
		return true;
	}
}
