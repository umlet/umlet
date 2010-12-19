package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.awt.Point;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;

public class AEnd extends End {

	private float zoom = (float) Umlet.getInstance().getDiagramHandler().getGridSize() / (float) Constants.defaultGridSize;

	public AEnd(Graphics2D g, String id) {
		super(g, id == null ? "AEnd" : id);
	}

	@Override
	public void paint() {

		Point cord = this.getPosition();
		this.getGraphics().drawArc(cord.x - (int) (10 * zoom), cord.y - (int) (10 * zoom), (int) (21 * zoom), (int) (21 * zoom), 0, 360);
		int delta = (int) (21 * zoom / 2 / Math.sqrt(2));
		this.getGraphics().drawLine(cord.x - delta, cord.y - delta, cord.x + delta, cord.y + delta);
		this.getGraphics().drawLine(cord.x - delta, cord.y + delta, cord.x + delta, cord.y - delta);
	}
}
