package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.command.Resize;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.OldGridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class TimeSignal extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getElementHandlerMapping().get(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		
		boolean ADAPT_SIZE = false;

		int x0, y0, b, h;
		x0 = (int) (this.getZoomedSize().width / 2 - 20 * zoom);
		y0 = 0;
		b = (int) (this.getZoomedSize().width / 2 + 20 * zoom);
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
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawPolygon(poly);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		yPos += 4 * Main.getElementHandlerMapping().get(this).getGridSize();
		yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(this.getZoomedSize().width / 2 - (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() * 4, yPos, this.getZoomedSize().width / 2 + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() * 4, yPos);
				yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
				TextLayout l = new TextLayout(s, Main.getElementHandlerMapping().get(this).getFontHandler().getFont(), g2.getFontRenderContext());
				Rectangle2D r2d = l.getBounds();
				int width = (int) r2d.getWidth();
				int xPos = this.getZoomedSize().width / 2 - width / 2;
				if (xPos < 0) {
					ADAPT_SIZE = true;
					break;
				}
				Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, this.getZoomedSize().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -Main.getElementHandlerMapping().get(this).getGridSize(), 0, 0, 0)).execute(Main.getElementHandlerMapping().get(this));
			(new Resize(this, 0, 0, Main.getElementHandlerMapping().get(this).getGridSize(), 0)).execute(Main.getElementHandlerMapping().get(this));
			return;
		}
		if (yPos > this.getZoomedSize().height) {
			(new Resize(this, 0, 0, 0, 20)).execute(Main.getElementHandlerMapping().get(this));
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

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

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
