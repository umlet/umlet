package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.command.OldResize;

@SuppressWarnings("serial")
public class Interface extends OldGridElement {

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		/* FontRenderContext rendering; if (Constants.getFontsize()>12) { rendering=new FontRenderContext(null, true, true); g2.setRenderingHints(Constants.UxRenderingQualityHigh()); } else { rendering=new FontRenderContext(null, false, false); g2.setRenderingHints(Constants.UxRenderingQualityLow()); } */

		boolean ADAPT_SIZE = false;

		Vector<String> tmp = getStringVector();
		int yPos = 0;
		yPos += 2 * HandlerElementMap.getHandlerForElement(this).getGridSize();
		yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(0, yPos, getRectangle().width, yPos);
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				TextLayout l = new TextLayout(s, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
				Rectangle2D r2d = l.getBounds();
				int width = (int) r2d.getWidth();
				int xPos = getRectangle().width / 2 - width / 2;
				if (xPos < 0) {
					ADAPT_SIZE = true;
					break;
				}
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (ADAPT_SIZE) {
			new OldResize(this, -HandlerElementMap.getHandlerForElement(this).getGridSize(), 0, 0, 0).execute(HandlerElementMap.getHandlerForElement(this));
			new OldResize(this, 0, 0, HandlerElementMap.getHandlerForElement(this).getGridSize(), 0).execute(HandlerElementMap.getHandlerForElement(this));
			return;
		}
		if (yPos > getRectangle().height) {
			new OldResize(this, 0, 0, 0, 20).execute(HandlerElementMap.getHandlerForElement(this));
			return;
		}

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g.fillOval(getRectangle().width / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g.drawOval(getRectangle().width / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
		/* if (_selected) { g.drawOval(this.getWidth()/2-Constants.getFontsize()+1, 1, 2*Constants.getFontsize()-2, 2*Constants.getFontsize()-2); } */
	}

	/* public int doesCoordinateAppearToBeConnectedToMe(Point p) { int tmpX=p.x-this.getX(); int tmpY=p.y-this.getY(); int links=this.getWidth()/2-this.getHandler().getMainUnit(); int rechts=this.getWidth()/2+this.getHandler().getMainUnit(); int oben=0; int unten=2*this.getHandler().getMainUnit(); if (tmpX>links-4 && tmpX<rechts+4) { if ((tmpY>oben-4 && tmpY<oben+4) || (tmpY>unten-4 && tmpY<unten+4)) return 15; } if (tmpY>oben-4 && tmpY<unten+4) { if ((tmpX>links-4 && tmpX<links+4) || (tmpX>rechts-4 && tmpX<rechts+4)) return 15; } return 0; } */

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		int links = x + width / 2 - (int) (10 * zoom);
		int rechts = x + width / 2 + (int) (10 * zoom);
		int oben = y;
		int unten = y + (int) (20 * zoom);
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(links, oben);
		p.addPoint(rechts, oben);
		p.addPoint(rechts, unten);
		p.addPoint(links, unten);
		p.addPoint(links, oben);
		return p;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
}
