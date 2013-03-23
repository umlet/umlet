package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class InteractionFrame extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getElementHandlerMapping().get(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		float yPos = 0;
		yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		int textWidth = 0;
		// A.Mueller start
		boolean center = false;
		int topHeight = tmp.size();
		int maxWidthTemp = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			maxWidthTemp = (int) Math.max(Main.getElementHandlerMapping().get(this).getFontHandler().getTextWidth(s), maxWidthTemp);
			if (s.equals("--")) {
				textWidth = (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts() + maxWidthTemp + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
				topHeight = i;
				yPos += (Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts() + Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts());
				center = true;
			}
			else if (s.equals("-.")) {
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, (int) yPos, this.getZoomedSize().width, (int) yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
				if (center) Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, this.getZoomedSize().width / 2, (int) yPos, AlignHorizontal.CENTER);
				else
					Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() / 2, (int) yPos, AlignHorizontal.LEFT);
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
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

		int w = (((int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() * 7) > textWidth) ? ((int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() * 7) : (textWidth);
		// A.Mueller start
		int h = topHeight * ((int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() + (int) (3 * zoom)) + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
		int sw = w - (topHeight - 1) * (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
		// <OLDCODE>
		// int h=tmp.size()*(this.getHandler().getFontHandler().getFontsize()+3)+this.getHandler().getFontHandler().getFontsize();
		// int sw=w-(tmp.size()-1)*this.getHandler().getFontHandler().getFontsize();
		// </OLDCODE>
		// A.Mueller end

		g2.drawLine(0, h, sw, h);
		g2.drawLine(w + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize(), 0, w + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize(), (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize());
		g2.drawLine(sw, h, w + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize(), (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize());
	}
}
