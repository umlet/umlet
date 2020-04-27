package com.baselet.element.old.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class ActiveClass extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(fgColor);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = 0;
		yPos = getRectangle().height / 2 - tmp.size() * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		g2.drawLine(0, 0, getRectangle().width, 0);
		g2.drawLine(getRectangle().width - 1, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.drawLine(getRectangle().width - 1, getRectangle().height - 1, 0, getRectangle().height - 1);
		g2.drawLine(0, getRectangle().height - 1, 0, 0);

		g2.drawLine((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, getRectangle().height - 1);
		g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, getRectangle().height - 1);
	}
}
