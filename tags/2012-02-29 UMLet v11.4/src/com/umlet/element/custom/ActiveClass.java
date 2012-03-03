package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class ActiveClass extends GridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		g2.setColor(fgColor);
		

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		g2.drawLine(0, 0, this.getWidth(), 0);
		g2.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.drawLine(this.getWidth() - 1, this.getHeight() - 1, 0, this.getHeight() - 1);
		g2.drawLine(0, this.getHeight() - 1, 0, 0);

		g2.drawLine((int) this.getHandler().getFontHandler().getFontSize() / 2, 0, (int) this.getHandler().getFontHandler().getFontSize() / 2, this.getHeight() - 1);
		g2.drawLine(this.getWidth() - (int) this.getHandler().getFontHandler().getFontSize() / 2, 0, this.getWidth() - (int) this.getHandler().getFontHandler().getFontSize() / 2, this.getHeight() - 1);
	}
}
