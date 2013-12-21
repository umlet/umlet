package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class ActiveClass extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(fgColor);
		

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		yPos = this.getRectangle().height / 2 - tmp.size() * ((int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		g2.drawLine(0, 0, this.getRectangle().width, 0);
		g2.drawLine(this.getRectangle().width - 1, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
		g2.drawLine(this.getRectangle().width - 1, this.getRectangle().height - 1, 0, this.getRectangle().height - 1);
		g2.drawLine(0, this.getRectangle().height - 1, 0, 0);

		g2.drawLine((int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, this.getRectangle().height - 1);
		g2.drawLine(this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, this.getRectangle().width - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, this.getRectangle().height - 1);
	}
}
