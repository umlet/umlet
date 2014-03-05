package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Note extends OldGridElement {

	public Note() {
		super();
	}

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(this.getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		// g2.setColor(_activeColor);
		
		Composite[] composites = colorize(g2); // enable colors
		int yPos = 0;
		yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		Vector<String> tmp = this.getStringVector();

		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		poly.addPoint(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		poly.addPoint(this.getRectangle().width - 1, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		poly.addPoint(this.getRectangle().width - 1, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		poly.addPoint(this.getRectangle().width - 1, this.getRectangle().height - 1);
		poly.addPoint(this.getRectangle().width - 1, this.getRectangle().height - 1);
		poly.addPoint(0, this.getRectangle().height - 1);
		poly.addPoint(0, this.getRectangle().height - 1);
		poly.addPoint(0, 0);
		// p.addPoint(this.getWidth()-Constants.getFontsize(),0); p.addPoint(this.getWidth()-Constants.getFontsize(), Constants.getFontsize());
		// p.addPoint(this.getWidth()-Constants.getFontsize(),Constants.getFontsize()); p.addPoint(this.getWidth()-1, Constants.getFontsize());

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		g2.drawLine(0, 0, this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
		g2.drawLine(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0, this.getRectangle().width - 1, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(this.getRectangle().width - 1, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), this.getRectangle().width - 1, this.getRectangle().height - 1);
		g2.drawLine(this.getRectangle().width - 1, this.getRectangle().height - 1, 0, this.getRectangle().height - 1);
		g2.drawLine(0, this.getRectangle().height - 1, 0, 0);
		g2.drawLine(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0, this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), this.getRectangle().width - 1, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
	}
}
