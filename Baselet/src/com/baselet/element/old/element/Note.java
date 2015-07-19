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

@SuppressWarnings("serial")
public class Note extends OldGridElement {

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		// g2.setColor(_activeColor);

		Composite[] composites = colorize(g2); // enable colors
		int yPos = 0;
		yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		Vector<String> tmp = getStringVector();

		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		poly.addPoint(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		poly.addPoint(getRectangle().width - 1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		poly.addPoint(getRectangle().width - 1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		poly.addPoint(getRectangle().width - 1, getRectangle().height - 1);
		poly.addPoint(getRectangle().width - 1, getRectangle().height - 1);
		poly.addPoint(0, getRectangle().height - 1);
		poly.addPoint(0, getRectangle().height - 1);
		poly.addPoint(0, 0);
		// p.addPoint(this.getWidth()-Constants.getFontsize(),0); p.addPoint(this.getWidth()-Constants.getFontsize(), Constants.getFontsize());
		// p.addPoint(this.getWidth()-Constants.getFontsize(),Constants.getFontsize()); p.addPoint(this.getWidth()-1, Constants.getFontsize());

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

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2.0, yPos, AlignHorizontal.LEFT);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		g2.drawLine(0, 0, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0, getRectangle().width - 1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(getRectangle().width - 1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), getRectangle().width - 1, getRectangle().height - 1);
		g2.drawLine(getRectangle().width - 1, getRectangle().height - 1, 0, getRectangle().height - 1);
		g2.drawLine(0, getRectangle().height - 1, 0, 0);
		g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), getRectangle().width - 1, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
	}
}
