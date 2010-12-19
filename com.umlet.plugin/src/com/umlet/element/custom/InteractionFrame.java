// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class InteractionFrame extends Entity {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		int yPos = 0;
		yPos += (int) this.getHandler().getZoomedDistLineToText();

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		int textWidth = 0;
		// A.Mueller start
		boolean center = false;
		int topHeight = tmp.size();
		int maxWidthTemp = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			maxWidthTemp = Math.max(this.getHandler().getZoomedTextWidth(g2, s), maxWidthTemp);
			if (s.equals("--")) {
				textWidth = (int) this.getHandler().getZoomedDistLineToText() + maxWidthTemp + (int) this.getHandler().getZoomedDistTextToLine();
				topHeight = i;
				yPos += (this.getHandler().getZoomedDistTextToLine() + (int) this.getHandler().getZoomedDistLineToText());
				center = true;
			}
			else if (s.equals("-.")) {
				yPos += this.getHandler().getZoomedDistTextToLine();
				g2.setStroke(Constants.getStroke(1, 1));
				g2.drawLine(0, yPos, this.getWidth(), yPos);
				g2.setStroke(Constants.getStroke(0, 1));
				yPos += (int) this.getHandler().getZoomedDistLineToText();
			}
			else {
				yPos += (int) this.getHandler().getZoomedFontsize();
				if (center) this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				else this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize() / 2, yPos,
						false);
				yPos += this.getHandler().getZoomedDistTextToText();
			}
		}
		if (textWidth == 0) textWidth = maxWidthTemp;
		/*
		 * <OLDCODE>
		 * for (int i=0; i<tmp.size(); i++) {
		 * String s=tmp.elementAt(i);
		 * yPos+=this.getHandler().getFontsize();
		 * this.getHandler().write(g2,s,this.getHandler().getFontsize()/2, yPos, false);
		 * yPos+=this.getHandler().getDistTextToText();
		 * TextLayout l=new TextLayout(s, this.getHandler().getFont(), this.getHandler().getFRC(g2));
		 * Rectangle2D r2d=l.getBounds();
		 * textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);
		 * }
		 * </OLDCODE>
		 */
		// A.Mueller end

		int w = (((int) this.getHandler().getZoomedFontsize() * 7) > textWidth) ? ((int) this.getHandler().getZoomedFontsize() * 7) : (textWidth);
		// A.Mueller start
		int h = topHeight * ((int) this.getHandler().getZoomedFontsize() + (int) (3 * zoom)) + (int) this.getHandler().getZoomedFontsize();
		int sw = w - (topHeight - 1) * (int) this.getHandler().getZoomedFontsize();
		// <OLDCODE>
		// int h=tmp.size()*(this.getHandler().getFontsize()+3)+this.getHandler().getFontsize();
		// int sw=w-(tmp.size()-1)*this.getHandler().getFontsize();
		// </OLDCODE>
		// A.Mueller end

		g2.drawLine(0, h, sw, h);
		g2.drawLine(w + (int) this.getHandler().getZoomedFontsize(), 0, w + (int) this.getHandler().getZoomedFontsize(), (int) this.getHandler().getZoomedFontsize());
		g2.drawLine(sw, h, w + (int) this.getHandler().getZoomedFontsize(), (int) this.getHandler().getZoomedFontsize());
	}
}
