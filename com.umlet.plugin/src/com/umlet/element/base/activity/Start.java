package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class Start extends Element {

	private float zoom = (float) Umlet.getInstance().getDiagramHandler().getGridSize() / (float) Constants.defaultGridSize;

	public Start(Graphics2D g) {
		super(g, Const.PAD, null);
		this.setWidth((int) (21 * zoom));
		this.setHeight((int) (21 * zoom));
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	public void paint() {
		Point cord = this.getPosition();
		this.getGraphics().fillArc(cord.x - (int) (10 * zoom), cord.y - (int) (10 * zoom), (int) (21 * zoom), (int) (21 * zoom), 0, 360);
	}
}
