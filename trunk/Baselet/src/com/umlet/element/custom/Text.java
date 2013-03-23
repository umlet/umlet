package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Text extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getElementHandlerMapping().get(this).getFontHandler().getFont());
		colorize(g2);
		g2.setColor(fgColor);
		
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
			Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
		}
	}

}
