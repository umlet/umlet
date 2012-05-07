package com.umlet.element.custom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.Utils;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class SeqSelfMessage extends GridElement {

	public SeqSelfMessage() {
		super();
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		float zoom = getHandler().getZoomFactor();
		int size_3d = (int) (10 * zoom);
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
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
		g2.setColor(fgColor);

		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawRect(0, size_3d, this.getWidth() - size_3d - 1, this.getHeight() - size_3d - 1);
		// draw polygons by hand to avoid double painted line
		g2.drawLine(0, size_3d, size_3d, 0);
		g2.drawLine(size_3d, 0, this.getWidth() - 1, 0);
		g2.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - size_3d - 1);
		g2.drawLine(this.getWidth() - 1, this.getHeight() - size_3d - 1, this.getWidth() - size_3d - 1, this.getHeight() - 1);
		g2.drawLine(this.getWidth() - size_3d - 1, size_3d, this.getWidth() - 1, 0);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		yPos = yPos + size_3d;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			if (s.startsWith("center:")) {
				s = s.substring(7);
				this.getHandler().getFontHandler().writeText(g2, s, (this.getWidth() - size_3d - 1) / 2, yPos, true);
			}
			else this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);

			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		float zoom = getHandler().getZoomFactor();
		int size_3d = (int) (10 * zoom);
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
