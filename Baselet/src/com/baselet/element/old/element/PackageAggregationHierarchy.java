package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;

@SuppressWarnings("serial")
public class PackageAggregationHierarchy extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		// init graph and colors
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		// extract property strings
		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());

		// draw bounding box
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}
		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);

		// init coordinates;
		int level = 0;
		double yPos = 10 * zoom;
		double xPos = 10 * zoom;
		double packageHeight = HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
		double packageWidth = 2 * HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();

		Vector<Point> dock = new Vector<Point>();

		Point nextDock = new Point((int) (xPos + packageWidth / 3 + 0.5), (int) (2 * packageHeight + yPos + 0.5));
		dock.add(nextDock);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			int currentLineLevel = calculateLevel(s);
			s = s.replaceAll("\t", "");

			// increase level
			if (currentLineLevel > level && i > 0) {
				level++;

				nextDock = new Point((int) (xPos + packageWidth / 3 + 0.5), (int) (2 * packageHeight + yPos + 0.5));
				dock.add(nextDock);

				nextDock = dock.elementAt(level - 1);
				drawDockAnchor(g2, nextDock);
			}

			// decrease level
			if (currentLineLevel < level) {
				level = currentLineLevel;
			}

			xPos = 10 * zoom + (float) (packageWidth * level * 1.5);
			drawPackage(g2, xPos, yPos, packageHeight, packageWidth, s);

			// draw docks for non root elements
			if (level > 0) {
				// change dock color to red if too much tabs occur
				Color color = g2.getColor();
				if (currentLineLevel > level) {
					g2.setColor(Color.red);
				}

				nextDock = dock.elementAt(level - 1);
				drawDock(g2, nextDock, xPos, yPos, packageHeight);

				// reset color
				g2.setColor(color);
			}

			nextDock = new Point((int) (xPos + packageWidth / 3 + 0.5), (int) (2 * packageHeight + yPos + 0.5));
			dock.set(level, nextDock);

			yPos += 2 * packageHeight + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}
	}

	@Override
	public StickingPolygon generateStickingBorder() {
		StickingPolygon p = new StickingPolygon(0, 0);

		return p;
	}

	private void drawDockAnchor(Graphics2D g2, Point nextDock) {
		// Logger.getAnonymousLogger().info("\ndrawDock: \ndock.x/y: " + nextDock.x + "/" + nextDock.y);
		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		g2.drawOval(nextDock.x - (int) (5 * zoom + 0.5), nextDock.y - (int) (10 * zoom + 0.5), (int) (10 * zoom + 0.5), (int) (10 * zoom + 0.5));
		g2.drawLine(nextDock.x, nextDock.y - (int) (8 * zoom + 0.5), nextDock.x, nextDock.y - (int) (2 * zoom + 0.5));
		g2.drawLine(nextDock.x - (int) (3 * zoom + 0.5), nextDock.y - (int) (5 * zoom + 0.5), nextDock.x + (int) (3 * zoom + 0.5), nextDock.y - (int) (5 * zoom + 0.5));
	}

	private void drawDock(Graphics2D g2, Point nextDock, double xPos, double yPos, double packageHeight) {
		// Logger.getAnonymousLogger().info("\ndrawDock: \ndock.x/y: " + nextDock.x + "/" + nextDock.y + "\nx/yPos: " + xPos + "/" + yPos);

		g2.drawLine(nextDock.x, nextDock.y, nextDock.x, (int) (packageHeight / 2 + 0.5) + (int) (yPos + 0.5));
		g2.drawLine(nextDock.x, (int) (packageHeight / 2 + 0.5) + (int) (yPos + 0.5), (int) (xPos + 0.5), (int) (packageHeight / 2 + 0.5) + (int) (yPos + 0.5));
	}

	private void drawPackage(Graphics2D g2, double xPos, double yPos, double packageHeight, double packageWidth, String name) {
		// Logger.getAnonymousLogger().info("\nxPos: " + xPos + "\nyPos: " + yPos + "\nzoom: " + zoom + "\nname: " + name);

		g2.drawRect((int) (xPos + 0.5), (int) (yPos + 0.5), (int) (packageWidth / 3 + 0.5), (int) (packageHeight / 4 + 0.5));
		g2.drawRect((int) (xPos + 0.5), (int) (packageHeight / 4 + 0.5) + (int) (yPos + 0.5), (int) (packageWidth + 0.5), (int) (packageHeight + 0.5));

		HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, name, (int) (xPos + packageWidth + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + 0.5), (int) (packageHeight + yPos + 0.5), AlignHorizontal.LEFT);
	}

	// calculates the hierarchy level according to tab count in the string
	protected int calculateLevel(String s) {
		int level = 0;
		while (s.length() > 0 && s.charAt(0) == '\t') {
			level++;
			s = s.substring(1);
		}
		// Logger.getAnonymousLogger().info("string=="+s+";index=="+level);
		return level;
	}

}
