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
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.command.Resize;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class TimeSignal extends Entity {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);
		boolean ADAPT_SIZE = false;

		int x0, y0, b, h;
		x0 = (int) (this.getWidth() / 2 - 20 * zoom);
		y0 = 0;
		b = (int) (this.getWidth() / 2 + 20 * zoom);
		h = (int) (40 * zoom);

		// g2.drawLine(x0,y0,b,y0);
		// g2.drawLine(b, h, x0, h);
		// g2.drawLine(x0,y0,b, h);
		// g2.drawLine(b,y0,x0, h);

		Polygon poly = new Polygon();
		poly.addPoint(x0, y0);
		poly.addPoint(b, y0);
		poly.addPoint(x0, h);
		poly.addPoint(b, h);

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawPolygon(poly);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = 0;
		yPos += 4 * this.getHandler().getGridSize();
		yPos += (int) this.getHandler().getZoomedDistLineToText();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += this.getHandler().getZoomedDistTextToLine();
				g2.drawLine(this.getWidth() / 2 - (int) this.getHandler().getZoomedFontsize() * 4, yPos, this.getWidth() / 2 + (int) this.getHandler().getZoomedFontsize() * 4, yPos);
				yPos += (int) this.getHandler().getZoomedDistLineToText();
			}
			else {
				yPos += (int) this.getHandler().getZoomedFontsize();
				TextLayout l = new TextLayout(s, this.getHandler().getZoomedFont(), this.getHandler().getFRC(g2));
				Rectangle2D r2d = l.getBounds();
				int width = (int) r2d.getWidth();
				int xPos = this.getWidth() / 2 - width / 2;
				if (xPos < 0) {
					ADAPT_SIZE = true;
					break;
				}
				this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				yPos += this.getHandler().getZoomedDistTextToText();
			}
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -this.getHandler().getGridSize(), 0, 0, 0)).execute(this.getHandler());
			(new Resize(this, 0, 0, this.getHandler().getGridSize(), 0)).execute(this.getHandler());
			return;
		}
		if (yPos > this.getHeight()) {
			(new Resize(this, 0, 0, 0, 20)).execute(this.getHandler());
			return;
		}

	}

	/*
	 * public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	 * int tmpX=p.x-this.getX()-this.getWidth()/2;
	 * int tmpY=p.y-this.getY()-(2*this.getHandler().getMainUnit()+20)/2;
	 * if ((tmpX>-4 && tmpX<+4)&&(tmpY>-4 && tmpY<+4)) {
	 * return 15;
	 * } else return 0;
	 * }
	 */
	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = getHandler().getZoomFactor();

		StickingPolygon p = new StickingPolygon();
		int px = x + width / 2;
		int py = (int) (y + (40 * zoom) / 2);
		p.addPoint(new Point(px - 4, py - 4));
		p.addPoint(new Point(px + 4, py - 4));
		p.addPoint(new Point(px + 4, py + 4));
		p.addPoint(new Point(px - 4, py + 4), true);
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
