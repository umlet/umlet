package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;

@SuppressWarnings("serial")
public class ThreeWayRelation extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		Polygon poly = new Polygon();
		poly.addPoint(getRectangle().width / 2, 0);
		poly.addPoint(getRectangle().width, getRectangle().height / 2);
		poly.addPoint(getRectangle().width / 2, getRectangle().height - 1);
		poly.addPoint(0, getRectangle().height / 2);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g2.drawPolygon(poly);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2), yPos, AlignHorizontal.LEFT);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon(0, 0);
		y += 1;
		width += 1;
		p.addPoint(x + width / 2, y);
		p.addPoint(x + width, y + height / 2);
		p.addPoint(x + width / 2, y + height);
		p.addPoint(x, y + height / 2, true);
		return p;
	}
}
