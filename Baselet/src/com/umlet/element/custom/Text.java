package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Text extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2);
		g2.setColor(fgColor);
		
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}
	}

}
