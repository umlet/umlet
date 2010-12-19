// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 29.07.2004
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class SendSignal extends Entity {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(this.getWidth() - (int) this.getHandler().getZoomedFontsize(), 0);
		poly.addPoint(this.getWidth() - 1, this.getHeight() / 2);
		poly.addPoint(this.getWidth() - (int) this.getHandler().getZoomedFontsize(), this.getHeight() - 1);
		poly.addPoint(0, this.getHeight() - 1);

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);
		g2.drawPolygon(poly);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getZoomedFontsize();
			this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getZoomedDistTextToText();
		}
		// g2.drawLine(0,0,this.getWidth()-this.getHandler().getFontsize(),0);
		// g2.drawLine(this.getWidth()-this.getHandler().getFontsize(), this.getHeight()-1, 0, this.getHeight()-1);
		// g2.drawLine(this.getWidth()-this.getHandler().getFontsize(),0,this.getWidth()-1,this.getHeight()/2);
		// g2.drawLine(this.getWidth(),this.getHeight()/2,this.getWidth()-this.getHandler().getFontsize(),this.getHeight());
		// g2.drawLine(0, this.getHeight()-1, 0, 0);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width - (int) this.getHandler().getZoomedFontsize(), y));
		p.addPoint(new Point(x + width, y + height / 2));
		p.addPoint(new Point(x + width - (int) this.getHandler().getZoomedFontsize(), y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}
}
