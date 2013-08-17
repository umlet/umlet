package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Signal extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(Color.red);
		

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		yPos = this.getRectangle().height / 2 - (tmp.size() - 1) * ((int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts())) / 2;

		int signalType = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals(">")) signalType = 1; // send signal
			else if (s.equals("<")) signalType = 2; // accept signal
			else if (s.equals("x")) signalType = 3; // time signal
			else { // draw string
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (signalType == 1) { // send signal
			g2.drawLine(0, 0, this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0);
			g2.drawLine(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), this.getRectangle().height - 1, 0, this.getRectangle().height - 1);
			g2.drawLine(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0, this.getRectangle().width - 1, this.getRectangle().height / 2);
			g2.drawLine(this.getRectangle().width, this.getRectangle().height / 2, this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), this.getRectangle().height);
			g2.drawLine(0, this.getRectangle().height - 1, 0, 0);
		}
		else if (signalType == 2) { // accept signal
			g2.drawLine(0, 0, this.getRectangle().width, 0);
			g2.drawLine(this.getRectangle().width - 1, this.getRectangle().height - 1, 0, this.getRectangle().height - 1);
			g2.drawLine(0, 0, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() - 2, this.getRectangle().height / 2);
			g2.drawLine((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() - 2, this.getRectangle().height / 2, 0, this.getRectangle().height);
			g2.drawLine(this.getRectangle().width - 1, this.getRectangle().height - 1, this.getRectangle().width - 1, 0);
		}
		else if (signalType == 3) { // time signal
			g2.drawLine(0, 0, this.getRectangle().width, 0);
			g2.drawLine(this.getRectangle().width - 1, this.getRectangle().height - 1, 0, this.getRectangle().height - 1);
			g2.drawLine(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
			g2.drawLine(this.getRectangle().width - 1, 0, 0, this.getRectangle().height - 1);
		}
		else { // NO signal specified
			g2.drawLine(0, 0, this.getRectangle().width, 0);
			g2.drawLine(this.getRectangle().width - 1, this.getRectangle().height - 1, 0, this.getRectangle().height - 1);
			g2.drawLine(this.getRectangle().width - 1, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
			g2.drawLine(0, this.getRectangle().height - 1, 0, 0);
		}

	}

	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret = 0;

		int tmpX = p.x - this.getRectangle().x;
		int tmpY = p.y - this.getRectangle().y;

		if ((tmpX > -4) && (tmpX < this.getRectangle().width + 4)) {
			if ((tmpY > -4) && (tmpY < 4)) ret += 1;
			if ((tmpY > this.getRectangle().height - 4) && (tmpY < this.getRectangle().height + 4)) ret += 4;
		}
		if ((tmpY > -4) && (tmpY < this.getRectangle().height + 4)) {
			if ((tmpX > -4) && (tmpX < 12)) ret += 8;
			if ((tmpX > this.getRectangle().width - 4) && (tmpX < this.getRectangle().width + 4)) ret += 2;
		}
		return ret;
	}
}
