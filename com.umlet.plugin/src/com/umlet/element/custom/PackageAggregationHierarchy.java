// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class PackageAggregationHierarchy extends com.umlet.element.base.Entity {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		boolean root = true;
		int level = 0;
		int yPos = (int) (10 * zoom);
		int packageHeight = (int) this.getHandler().getZoomedFontsize();
		int packageWidth = 2 * (int) this.getHandler().getZoomedFontsize();
		int in = (int) (packageWidth * 1.5);
		Point nextDock = new Point(packageWidth / 3 + (int) (10 * zoom), 2 * packageHeight + (int) (10 * zoom));

		Vector<Point> dock = new Vector<Point>();
		dock.add(nextDock);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (root) {
				// Root
				g2.drawRect((int) (10 * zoom), 0 + yPos, packageWidth / 3, packageHeight / 4);
				g2.drawRect((int) (10 * zoom), packageHeight / 4 + yPos, packageWidth, packageHeight);
				this.getHandler().writeText(g2, s, packageWidth + (int) this.getHandler().getZoomedDistLineToText() + (int) (10 * zoom), packageHeight + yPos, false);
				yPos += 2 * packageHeight + this.getHandler().getZoomedDistTextToText();
				nextDock = new Point(in + packageWidth / 3 + (int) (10 * zoom), 2 * packageHeight + yPos);
				dock.add(nextDock);
				level++;
				root = false;

			}
			else if (s.startsWith(">") && (level > 0) && !root && (yPos > 4 * packageHeight)) {

				level++;
				in += (int) (packageWidth * 1.5);

				nextDock = new Point(in + packageWidth / 3, 2 * packageHeight + yPos);
				dock.add(nextDock);

				nextDock = dock.elementAt(level - 1);
				g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight / 2 + yPos);
				g2.drawLine(nextDock.x, packageHeight / 2 + yPos, in, packageHeight / 2 + yPos);
				// draw end of Line
				g2.drawOval(nextDock.x - (int) (5 * zoom), nextDock.y - (int) (10 * zoom), (int) (10 * zoom), (int) (10 * zoom));
				g2.drawLine(nextDock.x, nextDock.y - (int) (8 * zoom), nextDock.x, nextDock.y - (int) (2 * zoom));
				g2.drawLine(nextDock.x - (int) (3 * zoom), nextDock.y - (int) (5 * zoom), nextDock.x + (int) (3 * zoom), nextDock.y - (int) (5 * zoom));
				// draw Package
				g2.drawRect(in, 0 + yPos, packageWidth / 3, packageHeight / 4);
				g2.drawRect(in, packageHeight / 4 + yPos, packageWidth, packageHeight);
				this.getHandler().writeText(g2, s.substring(1), in + packageWidth + (int) this.getHandler().getZoomedDistTextToText(), packageHeight + yPos, false);

				nextDock = new Point(in + packageWidth / 3, 2 * packageHeight + yPos);
				dock.set(level, nextDock);

				yPos += 2 * packageHeight + this.getHandler().getZoomedDistTextToText();

			}
			else if (s.startsWith("<") && (level > 1)) {

				do {
					level--;
					in -= (int) (packageWidth * 1.5);
					s = s.substring(1);
				}
				while (s.startsWith("<") && (level > 1));

				nextDock = dock.elementAt(level - 1);
				g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight / 2 + yPos);
				g2.drawLine(nextDock.x, packageHeight / 2 + yPos, in, packageHeight / 2 + yPos);
				// draw end of Line
				g2.drawOval(nextDock.x - (int) (5 * zoom), nextDock.y - (int) (10 * zoom), (int) (10 * zoom), (int) (10 * zoom));
				g2.drawLine(nextDock.x, nextDock.y - (int) (8 * zoom), nextDock.x, nextDock.y - (int) (2 * zoom));
				g2.drawLine(nextDock.x - (int) (3 * zoom), nextDock.y - (int) (5 * zoom), nextDock.x + (int) (3 * zoom), nextDock.y - (int) (5 * zoom));
				// draw Package
				g2.drawRect(in, 0 + yPos, packageWidth / 3, packageHeight / 4);
				g2.drawRect(in, packageHeight / 4 + yPos, packageWidth, packageHeight);
				this.getHandler().writeText(g2, s, in + packageWidth + (int) this.getHandler().getZoomedDistTextToText(), packageHeight + yPos, false);

				nextDock = new Point(in + packageWidth / 3, 2 * packageHeight + yPos);
				dock.set(level, nextDock);

				yPos += 2 * packageHeight + this.getHandler().getZoomedDistTextToText();

			}
			else {

				nextDock = dock.elementAt(level - 1);
				g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight / 2 + yPos);
				g2.drawLine(nextDock.x, packageHeight / 2 + yPos, in, packageHeight / 2 + yPos);
				// draw end of Line
				g2.drawOval(nextDock.x - (int) (5 * zoom), nextDock.y - (int) (10 * zoom), (int) (10 * zoom), (int) (10 * zoom));
				g2.drawLine(nextDock.x, nextDock.y - (int) (8 * zoom), nextDock.x, nextDock.y - (int) (2 * zoom));
				g2.drawLine(nextDock.x - (int) (3 * zoom), nextDock.y - (int) (5 * zoom), nextDock.x + (int) (3 * zoom), nextDock.y - (int) (5 * zoom));
				// draw Package
				g2.drawRect(in, 0 + yPos, packageWidth / 3, packageHeight / 4);
				g2.drawRect(in, packageHeight / 4 + yPos, packageWidth, packageHeight);
				this.getHandler().writeText(g2, s, in + packageWidth + (int) this.getHandler().getZoomedDistTextToText(), packageHeight + yPos, false);

				nextDock = new Point(in + packageWidth / 3, 2 * packageHeight + yPos);
				dock.set(level, nextDock);

				yPos += 2 * packageHeight + this.getHandler().getZoomedDistTextToText();

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
