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
public class Actor extends OldGridElement {

	private Vector<String> getStringVector() {
		Vector<String> ret = Utils.decomposeStrings(getPanelAttributes());
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2);
		g2.setColor(fgColor);

		boolean ADAPT_SIZE = false;

		Vector<String> tmp = getStringVector();
		int yPos = 6 * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(getRectangle().width / 2 - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 4, yPos, getRectangle().width / 2 + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 4, yPos);
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
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width * 0.5, yPos, AlignHorizontal.CENTER);
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

		int startx = getRectangle().width / 2;

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillOval(startx - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g2.drawOval(startx - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(startx, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), startx, (int) ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2.5));
		g2.drawLine(startx - 2 * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 1.3), startx + 2 * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 1.3));

		// Feet
		g2.drawLine(startx, (int) ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2.5), startx - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 5);
		g2.drawLine(startx, (int) ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 2.5), startx + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 5);
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		int links = HandlerElementMap.getHandlerForElement(this).realignToGrid(false, x + width / 2 - (int) (25 * zoom));
		int rechts = HandlerElementMap.getHandlerForElement(this).realignToGrid(false, x + width / 2 + (int) (35 * zoom));
		int oben = HandlerElementMap.getHandlerForElement(this).realignToGrid(false, y);
		int unten = HandlerElementMap.getHandlerForElement(this).realignToGrid(false, y + (int) (75 * zoom));
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(links, oben);
		p.addPoint(rechts, oben);
		p.addPoint(rechts, unten);
		p.addPoint(links, unten, true);
		return p;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}

}
