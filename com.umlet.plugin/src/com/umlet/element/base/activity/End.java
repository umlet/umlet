package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class End extends Element {

	private float zoom = (float) Umlet.getInstance().getDiagramHandler().getGridSize() / (float) Constants.defaultGridSize;

	public End(Graphics2D g, String id) {
		super(g, Const.PAD, id == null ? "End" : id);
		this.setWidth((int) (21 * zoom));
		this.setHeight((int) (21 * zoom));
	}

	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {

		Point cord = this.getPosition();
		this.getGraphics().drawArc(cord.x - (int) (10 * zoom), cord.y - (int) (10 * zoom), (int) (21 * zoom), (int) (21 * zoom), 0, 360);
		this.getGraphics().fillArc(cord.x - (int) (6 * zoom), cord.y - (int) (6 * zoom), (int) (13 * zoom), (int) (13 * zoom), 0, 360);
	}
}
