package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.OldGridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class ThreeWayRelation extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		Polygon poly = new Polygon();
		poly.addPoint(this.getZoomedSize().width / 2, 0);
		poly.addPoint(this.getZoomedSize().width, this.getZoomedSize().height / 2);
		poly.addPoint(this.getZoomedSize().width / 2, this.getZoomedSize().height - 1);
		poly.addPoint(0, this.getZoomedSize().height / 2);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawPolygon(poly);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		y += 1;
		width += 1;
		p.addPoint(new Point(x + width / 2, y));
		p.addPoint(new Point(x + width, y + height / 2));
		p.addPoint(new Point(x + width / 2, y + height));
		p.addPoint(new Point(x, y + height / 2), true);
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}
}
