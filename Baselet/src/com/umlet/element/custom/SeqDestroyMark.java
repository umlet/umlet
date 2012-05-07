package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class SeqDestroyMark extends GridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		Rectangle r = this.getBounds();
		g2.drawLine(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
		g2.drawLine((int) r.getWidth() - 1, 0, 0, (int) r.getHeight() - 1);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		int px = x + width / 2;
		int py = y + height / 2;
		p.addPoint(new Point(px - 4, py - 4));
		p.addPoint(new Point(px + 4, py - 4));
		p.addPoint(new Point(px + 4, py + 4));
		p.addPoint(new Point(px - 4, py + 4), true);
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
