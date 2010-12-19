// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Decision extends Entity {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		Polygon poly = new Polygon();
		poly.addPoint(this.getWidth() / 2, 0);
		poly.addPoint(this.getWidth(), this.getHeight() / 2);
		poly.addPoint(this.getWidth() / 2, this.getHeight());
		poly.addPoint(0, this.getHeight() / 2);

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawPolygon(poly);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		y += 1;
		width += 1;
		p.addPoint(new Point(x + width / 2, y));
		p.addPoint(new Point(x + width, y + height / 2));
		p.addPoint(new Point(x + width / 2, y + height));
		p.addPoint(new Point(x, y + height / 2), true);
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
