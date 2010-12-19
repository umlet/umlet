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
public class Actor extends Entity {

	public Actor() {
		super();
	}

	private Vector<String> getStringVector() {
		Vector<String> ret = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2);
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		boolean ADAPT_SIZE = false;

		Vector<String> tmp = getStringVector();
		int yPos = 6 * (int) this.getHandler().getZoomedFontsize();

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

		int startx = this.getWidth() / 2;

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillOval(startx - (int) this.getHandler().getZoomedFontsize() / 2, 0, (int) this.getHandler().getZoomedFontsize(), (int) this.getHandler().getZoomedFontsize());
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawOval(startx - (int) this.getHandler().getZoomedFontsize() / 2, 0, (int) this.getHandler().getZoomedFontsize(), (int) this.getHandler().getZoomedFontsize());
		g2.drawLine(startx, (int) this.getHandler().getZoomedFontsize(), startx, (int) ((int) this.getHandler().getZoomedFontsize() * 2.5));
		g2.drawLine(startx - 2 * (int) this.getHandler().getZoomedFontsize(), (int) ((int) this.getHandler().getZoomedFontsize() * 1.3), startx + 2 * (int) this.getHandler().getZoomedFontsize(), (int) ((int) this.getHandler().getZoomedFontsize() * 1.3));

		// Feet
		g2.drawLine(startx, (int) ((int) this.getHandler().getZoomedFontsize() * 2.5), startx - (int) this.getHandler().getZoomedFontsize(), (int) this.getHandler().getZoomedFontsize() * 5);
		g2.drawLine(startx, (int) ((int) this.getHandler().getZoomedFontsize() * 2.5), startx + (int) this.getHandler().getZoomedFontsize(), (int) this.getHandler().getZoomedFontsize() * 5);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = getHandler().getZoomFactor();

		int links = x + width / 2 - (int) (10 * zoom);
		int rechts = x + width / 2 + (int) (10 * zoom);
		int oben = y;
		int unten = y + 4 * (int) (10 * zoom);
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(links, oben));
		p.addPoint(new Point(rechts, oben));
		p.addPoint(new Point(rechts, unten));
		p.addPoint(new Point(links, unten), true);
		return p;
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes

}
