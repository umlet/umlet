package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import com.baselet.element.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
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

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getElementHandlerMapping().get(this).getFontHandler().getFont());
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
		yPos += 2 * Main.getElementHandlerMapping().get(this).getGridSize();
		yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(0, yPos, this.getZoomedSize().width, yPos);
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

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g.fillOval(this.getZoomedSize().width / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g.drawOval(this.getZoomedSize().width / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
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

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

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
