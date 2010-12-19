package com.umlet.element.base.activity;

import java.awt.Graphics2D;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class If extends StartElement {

	private float zoom = (float) Umlet.getInstance().getDiagramHandler().getGridSize() / (float) Constants.defaultGridSize;
	private int h = (int) (40 * zoom);
	private int w = (int) (40 * zoom);

	public If(Graphics2D g, String id) {
		super(g, Const.PAD, id == null ? "If" : id);
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
