// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Color;
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

		int level = 0;
		float yPos = 10 * zoom;
		float xPos = 10 * zoom;

		float actorHeight = 60 * zoom;
		float actorWidth = 14 * zoom;

		Vector<Point> dock = new Vector<Point>();
		Point nextDock = new Point((int) (xPos + actorWidth / 2 + 0.5), (int) (actorHeight + yPos + 0.5));
		dock.add(nextDock);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			int currentLineLevel = calculateLevel(s);
			s = s.replaceAll("\t", "");

			// increase level
			if ((currentLineLevel > level) && (i > 0)) {
				level++;

				nextDock = new Point((int) (xPos + actorWidth / 2 + 0.5), (int) (actorHeight + yPos + 0.5));
				dock.add(nextDock);

				nextDock = dock.elementAt(level - 1);
				drawDockAnchor(g2, nextDock);
			}

			// decrease level
			if ((currentLineLevel < level)) {
				level = currentLineLevel;
			}

			xPos = 10 * zoom + 4 * actorWidth * level;
			drawActor(g2, xPos, yPos, s);

			// draw docks for non root elements
			if (level > 0) {
				// change dock color to red if too much tabs occur
				Color color = g2.getColor();
				if (currentLineLevel > level) g2.setColor(Color.red);

				nextDock = dock.elementAt(level - 1);
				drawDock(g2, nextDock, xPos, yPos);

				// reset color
				g2.setColor(color);
			}

			nextDock = new Point((int) (xPos + actorWidth / 2 + 0.5), (int) (actorHeight + yPos + 0.5));
			dock.set(level, nextDock);

			yPos += actorHeight;
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

	private void drawDock(Graphics2D g2, Point nextDock, float xPos, float yPos) {
		float zoom = getHandler().getZoomFactor();
		float dockHeight = 50 * zoom;

		g2.drawLine(nextDock.x, nextDock.y, nextDock.x, (int) (dockHeight / 2 + yPos + 0.5));
		g2.drawLine(nextDock.x, (int) (dockHeight / 2 + yPos + 0.5), (int) (xPos - 4 * zoom + 0.5), (int) (dockHeight / 2 + yPos + 0.5));
	}

	private void drawActor(Graphics2D g2, float xPos, float yPos, String name) {
		float zoom = getHandler().getZoomFactor();
		float head = 14 * zoom;
		float distance = 10 * zoom;

		g2.drawOval((int) (xPos + 0.5), (int) (yPos + 0.5), (int) (head + 0.5), (int) (head + 0.5));
		g2.drawLine((int) (head / 2 + xPos + 0.5), (int) (head + yPos + 0.5), (int) (head / 2 + xPos + 0.5), (int) (yPos + head * 3 - distance + 0.5));
		g2.drawLine((int) (xPos - 4 * zoom + 0.5), (int) (yPos + head + head / 3 + 0.5), (int) (xPos + head + 4 * zoom + 0.5), (int) (yPos + head + head / 3 + 0.5));
		g2.drawLine((int) (head / 2 + xPos + 0.5), (int) (yPos + head * 3 - distance + 0.5), (int) (head + xPos + 0.5), (int) (yPos + head * 4 - distance + 0.5));
		g2.drawLine((int) (head / 2 + xPos), (int) (yPos + head * 3 - distance + 0.5), (int) (xPos + 0.5), (int) (yPos + head * 4 - distance + 0.5));
		// this.getHandler().writeText(g2, name, xPos, yPos + (int) (4.5*head), false);
		this.getHandler().writeText(g2, name, (int) (xPos + head * 1.5 + this.getHandler().getZoomedDistLineToText() + 0.5), (int) (2 * head + yPos + 0.5), false);
	}

	private void drawDockAnchor(Graphics2D g2, Point nextDock) {
		float zoom = getHandler().getZoomFactor();

		int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
		int[] ykanten = { nextDock.y - (int) (9 * zoom), nextDock.y, nextDock.y };
		int kanten_zahl = 3;
		g2.drawPolygon(new Polygon(xkanten, ykanten, kanten_zahl));
	}

	// calculates the hierarchy level according to tab count in the string
	protected int calculateLevel(String s) {
		int level = 0;
		while ((s.length() > 0) && (s.charAt(0) == '\t')) {
			level++;
			s = s.substring(1);
		}
		// Logger.getAnonymousLogger().info("string=="+s+";index=="+level);
		return level;
	}
}
