// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.command.Resize;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class Interface extends Entity {
	@Override
	public Entity CloneFromMe() {
		Interface ret = new Interface();
		ret.setState(this.getPanelAttributes());

		ret.setBounds(this.getBounds());

		// int i = ret.getWidth() + this.getHandler().getGridSize() - 1;
		// i = i / this.getHandler().getGridSize();
		// ret.setBounds(new Rectangle(i, ret.getHeight()));

		return ret;
	}

	public Interface() {
		super();
	}

	private Vector<String> getStringVector() {
		return Constants.decomposeStrings(this.getPanelAttributes(), "\n");
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);

		this.getHandler().getFRC(g2);

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

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g.fillOval(this.getWidth() / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g.drawOval(this.getWidth() / 2 - (int) (10 * zoom), 0, (int) (20 * zoom), (int) (20 * zoom));
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
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
