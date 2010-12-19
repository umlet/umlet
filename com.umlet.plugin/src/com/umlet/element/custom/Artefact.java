// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Artefact extends Entity {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		// symbol outline
		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = (int) (10 * zoom);
		int startY = (int) this.getHandler().getZoomedDistLineToText();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getZoomedFontsize();
			this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedDistLineToText(), startY + yPos, false);
			yPos += this.getHandler().getZoomedDistTextToText();
		}

		// small component symbol
		g2.drawLine(this.getWidth() - (int) (30 * zoom), (int) (10 * zoom), this.getWidth() - (int) (30 * zoom), (int) (40 * zoom));
		g2.drawLine(this.getWidth() - (int) (30 * zoom), (int) (40 * zoom), this.getWidth() - (int) (5 * zoom), (int) (40 * zoom));
		g2.drawLine(this.getWidth() - (int) (5 * zoom), (int) (40 * zoom), this.getWidth() - (int) (5 * zoom), (int) (20 * zoom));
		g2.drawLine(this.getWidth() - (int) (5 * zoom), (int) (20 * zoom), this.getWidth() - (int) (15 * zoom), (int) (10 * zoom));
		g2.drawLine(this.getWidth() - (int) (15 * zoom), (int) (10 * zoom), this.getWidth() - (int) (30 * zoom), (int) (10 * zoom));
		g2.drawLine(this.getWidth() - (int) (5 * zoom), (int) (20 * zoom), this.getWidth() - (int) (15 * zoom), (int) (20 * zoom));
		g2.drawLine(this.getWidth() - (int) (15 * zoom), (int) (20 * zoom), this.getWidth() - (int) (15 * zoom), (int) (10 * zoom));

	}
}
