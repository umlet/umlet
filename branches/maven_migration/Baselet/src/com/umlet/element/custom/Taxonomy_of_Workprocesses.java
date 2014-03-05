package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;


@SuppressWarnings("serial")
public class Taxonomy_of_Workprocesses extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		int level = 0;
		double yPos = 10 * zoom;
		double xPos = 10 * zoom;
		double dist = 10 * Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		double ovalHeight = 3 * Main.getHandlerForElement(this).getFontHandler().getFontSize();
		double ovalWidth = 10 * Main.getHandlerForElement(this).getFontHandler().getFontSize();
		Point nextDock = new Point((int) (xPos + ovalWidth / 2 + 0.5), (int) (ovalHeight + yPos + 0.5));
		Vector<Point> dock = new Vector<Point>();
		dock.add(nextDock);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			int currentLineLevel = calculateLevel(s);
			s = s.replaceAll("\t", "");

			if ((currentLineLevel > level) && (i > 0)) {
				level++;

				nextDock = new Point((int) (xPos + ovalWidth / 2 + 0.5), (int) (ovalHeight + yPos + 0.5));
				dock.add(nextDock);

				nextDock = dock.elementAt(level - 1);
				drawDockAnchor(g2, nextDock);
			}

			if ((currentLineLevel < level)) {
				level = currentLineLevel;
			}

			xPos = 10 * zoom + ovalWidth * level;
			drawProcess(g2, xPos, yPos, s);

			if (level > 0) {
				// change dock color to red if too much tabs occur
				Color color = g2.getColor();
				if (currentLineLevel > level) g2.setColor(Color.red);

				nextDock = dock.elementAt(level - 1);
				drawDock(g2, nextDock, xPos, yPos);

				// reset color
				g2.setColor(color);
			}

			nextDock = new Point((int) (xPos + ovalWidth / 2 + 0.5), (int) (yPos + ovalHeight + 0.5));
			dock.set(level, nextDock);
			yPos += 2 * ovalHeight - dist;
		}
	}

	public StickingPolygon generateStickingBorder() {
		StickingPolygon p = new StickingPolygon(0, 0);
		return p;
	}

	private void drawProcess(Graphics2D g2, double xPos, double yPos, String name) {
		double zoom = Main.getHandlerForElement(this).getZoomFactor();
		double ovalHeight = 3 * Main.getHandlerForElement(this).getFontHandler().getFontSize();
		double ovalWidth = 10 * Main.getHandlerForElement(this).getFontHandler().getFontSize();

		g2.drawOval((int) (xPos + 0.5), (int) (yPos + 0.5), (int) (ovalWidth + 0.5), (int) (ovalHeight + 0.5));
		Main.getHandlerForElement(this).getFontHandler().writeText(g2, name, (int) (xPos + ovalWidth / 2 + 0.5), (int) (yPos + 5 * zoom + ovalHeight / 2 + 0.5), AlignHorizontal.CENTER);
	}

	private void drawDock(Graphics2D g2, Point nextDock, double xPos, double yPos) {
		double zoom = Main.getHandlerForElement(this).getZoomFactor();
		double ovalHeight = 3 * Main.getHandlerForElement(this).getFontHandler().getFontSize();

		g2.drawLine(nextDock.x, (int) (nextDock.y + 9 * zoom + 0.5), nextDock.x, (int) (ovalHeight / 2 + yPos + 0.5));
		g2.drawLine(nextDock.x, (int) (ovalHeight / 2 + yPos + 0.5), (int) (xPos + 0.5), (int) (ovalHeight / 2 + yPos + 0.5));
	}

	private void drawDockAnchor(Graphics2D g2, Point nextDock) {
		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		int[] xkanten = { nextDock.x, nextDock.x + (int) (6 * zoom), nextDock.x - (int) (6 * zoom) };
		int[] ykanten = { nextDock.y, nextDock.y + (int) (9 * zoom), nextDock.y + (int) (9 * zoom) };
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
