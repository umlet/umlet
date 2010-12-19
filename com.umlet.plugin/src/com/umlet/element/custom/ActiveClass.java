// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class ActiveClass extends Entity {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = 0;
		yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getZoomedFontsize();
			this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getZoomedDistTextToText();
		}

		g2.drawLine(0, 0, this.getWidth(), 0);
		g2.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.drawLine(this.getWidth() - 1, this.getHeight() - 1, 0, this.getHeight() - 1);
		g2.drawLine(0, this.getHeight() - 1, 0, 0);

		g2.drawLine((int) this.getHandler().getZoomedFontsize() / 2, 0, (int) this.getHandler().getZoomedFontsize() / 2, this.getHeight() - 1);
		g2.drawLine(this.getWidth() - (int) this.getHandler().getZoomedFontsize() / 2, 0, this.getWidth() - (int) this.getHandler().getZoomedFontsize() / 2, this.getHeight() - 1);
	}
}
