package com.umlet.element.custom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;

@SuppressWarnings("serial")
public class Node extends OldGridElement {

	public Node() {
		super();
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		int size_3d = (int) (10 * zoom);
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, size_3d, getRectangle().width - size_3d - 1, getRectangle().height - size_3d - 1);

		Polygon p = new Polygon();
		p.addPoint(getRectangle().width - size_3d - 1, getRectangle().height - 1);
		p.addPoint(getRectangle().width - size_3d - 1, size_3d);
		p.addPoint(getRectangle().width - 1, 0);
		p.addPoint(getRectangle().width - 1, getRectangle().height - size_3d - 1);

		Polygon p1 = new Polygon();
		p1.addPoint(0, size_3d);
		p1.addPoint(size_3d, 0);
		p1.addPoint(getRectangle().width - 1, 0);
		p1.addPoint(getRectangle().width - size_3d - 1, size_3d);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2.setColor(new Color(230, 230, 230));
		g2.fillPolygon(p);
		g2.fillPolygon(p1);
		g2.setComposite(composites[0]);
		g2.setColor(fgColor);

		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g2.drawRect(0, size_3d, getRectangle().width - size_3d - 1, getRectangle().height - size_3d - 1);
		// draw polygons by hand to avoid double painted line
		g2.drawLine(0, size_3d, size_3d, 0);
		g2.drawLine(size_3d, 0, getRectangle().width - 1, 0);
		g2.drawLine(getRectangle().width - 1, 0, getRectangle().width - 1, getRectangle().height - size_3d - 1);
		g2.drawLine(getRectangle().width - 1, getRectangle().height - size_3d - 1, getRectangle().width - size_3d - 1, getRectangle().height - 1);
		g2.drawLine(getRectangle().width - size_3d - 1, size_3d, getRectangle().width - 1, 0);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		yPos = yPos + size_3d;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			if (s.startsWith("center:")) {
				s = s.substring(7);
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - size_3d - 1) / 2, yPos, AlignHorizontal.CENTER);
			}
			else {
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			}

			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		int size_3d = 10;
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(x, y + size_3d);
		p.addPoint(x, y + height);
		p.addPoint(x + width - size_3d, y + height);
		p.addPoint(x + width, y + height - size_3d);
		p.addPoint(x + width, y);
		p.addPoint(x + size_3d, y, true);
		return p;
	}
}
