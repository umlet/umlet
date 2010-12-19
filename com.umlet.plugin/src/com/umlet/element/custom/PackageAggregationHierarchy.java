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

		// init graph and colors
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		// extract property strings
		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		// draw bounding box
		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		int level = 0;
		int yPos = (int) (10 * zoom);
		int packageHeight = (int) this.getHandler().getZoomedFontsize();
		int packageWidth = 2 * (int) this.getHandler().getZoomedFontsize();
		int in = (int) (packageWidth * 1.5);

		Vector<Point> dock = new Vector<Point>();
		Point nextDock = new Point(packageWidth / 3 + (int) (10 * zoom), 2 * packageHeight + (int) (10 * zoom));
		dock.add(nextDock);

		// draw root package
		drawPackage(g2, (int) (10 * zoom), yPos, tmp.firstElement());
		yPos += 2 * packageHeight + this.getHandler().getZoomedDistTextToText();
		nextDock = new Point(in + packageWidth / 3 + (int) (10 * zoom), 2 * packageHeight + yPos);
		dock.add(nextDock);
		level++;

		for (int i = 1; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			int currentLineLevel = calculateLevel(s);
			s = s.replaceAll("\t", "");

			if ((currentLineLevel > level) && (level > 0) && (yPos > 4 * packageHeight)) {

				level++;
				in += (int) (packageWidth * 1.5);

				nextDock = new Point(in + packageWidth / 3, 2 * packageHeight + yPos);
				dock.add(nextDock);
				nextDock = dock.elementAt(level - 1);

				drawDock(g2, nextDock, in, yPos);
				drawDockAnchor(g2, nextDock);
				drawPackage(g2, in, yPos, s);
			}
			else if ((currentLineLevel < level) && (level > 1)) {

				level = currentLineLevel;
				in = (int) (packageWidth * level * 1.5);

				nextDock = dock.elementAt(level - 1);

				drawDock(g2, nextDock, in, yPos);
				drawDockAnchor(g2, nextDock);
				drawPackage(g2, in, yPos, s);
			}
			else {
				nextDock = dock.elementAt(level - 1);

				drawDock(g2, nextDock, in, yPos);
				drawDockAnchor(g2, nextDock);
				drawPackage(g2, in, yPos, s);
			}

			nextDock = new Point(in + packageWidth / 3, 2 * packageHeight + yPos);
			dock.set(level, nextDock);

			yPos += 2 * packageHeight + this.getHandler().getZoomedDistTextToText();
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

	private void drawDockAnchor(Graphics2D g2, Point nextDock) {
		// Logger.getAnonymousLogger().info("\ndrawDock: \ndock.x/y: " + nextDock.x + "/" + nextDock.y);
		float zoom = getHandler().getZoomFactor();

		g2.drawOval(nextDock.x - (int) (5 * zoom), nextDock.y - (int) (10 * zoom), (int) (10 * zoom), (int) (10 * zoom));
		g2.drawLine(nextDock.x, nextDock.y - (int) (8 * zoom), nextDock.x, nextDock.y - (int) (2 * zoom));
		g2.drawLine(nextDock.x - (int) (3 * zoom), nextDock.y - (int) (5 * zoom), nextDock.x + (int) (3 * zoom), nextDock.y - (int) (5 * zoom));
	}

	private void drawDock(Graphics2D g2, Point nextDock, int xPos, int yPos) {
		// Logger.getAnonymousLogger().info("\ndrawDock: \ndock.x/y: " + nextDock.x + "/" + nextDock.y + "\nx/yPos: " + xPos + "/" + yPos);
		int packageHeight = (int) this.getHandler().getZoomedFontsize();

		g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight / 2 + yPos);
		g2.drawLine(nextDock.x, packageHeight / 2 + yPos, xPos, packageHeight / 2 + yPos);
	}

	private void drawPackage(Graphics2D g2, int xPos, int yPos, String name) {
		// Logger.getAnonymousLogger().info("\nxPos: " + xPos + "\nyPos: " + yPos + "\nzoom: " + zoom + "\nname: " + name);
		int packageHeight = (int) this.getHandler().getZoomedFontsize();
		int packageWidth = 2 * (int) this.getHandler().getZoomedFontsize();

		g2.drawRect(xPos, 0 + yPos, packageWidth / 3, packageHeight / 4);
		g2.drawRect(xPos, packageHeight / 4 + yPos, packageWidth, packageHeight);

		this.getHandler().writeText(g2, name, xPos + packageWidth + (int) this.getHandler().getZoomedDistLineToText(), packageHeight + yPos, false);
	}

	// calculates the hierarchy level according to tab count in the string
	protected int calculateLevel(String s) {
		int level = 1;
		while ((s.length() > 0) && (s.charAt(0) == '\t')) {
			level++;
			s = s.substring(1);
		}
		// Logger.getAnonymousLogger().info("string=="+s+";index=="+level);
		return level;
	}

}
