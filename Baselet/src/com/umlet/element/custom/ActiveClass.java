package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class ActiveClass extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		g2.setColor(fgColor);
		

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		yPos = this.getSize().height / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, this.getSize().width / 2, yPos, AlignHorizontal.CENTER);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		g2.drawLine(0, 0, this.getSize().width, 0);
		g2.drawLine(this.getSize().width - 1, 0, this.getSize().width - 1, this.getSize().height - 1);
		g2.drawLine(this.getSize().width - 1, this.getSize().height - 1, 0, this.getSize().height - 1);
		g2.drawLine(0, this.getSize().height - 1, 0, 0);

		g2.drawLine((int) this.getHandler().getFontHandler().getFontSize() / 2, 0, (int) this.getHandler().getFontHandler().getFontSize() / 2, this.getSize().height - 1);
		g2.drawLine(this.getSize().width - (int) this.getHandler().getFontHandler().getFontSize() / 2, 0, this.getSize().width - (int) this.getHandler().getFontHandler().getFontSize() / 2, this.getSize().height - 1);
	}
}
