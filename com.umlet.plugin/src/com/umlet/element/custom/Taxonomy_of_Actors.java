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
public class Taxonomy_of_Actors extends com.umlet.element.base.Entity {

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

		boolean first = true;
		boolean root = true;
		int level = 0;
		float yPos = 10 * zoom;
		float head = 14 * zoom;
		float abstand = 10 * zoom;
		int count = 2;
		String help = "";
		Point nextDock = new Point(0, 0);

		Vector<Point> dock = new Vector<Point>();
		// dock.add(nextDock);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (root) {
				// Root
				g2.drawOval((int) (10 * zoom), (int) (yPos), (int) (head), (int) (head));
				g2.drawLine((int) (head / 2 + 10 * zoom), (int) (head + yPos), (int) (head / 2 + 10 * zoom), (int) (head * 3));
				g2.drawLine((int) (6 * zoom), (int) (yPos + head + head / 3), (int) (head + 14 * zoom), (int) (yPos + head + head / 3));
				g2.drawLine((int) (head / 2 + 10 * zoom), (int) (head * 3), (int) (head + 10 * zoom), (int) (head * 4));
				g2.drawLine((int) (head / 2 + 10 * zoom), (int) (head * 3), (int) (10 * zoom), (int) (head * 4));
				this.getHandler().writeText(g2, s, (int) (10 * zoom), (int) (head * 5), false);
				yPos += 8 * head;
				nextDock = new Point((int) (head / 2 + 10 * zoom), (int) (5 * head + 4 * zoom));
				dock.add(nextDock);
				count = count * 2;
				level++;
				root = false;
				help = s;

			}
			else if (s.startsWith(">") && (level > 0) && !root && !first) {

				level++;
				nextDock = dock.elementAt(level - 1);
				nextDock = new Point(nextDock.x, nextDock.y);
				dock.add(nextDock);
				nextDock = dock.elementAt(level - 1);
				int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
				int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
				int kanten_zahl = 3;

				g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));

				g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom), nextDock.x, (int) (yPos - abstand));
				g2.drawLine(nextDock.x, (int) (yPos - abstand), (int) (nextDock.x + abstand * 6), (int) (yPos - abstand));
				g2.drawOval((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos - 3 * head), (int) (head), (int) (head));

				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - 2 * head), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand));
				g2.drawLine((int) ((nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2) - 4 * zoom), (int) (yPos - 2 * head + head / 3), (int) ((nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2) + head + 4 * zoom), (int) (yPos - 2 * head + head / 3));
				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head), (int) (yPos + count));
				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos + count), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand));
				if (s.length() > 1) {
					this.getHandler().writeText(g2, s.substring(1), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos + head + count), false);
				}
				nextDock = new Point((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos + head + count * 2));
				dock.add(level, nextDock);
				yPos += 5 * head;
				if ((i < tmp.size() - 1) && (tmp.elementAt(i + 1)).startsWith(">")) {
					help = help + s;
				}
			}
			else if (s.startsWith("<") && (level > 1)) {
				do {
					level--;
					s = s.substring(1, s.length());
				}
				while (s.startsWith("<") && (level > 1));

				nextDock = dock.elementAt(level - 1);
				int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
				int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
				int kanten_zahl = 3;
				g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));

				g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom), nextDock.x, (int) (yPos - abstand));
				g2.drawLine(nextDock.x, (int) (yPos - abstand), (int) (nextDock.x + abstand * 6), (int) (yPos - abstand));
				g2.drawOval((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos - 3 * head), (int) (head), (int) (head));

				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - 2 * head), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand));
				g2.drawLine((int) ((nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2) - 4 * zoom), (int) (yPos - 2 * head + head / 3), (int) ((nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2) + head + 4 * zoom), (int) (yPos - 2 * head + head / 3));
				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head), (int) (yPos + count));
				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos + count), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand));
				if (s.length() > 0) {
					this.getHandler().writeText(g2, s, (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos + head + count), false);
				}
				nextDock = new Point((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos + head + count * 2));
				dock.add(level, nextDock);
				yPos += 5 * head;
			}
			else {
				first = false;
				nextDock = dock.elementAt(level - 1);
				int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
				int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
				int kanten_zahl = 3;
				g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));

				g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom), nextDock.x, (int) (yPos - abstand));
				g2.drawLine(nextDock.x, (int) (yPos - abstand), (int) (nextDock.x + abstand * 6), (int) (yPos - abstand));
				g2.drawOval((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos - 3 * head), (int) (head), (int) (head));

				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - 2 * head), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand));
				g2.drawLine((int) ((nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2) - 4 * zoom), (int) (yPos - 2 * head + head / 3), (int) ((nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2) + head + 4 * zoom), (int) (yPos - 2 * head + head / 3));
				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head), (int) (yPos + count));
				g2.drawLine((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos + count), (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos - abstand));

				this.getHandler().writeText(g2, s, (int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2), (int) (yPos + count + head), false);

				nextDock = new Point((int) (nextDock.x + abstand * 6 + (int) this.getHandler().getZoomedFontsize() / 2 + head / 2), (int) (yPos + head + count * 2));
				dock.add(level, nextDock);
				yPos += 5 * head;

				// count = count*2;
				if ((i < tmp.size() - 1) && (tmp.elementAt(i + 1)).startsWith("::")) {
					help = help + s;
				}
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
