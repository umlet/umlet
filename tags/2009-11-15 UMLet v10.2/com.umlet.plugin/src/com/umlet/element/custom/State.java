// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class State extends Entity {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, (int) (30 * zoom), (int) (30 * zoom));
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, (int) (30 * zoom), (int) (30 * zoom));

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = 0;
		// A.Mueller start
		if (tmp.contains("--") || tmp.contains("-.")) yPos = 2 * (int) this.getHandler().getZoomedDistLineToText();
		else
		// A.Mueller end
		yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			// A.Mueller start
			if (s.equals("--")) {
				yPos += this.getHandler().getZoomedDistTextToLine();
				g2.drawLine(0, yPos, getWidth(), yPos);
				yPos += (int) this.getHandler().getZoomedDistLineToText();
			}
			else if (s.equals("-.")) {
				yPos += this.getHandler().getZoomedDistTextToLine();
				g2.setStroke(Constants.getStroke(1, 1));
				g2.drawLine(0, yPos, getWidth(), yPos);
				g2.setStroke(Constants.getStroke(0, 1));
				yPos += (int) this.getHandler().getZoomedDistLineToText();
			}
			else {
				// A.Mueller end
				yPos += (int) this.getHandler().getZoomedFontsize();
				this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				yPos += this.getHandler().getZoomedDistTextToText();
				// A.Mueller start
			}
			// A.Mueller end
		}

	}
}
