package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.diagram.command.Resize;
import com.baselet.element.OldGridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class Interface extends OldGridElement {
	public Interface() {
		super();
	}

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(this.getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		

		/*
		 * FontRenderContext rendering;
		 * if (Constants.getFontsize()>12) {
		 * rendering=new FontRenderContext(null, true, true);
		 * g2.setRenderingHints(Constants.UxRenderingQualityHigh());
		 * }
		 * else {
		 * rendering=new FontRenderContext(null, false, false);
		 * g2.setRenderingHints(Constants.UxRenderingQualityLow());
		 * }
		 */

		boolean ADAPT_SIZE = false;

		Vector<String> tmp = getStringVector();
		int yPos = 0;
		yPos += 2 * this.getHandler().getGridSize();
		yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(0, yPos, this.getSize().width, yPos);
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				TextLayout l = new TextLayout(s, this.getHandler().getFontHandler().getFont(), g2.getFontRenderContext());
				Rectangle2D r2d = l.getBounds();
				int width = (int) r2d.getWidth();
				int xPos = this.getSize().width / 2 - width / 2;
				if (xPos < 0) {
					ADAPT_SIZE = true;
					break;
				}
				this.getHandler().getFontHandler().writeText(g2, s, this.getSize().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -this.getHandler().getGridSize(), 0, 0, 0)).execute(this.getHandler());
			(new Resize(this, 0, 0, this.getHandler().getGridSize(), 0)).execute(this.getHandler());
			return;
		}
		if (yPos > this.getSize().height) {
			(new Resize(this, 0, 0, 0, 20)).execute(this.getHandler());
			return;
		}

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g.fillOval(this.getSize().width / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g.drawOval(this.getSize().width / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
		/*
		 * if (_selected) {
		 * g.drawOval(this.getWidth()/2-Constants.getFontsize()+1, 1, 2*Constants.getFontsize()-2, 2*Constants.getFontsize()-2);
		 * }
		 */
	}

	/*
	 * public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	 * int tmpX=p.x-this.getX();
	 * int tmpY=p.y-this.getY();
	 * int links=this.getWidth()/2-this.getHandler().getMainUnit();
	 * int rechts=this.getWidth()/2+this.getHandler().getMainUnit();
	 * int oben=0;
	 * int unten=2*this.getHandler().getMainUnit();
	 * if (tmpX>links-4 && tmpX<rechts+4) {
	 * if ((tmpY>oben-4 && tmpY<oben+4) || (tmpY>unten-4 && tmpY<unten+4)) return 15;
	 * }
	 * if (tmpY>oben-4 && tmpY<unten+4) {
	 * if ((tmpX>links-4 && tmpX<links+4) || (tmpX>rechts-4 && tmpX<rechts+4)) return 15;
	 * }
	 * return 0;
	 * }
	 */

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = getHandler().getZoomFactor();

		int links = x + width / 2 - (int) (10 * zoom);
		int rechts = x + width / 2 + (int) (10 * zoom);
		int oben = y;
		int unten = y + (int) (20 * zoom);
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(links, oben));
		p.addPoint(new Point(rechts, oben));
		p.addPoint(new Point(rechts, unten));
		p.addPoint(new Point(links, unten));
		p.addPoint(new Point(links, oben));
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
