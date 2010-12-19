// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class Package extends Entity {

	Area lastKnown = new Area();

	@Override
	public Entity CloneFromMe() {
		Package c = new Package();
		c.setState(this.getPanelAttributes());
		// c.setVisible(true);
		c.setBounds(this.getBounds());
		return c;
	}

	public Package() {
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
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		Vector<String> tmp = new Vector<String>(getStringVector()); // in order to make the addition of "--" possible
		if (tmp.size() == 0) tmp.add(" ");
		if (!tmp.contains("--")) tmp.add("--");

		int yPos = (int) this.getHandler().getZoomedDistLineToText();
		boolean borders = false;
		// G. Mueller start
		boolean normal = false;
		// int maxUpperBox=5*this.getHandler().getFontsize();
		int maxUpperBox = (int) (this.getWidth() * 0.4); // I think this looks better
		int lines = 0;

		int yPosBorder = yPos;

		// LME: coloring (some code doubled)
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (tmp.elementAt(0).equals("--") && (borders == false)) yPosBorder = (int) this.getHandler().getZoomedDistLineToText() + (int) this.getHandler().getZoomedDistTextToText() + (int) this.getHandler().getZoomedFontsize(); // if there is no Packagename
			if (s.equals("--") && (borders == false)) {
				g2.setComposite(composites[1]);
				g2.setColor(_fillColor);
				g2.fillRect(0, 0, maxUpperBox, yPosBorder);
				g2.fillRect(0, yPosBorder, this.getWidth() - 1, this.getHeight() - yPosBorder - 1);
				g2.setComposite(composites[0]);
				if (_selected) g2.setColor(_activeColor);
				else g2.setColor(_deselectedColor);

				g2.drawRect(0, 0, maxUpperBox, yPosBorder);
				g2.drawRect(0, yPosBorder, this.getWidth() - 1, this.getHeight() - yPosBorder - 1);
				// yPos to write the String centered
				yPosBorder = (int) this.getHandler().getZoomedFontsize() / 2 + yPosBorder / 2 + this.getHeight() / 2 - (tmp.size() - lines) * ((int) (this.getHandler().getZoomedFontsize() + (int) this.getHandler().getZoomedDistTextToText())) / 2;
			}
			else if (!normal && (i > 1) && (tmp.elementAt(i - 1).equals("--") && tmp.elementAt(i).startsWith("left:"))) {
				yPosBorder = (lines + 1) * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2 + (int) this.getHandler().getZoomedDistTextToText() + (int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText());
			}
			else if (!borders) {
				maxUpperBox = Math.max(maxUpperBox, this.getHandler().getZoomedTextWidth(g2, s) + (int) this.getHandler().getZoomedFontsize());
				yPosBorder += (int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText());
			}
			else if (normal) {
				yPosBorder += (int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText());
			}
			else if (!normal) {
				yPosBorder += (int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText());
			}
		}

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (tmp.elementAt(0).equals("--") && (borders == false)) yPos = (int) this.getHandler().getZoomedDistLineToText() + (int) this.getHandler().getZoomedDistTextToText() + (int) this.getHandler().getZoomedFontsize(); // if there is no Packagename

			if (s.equals("--") && (borders == false)) {
				borders = true;
				// g2.drawRect(0,0,maxUpperBox,yPos);
				// g2.drawRect(0,yPos,this.getWidth()-1,this.getHeight()-yPos-1);
				// yPos to write the String centered
				yPos = (int) this.getHandler().getZoomedFontsize() / 2 + yPos / 2 + this.getHeight() / 2 - (tmp.size() - lines) * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;
			}
			else if (!normal && (i > 1) && (tmp.elementAt(i - 1).equals("--") && tmp.elementAt(i).startsWith("left:"))) {
				// writes the string normal
				yPos = (lines + 1) * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;
				yPos += this.getHandler().getZoomedDistTextToText();
				yPos += (int) this.getHandler().getZoomedFontsize();
				this.getHandler().writeText(g2, s.substring(5), (int) this.getHandler().getZoomedFontsize() / 2, yPos, false);
				yPos += this.getHandler().getZoomedDistTextToText();
				normal = true;
			}
			else if (!borders) {

				lines++;
				maxUpperBox = Math.max(maxUpperBox, this.getHandler().getZoomedTextWidth(g2, s) + (int) this.getHandler().getZoomedFontsize());
				yPos += (int) this.getHandler().getZoomedFontsize();
				this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize() / 2, yPos, false);
				yPos += this.getHandler().getZoomedDistTextToText();

			}
			else if (normal) {

				yPos += (int) this.getHandler().getZoomedFontsize();
				this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize() / 2, yPos, false);
				yPos += this.getHandler().getZoomedDistTextToText();

			}
			else if (!normal) {

				yPos += (int) this.getHandler().getZoomedFontsize();
				this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				yPos += this.getHandler().getZoomedDistTextToText();

			}
			// G. Mueller End
		}

		/*
		 * Rectangle r=this.getBounds();
		 * g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
		 * if (_selected) {
		 * g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
		 * }
		 */
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		Vector<String> tmp = new Vector<String>(getStringVector());
		if (tmp.size() == 0) tmp.add(" ");
		int yPos = (int) this.getHandler().getZoomedDistLineToText();
		boolean borders = false;
		// int maxUpperBox=5*this.getHandler().getFontsize();
		int maxUpperBox = (int) (width * 0.4);
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			// G. Mueller start
			if (tmp.elementAt(0).equals("--") && (borders == false)) yPos = (int) this.getHandler().getZoomedDistLineToText() + (int) this.getHandler().getZoomedDistTextToText() + (int) this.getHandler().getZoomedFontsize(); // if there is no Packagename
			// G.Mueller End
			if (s.equals("--") && (borders == false)) borders = true;
			else if (!borders) {
				maxUpperBox = Math.max(maxUpperBox, this.getHandler().getZoomedTextWidth((Graphics2D) this.getGraphics(), s) + (int) this.getHandler().getZoomedFontsize());
				yPos += (int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText());
			}
		}
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + maxUpperBox, y));
		p.addPoint(new Point(x + maxUpperBox, y + yPos));
		p.addPoint(new Point(x + width, y + yPos));
		p.addPoint(new Point(x + width, y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}
}
