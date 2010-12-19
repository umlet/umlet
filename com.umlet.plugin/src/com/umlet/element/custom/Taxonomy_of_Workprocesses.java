// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class Taxonomy_of_Workprocesses extends com.umlet.element.base.Entity {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		boolean root = true;
		int level = 0;
		float yPos = 10 * zoom;
		float dist = 10 * this.getHandler().getZoomedDistTextToText();
		float ovalHeight = 3 * this.getHandler().getZoomedFontsize();
		float ovalWidth = 10 * this.getHandler().getZoomedFontsize();
		int in = (int) (ovalWidth * 0.5);
		Point nextDock = new Point((int) (yPos + ovalWidth / 2), (int) (ovalHeight + 10 * zoom));

		Vector<Point> dock = new Vector<Point>();
		dock.add(nextDock);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (root) {

				// Root
				g2.drawOval((int) (10 * zoom), (int) (yPos), (int) (ovalWidth), (int) (ovalHeight));
				this.getHandler().writeText(g2, s, (int) (yPos + (ovalWidth / 2)), (int) (ovalHeight / 2 + yPos + 3 * zoom), true);
				nextDock = new Point((int) (in + 10 * zoom), (int) (yPos + ovalHeight));

				yPos += 2 * ovalHeight - (dist / 2);

				dock.add(nextDock);
				level++;
				root = false;

			}
			else if (s.startsWith(">") && (level > 0) && !root) {

				level++;
				nextDock = dock.elementAt(level - 1);
				nextDock = new Point(nextDock.x, nextDock.y);
				dock.add(nextDock);
				nextDock = dock.elementAt(level - 1);
				int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
				int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
				int kanten_zahl = 3;

				g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));
				g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom), nextDock.x, (int) (yPos));
				g2.drawLine(nextDock.x, (int) (yPos), (int) (nextDock.x + (ovalWidth / 2)), (int) (yPos));
				g2.drawOval((int) (nextDock.x + (ovalWidth / 2)), (int) (yPos - (ovalHeight / 2)), (int) (ovalWidth), (int) (ovalHeight));

				if (s.length() > 1) {
					this.getHandler().writeText(g2, s.substring(1), (int) (nextDock.x + ovalWidth), (int) (yPos + 3 * zoom), true);
				}
				nextDock = new Point((int) (nextDock.x + ovalWidth), (int) (yPos + (ovalHeight / 2)));
				dock.set(level, nextDock);
				yPos += 2 * ovalHeight - dist;

			}
			else if (s.startsWith("<") && (level > 1)) {
				do {
					level--;
					// in-=(int)(packageWidth*1.5);
					s = s.substring(1, s.length());
				}
				while (s.startsWith("<") && (level > 1));

				nextDock = dock.elementAt(level - 1);
				int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
				int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
				int kanten_zahl = 3;

				g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));
				g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom), nextDock.x, (int) (yPos));
				g2.drawLine(nextDock.x, (int) (yPos), (int) (nextDock.x + (ovalWidth / 2)), (int) (yPos));
				g2.drawOval((int) (nextDock.x + (ovalWidth / 2)), (int) (yPos - (ovalHeight / 2)), (int) (ovalWidth), (int) (ovalHeight));

				this.getHandler().writeText(g2, s, (int) (nextDock.x + ovalWidth), (int) (yPos + 3 * zoom), true);
				nextDock = new Point((int) (nextDock.x + ovalWidth), (int) (yPos + (ovalHeight / 2)));
				dock.set(level, nextDock);
				yPos += 2 * ovalHeight - dist;
			}
			else {
				nextDock = dock.elementAt(level - 1);
				int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
				int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
				int kanten_zahl = 3;

				g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));
				g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom), nextDock.x, (int) (yPos));
				g2.drawLine(nextDock.x, (int) (yPos), (int) (nextDock.x + (ovalWidth / 2)), (int) (yPos));
				g2.drawOval((int) (nextDock.x + (ovalWidth / 2)), (int) (yPos - (ovalHeight / 2)), (int) (ovalWidth), (int) (ovalHeight));

				this.getHandler().writeText(g2, s, (int) (nextDock.x + ovalWidth), (int) (yPos + 3 * zoom), true);
				nextDock = new Point((int) (nextDock.x + ovalWidth), (int) (yPos + (ovalHeight / 2)));
				dock.set(level, nextDock);
				yPos += 2 * ovalHeight - dist;

			}

		}
	}

	@Override
	public int getPossibleResizeDirections() {
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	public StickingPolygon generateStickingBorder() {
		StickingPolygon p = new StickingPolygon();
		return p;
	}
}
