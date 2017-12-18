package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class EER_Entity extends OldGridElement {
	int ySave = 0;
	boolean hasAttributes = false;

	private Vector<String> getStringVector() {
		Vector<String> ret = Utils.decomposeStrings(getPanelAttributes());
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		Vector<String> tmp = getStringVector();
		boolean CENTER = true;
		boolean drawInnerRect = false;
		int yPos = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2;

		// ### draw rectangles and lines (some duplicated code)
		Polygon poly = new Polygon();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				ySave = yPos + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2;
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 3;
			}
			else {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				if (CENTER && s.startsWith("##")) {
					drawInnerRect = true;
				}
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				if (CENTER) {
					ySave = yPos;
				}
			}
		}
		poly.addPoint(0, 0);
		poly.addPoint(getRectangle().width - 1, 0);
		if (CENTER) {
			hasAttributes = false; // see getStickingBorder()
			ySave = getRectangle().height;
			poly.addPoint(getRectangle().width - 1, getRectangle().height - 1);
			poly.addPoint(0, getRectangle().height - 1);
		}
		else {
			hasAttributes = true; // see getStickingBorder()
			g.drawLine((int) (10 * zoom), ySave, (int) (10 * zoom), yPos + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());
			poly.addPoint(getRectangle().width - 1, ySave);
			poly.addPoint(0, ySave);
		}

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}
		g2.drawPolygon(poly);

		if (drawInnerRect) {
			if (CENTER) {
				g.drawRect((int) (3 * zoom), (int) (3 * zoom), getRectangle().width - (int) (7 * zoom), getRectangle().height - (int) (7 * zoom));
			}
			else {
				g.drawRect((int) (3 * zoom), (int) (3 * zoom), getRectangle().width - (int) (7 * zoom), ySave - (int) (6 * zoom));
			}
		}

		// #### draw text
		CENTER = true;
		yPos = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				ySave = yPos + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2;
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 3;
			}
			else {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				if (CENTER) {
					String s1 = s;
					if (s.startsWith("##")) {
						drawInnerRect = true;
						s1 = s1.substring(2);
					}
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s1, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
				}
				else {
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), yPos, AlignHorizontal.LEFT);
				}
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				if (CENTER) {
					ySave = yPos;
				}
			}
		}
	}

	// @Override
	// public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	// StickingPolygon p = new StickingPolygon(0, 0);
	// p.addPoint(new Point(x, y));
	// p.addPoint(new Point(x + width, y));
	// p.addPoint(new Point(x + width, y + ySave - 1));
	// if (!hasAttributes) p.addPoint(new Point(x, y + ySave - 1), true);
	// else p.addLine(new Point(x, y + ySave - 1), new Point(x, y));
	// return p;
	// }
}
