// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Node extends Entity {

	public Node() {
		super();
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		int size_3d = (int) (10 * zoom);
		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRect(0, size_3d, this.getWidth() - size_3d - 1, this.getHeight() - size_3d - 1);

		Polygon p = new Polygon();
		p.addPoint(this.getWidth() - size_3d - 1, this.getHeight() - 1);
		p.addPoint(this.getWidth() - size_3d - 1, size_3d);
		p.addPoint(this.getWidth() - 1, 0);
		p.addPoint(this.getWidth() - 1, this.getHeight() - size_3d - 1);

		Polygon p1 = new Polygon();
		p1.addPoint(0, size_3d);
		p1.addPoint(size_3d, 0);
		p1.addPoint(this.getWidth() - 1, 0);
		p1.addPoint(this.getWidth() - size_3d - 1, size_3d);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.setColor(new Color(230, 230, 230));
		g2.fillPolygon(p);
		g2.fillPolygon(p1);
		g2.setComposite(composites[0]);
		g2.setColor(_activeColor);

		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawRect(0, size_3d, this.getWidth() - size_3d - 1, this.getHeight() - size_3d - 1);
		// draw polygons by hand to avoid double painted line
		g2.drawLine(0, size_3d, size_3d, 0);
		g2.drawLine(size_3d, 0, this.getWidth() - 1, 0);
		g2.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - size_3d - 1);
		g2.drawLine(this.getWidth() - 1, this.getHeight() - size_3d - 1, this.getWidth() - size_3d - 1, this.getHeight() - 1);
		g2.drawLine(this.getWidth() - size_3d - 1, size_3d, this.getWidth() - 1, 0);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = (int) this.getHandler().getZoomedDistLineToText();
		yPos = yPos + size_3d;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getZoomedFontsize();
			if (s.startsWith("center:")) {
				s = s.substring(7);
				this.getHandler().writeText(g2, s, (this.getWidth() - size_3d - 1) / 2, yPos, true);
			}
			else this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize() / 2, yPos, false);

			yPos += this.getHandler().getZoomedDistTextToText();
		}

	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		int size_3d = 10;
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y + size_3d));
		p.addPoint(new Point(x, y + height));
		p.addPoint(new Point(x + width - size_3d, y + height));
		p.addPoint(new Point(x + width, y + height - size_3d));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + size_3d, y), true);
		return p;
	}
}
