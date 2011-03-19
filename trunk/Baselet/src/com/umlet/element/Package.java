package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.util.Vector;

import com.baselet.control.Utils;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class Package extends GridElement {

	Area lastKnown = new Area();

	public Package() {
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
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		Vector<String> tmp = new Vector<String>(getStringVector()); // in order to make the addition of "--" possible
		if (tmp.size() == 0) tmp.add(" ");
		if (!tmp.contains("--")) tmp.add("--");

		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		boolean borders = false;
		// G. Mueller start
		boolean normal = false;
		// int maxUpperBox=5*this.getHandler().getFontHandler().getFontsize();
		int maxUpperBox = (int) (this.getWidth() * 0.4); // I think this looks better
		int lines = 0;

		int yPosBorder = yPos;

		// LME: coloring (some code doubled)
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (tmp.elementAt(0).equals("--") && (borders == false)) yPosBorder = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getFontSize(); // if there is no Packagename
			if (s.equals("--") && (borders == false)) {
				g2.setComposite(composites[1]);
				g2.setColor(bgColor);
				g2.fillRect(0, 0, maxUpperBox, yPosBorder);
				g2.fillRect(0, yPosBorder, this.getWidth() - 1, this.getHeight() - yPosBorder - 1);
				g2.setComposite(composites[0]);
				if (isSelected) g2.setColor(fgColor);
				else g2.setColor(fgColorBase);

				g2.drawRect(0, 0, maxUpperBox, yPosBorder);
				g2.drawRect(0, yPosBorder, this.getWidth() - 1, this.getHeight() - yPosBorder - 1);
				// yPos to write the String centered
				yPosBorder = (int) this.getHandler().getFontHandler().getFontSize() / 2 + yPosBorder / 2 + this.getHeight() / 2 - (tmp.size() - lines) * ((int) (this.getHandler().getFontHandler().getFontSize() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;
			}
			else if (!normal && (i > 1) && (tmp.elementAt(i - 1).equals("--") && tmp.elementAt(i).startsWith("left:"))) {
				yPosBorder = (lines + 1) * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2 + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts());
			}
			else if (!borders) {
				maxUpperBox = Math.max(maxUpperBox, this.getHandler().getFontHandler().getTextWidth(s) + (int) this.getHandler().getFontHandler().getFontSize());
				yPosBorder += (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts());
			}
			else if (normal) {
				yPosBorder += (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts());
			}
			else if (!normal) {
				yPosBorder += (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts());
			}
		}

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (tmp.elementAt(0).equals("--") && (borders == false)) yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getFontSize(); // if there is no Packagename

			if (s.equals("--") && (borders == false)) {
				borders = true;
				// g2.drawRect(0,0,maxUpperBox,yPos);
				// g2.drawRect(0,yPos,this.getWidth()-1,this.getHeight()-yPos-1);
				// yPos to write the String centered
				yPos = (int) this.getHandler().getFontHandler().getFontSize() / 2 + yPos / 2 + this.getHeight() / 2 - (tmp.size() - lines) * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;
			}
			else if (!normal && (i > 1) && (tmp.elementAt(i - 1).equals("--") && tmp.elementAt(i).startsWith("left:"))) {
				// writes the string normal
				yPos = (lines + 1) * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s.substring(5), (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				normal = true;
			}
			else if (!borders) {

				lines++;
				maxUpperBox = Math.max(maxUpperBox, this.getHandler().getFontHandler().getTextWidth(s) + (int) this.getHandler().getFontHandler().getFontSize());
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();

			}
			else if (normal) {

				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();

			}
			else if (!normal) {

				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();

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
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		boolean borders = false;
		// int maxUpperBox=5*this.getHandler().getFontHandler().getFontsize();
		int maxUpperBox = (int) (width * 0.4);
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			// G. Mueller start
			if (tmp.elementAt(0).equals("--") && (borders == false)) yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getFontSize(); // if there is no Packagename
			// G.Mueller End
			if (s.equals("--") && (borders == false)) borders = true;
			else if (!borders) {
				maxUpperBox = Math.max(maxUpperBox, this.getHandler().getFontHandler().getTextWidth(s) + (int) this.getHandler().getFontHandler().getFontSize());
				yPos += (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts());
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
