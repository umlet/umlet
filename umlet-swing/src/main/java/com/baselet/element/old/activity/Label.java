package com.baselet.element.old.activity;

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
		for (String element : this.label) {
			Rectangle2D rect = g.getFont().getStringBounds(element, g.getFontRenderContext());
			w = w < (int) (rect.getMaxX() - rect.getMinX()) ? (int) (rect.getMaxX() - rect.getMinX()) : w;
			h += (int) (rect.getMaxY() - rect.getMinY());
			line_height = (int) (rect.getMaxY() - rect.getMinY());
		}

		setWidth(w);
		setHeight(h);
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
		Point cord = getPosition();
		int y = cord.y - getHeight() / 2 - getPadding();
		for (int i = 0; i < label.length; i++) {
			getGraphics().drawString(label[i], cord.x - getWidth() / 2,
					y + getHeight() - (label.length - i - 1) * line_height - 1);
		}
	}
}
