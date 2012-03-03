package com.umlet.element.custom;

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
public class SendSignal extends GridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(this.getWidth() - (int) this.getHandler().getFontHandler().getFontSize(), 0);
		poly.addPoint(this.getWidth() - 1, this.getHeight() / 2);
		poly.addPoint(this.getWidth() - (int) this.getHandler().getFontHandler().getFontSize(), this.getHeight() - 1);
		poly.addPoint(0, this.getHeight() - 1);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawPolygon(poly);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}
		// g2.drawLine(0,0,this.getWidth()-this.getHandler().getFontHandler().getFontsize(),0);
		// g2.drawLine(this.getWidth()-this.getHandler().getFontHandler().getFontsize(), this.getHeight()-1, 0, this.getHeight()-1);
		// g2.drawLine(this.getWidth()-this.getHandler().getFontHandler().getFontsize(),0,this.getWidth()-1,this.getHeight()/2);
		// g2.drawLine(this.getWidth(),this.getHeight()/2,this.getWidth()-this.getHandler().getFontHandler().getFontsize(),this.getHeight());
		// g2.drawLine(0, this.getHeight()-1, 0, 0);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width - (int) this.getHandler().getFontHandler().getFontSize(), y));
		p.addPoint(new Point(x + width, y + height / 2));
		p.addPoint(new Point(x + width - (int) this.getHandler().getFontHandler().getFontSize(), y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}
}
