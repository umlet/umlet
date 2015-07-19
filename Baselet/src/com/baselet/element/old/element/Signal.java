package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class Signal extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(Color.red);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = 0;
		yPos = getRectangle().height / 2 - (tmp.size() - 1) * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;

		int signalType = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals(">")) {
				signalType = 1; // send signal
			}
			else if (s.equals("<")) {
				signalType = 2; // accept signal
			}
			else if (s.equals("x")) {
				signalType = 3; // time signal
			}
			else { // draw string
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (signalType == 1) { // send signal
			g2.drawLine(0, 0, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
			g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), getRectangle().height - 1, 0, getRectangle().height - 1);
			g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), 0, getRectangle().width - 1, getRectangle().height / 2);
			g2.drawLine(getRectangle().width, getRectangle().height / 2, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(), getRectangle().height);
			g2.drawLine(0, getRectangle().height - 1, 0, 0);
		}
		else if (signalType == 2) { // accept signal
			g2.drawLine(0, 0, getRectangle().width, 0);
			g2.drawLine(getRectangle().width - 1, getRectangle().height - 1, 0, getRectangle().height - 1);
			g2.drawLine(0, 0, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() - 2, getRectangle().height / 2);
			g2.drawLine((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() - 2, getRectangle().height / 2, 0, getRectangle().height);
			g2.drawLine(getRectangle().width - 1, getRectangle().height - 1, getRectangle().width - 1, 0);
		}
		else if (signalType == 3) { // time signal
			g2.drawLine(0, 0, getRectangle().width, 0);
			g2.drawLine(getRectangle().width - 1, getRectangle().height - 1, 0, getRectangle().height - 1);
			g2.drawLine(0, 0, getRectangle().width - 1, getRectangle().height - 1);
			g2.drawLine(getRectangle().width - 1, 0, 0, getRectangle().height - 1);
		}
		else { // NO signal specified
			g2.drawLine(0, 0, getRectangle().width, 0);
			g2.drawLine(getRectangle().width - 1, getRectangle().height - 1, 0, getRectangle().height - 1);
			g2.drawLine(getRectangle().width - 1, 0, getRectangle().width - 1, getRectangle().height - 1);
			g2.drawLine(0, getRectangle().height - 1, 0, 0);
		}

	}

	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret = 0;

		int tmpX = p.x - getRectangle().x;
		int tmpY = p.y - getRectangle().y;

		if (tmpX > -4 && tmpX < getRectangle().width + 4) {
			if (tmpY > -4 && tmpY < 4) {
				ret += 1;
			}
			if (tmpY > getRectangle().height - 4 && tmpY < getRectangle().height + 4) {
				ret += 4;
			}
		}
		if (tmpY > -4 && tmpY < getRectangle().height + 4) {
			if (tmpX > -4 && tmpX < 12) {
				ret += 8;
			}
			if (tmpX > getRectangle().width - 4 && tmpX < getRectangle().width + 4) {
				ret += 2;
			}
		}
		return ret;
	}
}
