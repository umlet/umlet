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
public class SendSignal extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		poly.addPoint(getRectangle().width - 1, getRectangle().height / 2);
		poly.addPoint(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), getRectangle().height - 1);
		poly.addPoint(0, getRectangle().height - 1);

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
		int yPos = getRectangle().height / 2 - tmp.size() * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}
		// g2.drawLine(0,0,this.getWidth()-this.getHandler().getFontHandler().getFontsize(),0);
		// g2.drawLine(this.getWidth()-this.getHandler().getFontHandler().getFontsize(), this.getHeight()-1, 0, this.getHeight()-1);
		// g2.drawLine(this.getWidth()-this.getHandler().getFontHandler().getFontsize(),0,this.getWidth()-1,this.getHeight()/2);
		// g2.drawLine(this.getWidth(),this.getHeight()/2,this.getWidth()-this.getHandler().getFontHandler().getFontsize(),this.getHeight());
		// g2.drawLine(0, this.getHeight()-1, 0, 0);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(x, y);
		p.addPoint(x + width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), y);
		p.addPoint(x + width, y + height / 2);
		p.addPoint(x + width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), y + height);
		p.addPoint(x, y + height, true);
		return p;
	}
}
