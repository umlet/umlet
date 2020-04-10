package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class Systemborder extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

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
		int yPos = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		boolean center = false;
		boolean downleft = false;
		boolean upcenter = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.startsWith("center:") && !s.equals("center:")) {
				if (tmp.size() == 1) {
					yPos = (getRectangle().height - 1) / 2 - (int) (10 * zoom);
				}
				else {
					yPos = (getRectangle().height - 1) / 2 - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() * (tmp.size() / 2) - (int) (10 * zoom);
				}
				center = true;
				s = s.replace("center:", "");
			}
			else if (s.startsWith("bottomleft:") && !s.equals("bottomleft:")) {
				downleft = true;
				s = s.replace("bottomleft:", "");
				yPos = getRectangle().height - 1 - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			}
			else if (s.startsWith("topcenter:") && !s.equals("topcenter:")) {
				upcenter = true;
				s = s.replace("topcenter:", "");
				// yPos+=this.getHandler().getFontHandler().getFontsize();
			}
			if (center) {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - 1) / 2.0, yPos, AlignHorizontal.CENTER);
				yPos += 2 * HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else if (downleft) {
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2), yPos, AlignHorizontal.LEFT);
			}
			else if (upcenter) {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - 1) / 2.0, yPos, AlignHorizontal.CENTER);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2), yPos, AlignHorizontal.LEFT);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

	}
}
