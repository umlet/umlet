package com.baselet.element.old.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.Direction;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;

@SuppressWarnings("serial")
public class SeqDestroyMark extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		colorize(g2); // enable colors
		g2.setColor(fgColor);

		Rectangle r = getRectangle();
		g2.drawLine(0, 0, r.getWidth() - 1, r.getHeight() - 1);
		g2.drawLine(r.getWidth() - 1, 0, 0, r.getHeight() - 1);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon(0, 0);
		int px = x + width / 2;
		int py = y + height / 2;
		p.addPoint(px - 4, py - 4);
		p.addPoint(px + 4, py - 4);
		p.addPoint(px + 4, py + 4);
		p.addPoint(px - 4, py + 4, true);
		return p;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
}
