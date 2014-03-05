package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.command.Resize;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;


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
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2);
		g2.setColor(fgColor);
		

		boolean ADAPT_SIZE = false;

		Vector<String> tmp = getStringVector();
		int yPos = 6 * (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(this.getRectangle().width / 2 - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 4, yPos, this.getRectangle().width / 2 + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 4, yPos);
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				TextLayout l = new TextLayout(s, Main.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
				Rectangle2D r2d = l.getBounds();
				int width = (int) r2d.getWidth();
				int xPos = this.getRectangle().width / 2 - width / 2;
				if (xPos < 0) {
					ADAPT_SIZE = true;
					break;
				}
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -Main.getHandlerForElement(this).getGridSize(), 0, 0, 0)).execute(Main.getHandlerForElement(this));
			(new Resize(this, 0, 0, Main.getHandlerForElement(this).getGridSize(), 0)).execute(Main.getHandlerForElement(this));
			return;
		}
		if (yPos > this.getRectangle().height) {
			(new Resize(this, 0, 0, 0, 20)).execute(Main.getHandlerForElement(this));
			return;
		}

		int startx = this.getRectangle().width / 2;

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillOval(startx - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawOval(startx - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(startx, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), startx, (int) ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 2.5));
		g2.drawLine(startx - 2 * (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 1.3), startx + 2 * (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 1.3));

		// Feet
		g2.drawLine(startx, (int) ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 2.5), startx - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 5);
		g2.drawLine(startx, (int) ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 2.5), startx + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 5);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		int links = Main.getHandlerForElement(this).realignToGrid(false, x + width / 2 - (int) (25 * zoom));
		int rechts = Main.getHandlerForElement(this).realignToGrid(false, x + width / 2 + (int) (35 * zoom));
		int oben = Main.getHandlerForElement(this).realignToGrid(false, y);
		int unten = Main.getHandlerForElement(this).realignToGrid(false, y + (int) (75 * zoom));
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(new Point(links, oben));
		p.addPoint(new Point(rechts, oben));
		p.addPoint(new Point(rechts, unten));
		p.addPoint(new Point(links, unten), true);
		return p;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}

}
