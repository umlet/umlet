package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class InteractionFrame extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		float yPos = 0;
		yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}
		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());

		int textWidth = 0;
		// A.Mueller start
		boolean center = false;
		int topHeight = tmp.size();
		int maxWidthTemp = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			maxWidthTemp = (int) Math.max(HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(s), maxWidthTemp);
			if (s.equals("--")) {
				textWidth = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + maxWidthTemp + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				topHeight = i;
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				center = true;
			}
			else if (s.equals("-.")) {
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, (int) yPos, getRectangle().width, (int) yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				if (center) {
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, (int) yPos, AlignHorizontal.CENTER);
				}
				else {
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2), (int) yPos, AlignHorizontal.LEFT);
				}
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}
		if (textWidth == 0) {
			textWidth = maxWidthTemp;
			/* <OLDCODE> for (int i=0; i<tmp.size(); i++) { String s=tmp.elementAt(i); yPos+=this.getHandler().getFontHandler().getFontsize(); this.getHandler().write(g2,s,this.getHandler().getFontHandler().getFontsize()/2, yPos, false); yPos+=this.getHandler().getDistTextToText(); TextLayout l=new TextLayout(s, this.getHandler().getFont(), Constants.getFRC(g2)); Rectangle2D r2d=l.getBounds(); textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth); } </OLDCODE> */
			// A.Mueller end
		}

		int w = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 7 > textWidth ? (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * 7 : textWidth;
		// A.Mueller start
		int h = topHeight * ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) (3 * zoom)) + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
		int sw = w - (topHeight - 1) * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
		// <OLDCODE>
		// int h=tmp.size()*(this.getHandler().getFontHandler().getFontsize()+3)+this.getHandler().getFontHandler().getFontsize();
		// int sw=w-(tmp.size()-1)*this.getHandler().getFontHandler().getFontsize();
		// </OLDCODE>
		// A.Mueller end

		g2.drawLine(0, h, sw, h);
		g2.drawLine(w + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0, w + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
		g2.drawLine(sw, h, w + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize());
	}
}
