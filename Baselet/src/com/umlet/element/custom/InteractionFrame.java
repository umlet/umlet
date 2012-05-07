package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class InteractionFrame extends GridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		float yPos = 0;
		yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		int textWidth = 0;
		// A.Mueller start
		boolean center = false;
		int topHeight = tmp.size();
		int maxWidthTemp = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			maxWidthTemp = Math.max(this.getHandler().getFontHandler().getTextWidth(s), maxWidthTemp);
			if (s.equals("--")) {
				textWidth = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + maxWidthTemp + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
				topHeight = i;
				yPos += (this.getHandler().getFontHandler().getDistanceBetweenTexts() + this.getHandler().getFontHandler().getDistanceBetweenTexts());
				center = true;
			}
			else if (s.equals("-.")) {
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, (int) yPos, this.getWidth(), (int) yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += this.getHandler().getFontHandler().getFontSize();
				if (center) this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, (int) yPos, true);
				else this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, (int) yPos,
						false);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}
		if (textWidth == 0) textWidth = maxWidthTemp;
		/*
		 * <OLDCODE>
		 * for (int i=0; i<tmp.size(); i++) {
		 * String s=tmp.elementAt(i);
		 * yPos+=this.getHandler().getFontHandler().getFontsize();
		 * this.getHandler().write(g2,s,this.getHandler().getFontHandler().getFontsize()/2, yPos, false);
		 * yPos+=this.getHandler().getDistTextToText();
		 * TextLayout l=new TextLayout(s, this.getHandler().getFont(), Constants.getFRC(g2));
		 * Rectangle2D r2d=l.getBounds();
		 * textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);
		 * }
		 * </OLDCODE>
		 */
		// A.Mueller end

		int w = (((int) this.getHandler().getFontHandler().getFontSize() * 7) > textWidth) ? ((int) this.getHandler().getFontHandler().getFontSize() * 7) : (textWidth);
		// A.Mueller start
		int h = topHeight * ((int) this.getHandler().getFontHandler().getFontSize() + (int) (3 * zoom)) + (int) this.getHandler().getFontHandler().getFontSize();
		int sw = w - (topHeight - 1) * (int) this.getHandler().getFontHandler().getFontSize();
		// <OLDCODE>
		// int h=tmp.size()*(this.getHandler().getFontHandler().getFontsize()+3)+this.getHandler().getFontHandler().getFontsize();
		// int sw=w-(tmp.size()-1)*this.getHandler().getFontHandler().getFontsize();
		// </OLDCODE>
		// A.Mueller end

		g2.drawLine(0, h, sw, h);
		g2.drawLine(w + (int) this.getHandler().getFontHandler().getFontSize(), 0, w + (int) this.getHandler().getFontHandler().getFontSize(), (int) this.getHandler().getFontHandler().getFontSize());
		g2.drawLine(sw, h, w + (int) this.getHandler().getFontHandler().getFontSize(), (int) this.getHandler().getFontHandler().getFontSize());
	}
}
