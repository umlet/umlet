package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import com.baselet.diagram.DiagramHandler;


public class Label extends Element {

	private String[] label;
	private int line_height;

	public Label(DiagramHandler handler, String label, Graphics2D g, int padding) {
		super(handler, g, padding, null);
		this.label = label.split("\\\\");

		int w = 0, h = 0;
		for (int i = 0; i < this.label.length; i++) {
			Rectangle2D rect = g.getFont().getStringBounds(this.label[i], g.getFontRenderContext());
			w = w < (int) (rect.getMaxX() - rect.getMinX()) ? (int) (rect.getMaxX() - rect.getMinX()) : w;
			h += (int) (rect.getMaxY() - rect.getMinY());
			this.line_height = (int) (rect.getMaxY() - rect.getMinY());
		}

		this.setWidth(w);
		this.setHeight(h);
	}

	@Override
	public boolean connectIn() {
		return false;
	}

	@Override
	public boolean connectOut_overrideable() {
		return false;
	}

	@Override
	public void paint() {
		Point cord = this.getPosition();
		int y = cord.y - this.getHeight() / 2 - this.getPadding();
		for (int i = 0; i < this.label.length; i++)
			this.getGraphics().drawString(this.label[i], cord.x - this.getWidth() / 2,
					y + this.getHeight() - (this.label.length - i - 1) * this.line_height - 1);
	}
}
