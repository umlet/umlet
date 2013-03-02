package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Signal extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		g2.setColor(Color.red);
		

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		yPos = this.getZoomedSize().height / 2 - (tmp.size() - 1) * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;

		int signalType = 0;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals(">")) signalType = 1; // send signal
			else if (s.equals("<")) signalType = 2; // accept signal
			else if (s.equals("x")) signalType = 3; // time signal
			else { // draw string
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, this.getZoomedSize().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}

		if (signalType == 1) { // send signal
			g2.drawLine(0, 0, this.getZoomedSize().width - (int) this.getHandler().getFontHandler().getFontSize(), 0);
			g2.drawLine(this.getZoomedSize().width - (int) this.getHandler().getFontHandler().getFontSize(), this.getZoomedSize().height - 1, 0, this.getZoomedSize().height - 1);
			g2.drawLine(this.getZoomedSize().width - (int) this.getHandler().getFontHandler().getFontSize(), 0, this.getZoomedSize().width - 1, this.getZoomedSize().height / 2);
			g2.drawLine(this.getZoomedSize().width, this.getZoomedSize().height / 2, this.getZoomedSize().width - (int) this.getHandler().getFontHandler().getFontSize(), this.getZoomedSize().height);
			g2.drawLine(0, this.getZoomedSize().height - 1, 0, 0);
		}
		else if (signalType == 2) { // accept signal
			g2.drawLine(0, 0, this.getZoomedSize().width, 0);
			g2.drawLine(this.getZoomedSize().width - 1, this.getZoomedSize().height - 1, 0, this.getZoomedSize().height - 1);
			g2.drawLine(0, 0, (int) this.getHandler().getFontHandler().getFontSize() - 2, this.getZoomedSize().height / 2);
			g2.drawLine((int) this.getHandler().getFontHandler().getFontSize() - 2, this.getZoomedSize().height / 2, 0, this.getZoomedSize().height);
			g2.drawLine(this.getZoomedSize().width - 1, this.getZoomedSize().height - 1, this.getZoomedSize().width - 1, 0);
		}
		else if (signalType == 3) { // time signal
			g2.drawLine(0, 0, this.getZoomedSize().width, 0);
			g2.drawLine(this.getZoomedSize().width - 1, this.getZoomedSize().height - 1, 0, this.getZoomedSize().height - 1);
			g2.drawLine(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
			g2.drawLine(this.getZoomedSize().width - 1, 0, 0, this.getZoomedSize().height - 1);
		}
		else { // NO signal specified
			g2.drawLine(0, 0, this.getZoomedSize().width, 0);
			g2.drawLine(this.getZoomedSize().width - 1, this.getZoomedSize().height - 1, 0, this.getZoomedSize().height - 1);
			g2.drawLine(this.getZoomedSize().width - 1, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
			g2.drawLine(0, this.getZoomedSize().height - 1, 0, 0);
		}

	}

	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret = 0;

		int tmpX = p.x - this.getLocation().x;
		int tmpY = p.y - this.getLocation().y;

		if ((tmpX > -4) && (tmpX < this.getZoomedSize().width + 4)) {
			if ((tmpY > -4) && (tmpY < 4)) ret += 1;
			if ((tmpY > this.getZoomedSize().height - 4) && (tmpY < this.getZoomedSize().height + 4)) ret += 4;
		}
		if ((tmpY > -4) && (tmpY < this.getZoomedSize().height + 4)) {
			if ((tmpX > -4) && (tmpX < 12)) ret += 8;
			if ((tmpX > this.getZoomedSize().width - 4) && (tmpX < this.getZoomedSize().width + 4)) ret += 2;
		}
		return ret;
	}
}
