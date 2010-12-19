// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class EER_Entity extends Entity {
	int ySave = 0;
	boolean hasAttributes = false;

	private Vector<String> getStringVector() {
		Vector<String> ret = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);

		Vector<String> tmp = getStringVector();
		boolean CENTER = true;
		boolean drawInnerRect = false;
		int yPos = (int) this.getHandler().getZoomedDistLineToText() * 2;

		// ### draw rectangles and lines (some duplicated code)
		Polygon poly = new Polygon();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				ySave = yPos + (int) this.getHandler().getZoomedDistLineToText() * 2;
				yPos += (int) this.getHandler().getZoomedDistLineToText() * 3;
			}
			else {
				yPos += (int) this.getHandler().getZoomedFontsize();
				if (CENTER && s.startsWith("##")) drawInnerRect = true;
				yPos += this.getHandler().getZoomedDistTextToText();
				if (CENTER) ySave = yPos;
			}
		}
		poly.addPoint(0, 0);
		poly.addPoint(this.getWidth() - 1, 0);
		if (CENTER) {
			hasAttributes = false; // see getStickingBorder()
			ySave = this.getHeight();
			poly.addPoint(this.getWidth() - 1, this.getHeight() - 1);
			poly.addPoint(0, this.getHeight() - 1);
		}
		else {
			hasAttributes = true; // see getStickingBorder()
			g.drawLine((int) (10 * zoom), ySave, (int) (10 * zoom), yPos + (int) this.getHandler().getZoomedDistTextToText() - (int) this.getHandler().getZoomedDistLineToText());
			poly.addPoint(this.getWidth() - 1, ySave);
			poly.addPoint(0, ySave);
		}

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);
		g2.drawPolygon(poly);

		if (drawInnerRect) {
			if (CENTER) g.drawRect((int) (3 * zoom), (int) (3 * zoom), this.getWidth() - (int) (7 * zoom), this.getHeight() - (int) (7 * zoom));
			else g.drawRect((int) (3 * zoom), (int) (3 * zoom), this.getWidth() - (int) (7 * zoom), ySave - (int) (6 * zoom));
		}

		// #### draw text
		CENTER = true;
		yPos = (int) this.getHandler().getZoomedDistLineToText() * 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				ySave = yPos + (int) this.getHandler().getZoomedDistLineToText() * 2;
				yPos += (int) this.getHandler().getZoomedDistLineToText() * 3;
			}
			else {
				yPos += (int) this.getHandler().getZoomedFontsize();
				if (CENTER) {
					String s1 = s;
					if (s.startsWith("##")) {
						drawInnerRect = true;
						s1 = s1.substring(2);
					}
					this.getHandler().writeText(g2, s1, this.getWidth() / 2, yPos, true);
				}
				else {
					this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize(), yPos, false);
				}
				yPos += this.getHandler().getZoomedDistTextToText();
				if (CENTER) ySave = yPos;
			}
		}
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + width, y + ySave - 1));
		if (!hasAttributes) p.addPoint(new Point(x, y + ySave - 1), true);
		else p.addLine(new Point(x, y + ySave - 1), new Point(x, y));
		return p;
	}
}
