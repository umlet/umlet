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

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		float yPos = 0;
		yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		int textWidth = 0;
		// A.Mueller start
		boolean center = false;
		int topHeight = tmp.size();
		int maxWidthTemp = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			maxWidthTemp = (int) Math.max(Main.getHandlerForElement(this).getFontHandler().getTextWidth(s), maxWidthTemp);
			if (s.equals("--")) {
				textWidth = (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + maxWidthTemp + (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				topHeight = i;
				yPos += (Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());
				center = true;
			}
			else if (s.equals("-.")) {
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, (int) yPos, this.getRectangle().width, (int) yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += Main.getHandlerForElement(this).getFontHandler().getFontSize();
				if (center) Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, (int) yPos, AlignHorizontal.CENTER);
				else
					Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, (int) yPos, AlignHorizontal.LEFT);
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
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

		int w = (((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 7) > textWidth) ? ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 7) : (textWidth);
		// A.Mueller start
		int h = topHeight * ((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() + (int) (3 * zoom)) + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
		int sw = w - (topHeight - 1) * (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
		// <OLDCODE>
		// int h=tmp.size()*(this.getHandler().getFontHandler().getFontsize()+3)+this.getHandler().getFontHandler().getFontsize();
		// int sw=w-(tmp.size()-1)*this.getHandler().getFontHandler().getFontsize();
		// </OLDCODE>
		// A.Mueller end

		g2.drawLine(0, h, sw, h);
		g2.drawLine(w + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0, w + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(sw, h, w + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), (int) Main.getHandlerForElement(this).getFontHandler().getFontSize());
	}
}
