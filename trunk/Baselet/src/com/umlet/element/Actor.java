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
public class Actor extends OldGridElement {

	public Actor() {
		super();
	}

	private Vector<String> getStringVector() {
		Vector<String> ret = Utils.decomposeStrings(this.getPanelAttributes());
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2);
		g2.setColor(fgColor);
		

		boolean ADAPT_SIZE = false;

		Vector<String> tmp = getStringVector();
		int yPos = 6 * (int) this.getHandler().getFontHandler().getFontSize();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(this.getSize().width / 2 - (int) this.getHandler().getFontHandler().getFontSize() * 4, yPos, this.getSize().width / 2 + (int) this.getHandler().getFontHandler().getFontSize() * 4, yPos);
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

		int startx = this.getSize().width / 2;

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillOval(startx - (int) this.getHandler().getFontHandler().getFontSize() / 2, 0, (int) this.getHandler().getFontHandler().getFontSize(), (int) this.getHandler().getFontHandler().getFontSize());
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawOval(startx - (int) this.getHandler().getFontHandler().getFontSize() / 2, 0, (int) this.getHandler().getFontHandler().getFontSize(), (int) this.getHandler().getFontHandler().getFontSize());
		g2.drawLine(startx, (int) this.getHandler().getFontHandler().getFontSize(), startx, (int) ((int) this.getHandler().getFontHandler().getFontSize() * 2.5));
		g2.drawLine(startx - 2 * (int) this.getHandler().getFontHandler().getFontSize(), (int) ((int) this.getHandler().getFontHandler().getFontSize() * 1.3), startx + 2 * (int) this.getHandler().getFontHandler().getFontSize(), (int) ((int) this.getHandler().getFontHandler().getFontSize() * 1.3));

		// Feet
		g2.drawLine(startx, (int) ((int) this.getHandler().getFontHandler().getFontSize() * 2.5), startx - (int) this.getHandler().getFontHandler().getFontSize(), (int) this.getHandler().getFontHandler().getFontSize() * 5);
		g2.drawLine(startx, (int) ((int) this.getHandler().getFontHandler().getFontSize() * 2.5), startx + (int) this.getHandler().getFontHandler().getFontSize(), (int) this.getHandler().getFontHandler().getFontSize() * 5);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = getHandler().getZoomFactor();

		int links = getHandler().realignToGrid(false, x + width / 2 - (int) (25 * zoom));
		int rechts = getHandler().realignToGrid(false, x + width / 2 + (int) (35 * zoom));
		int oben = getHandler().realignToGrid(false, y);
		int unten = getHandler().realignToGrid(false, y + (int) (75 * zoom));
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
