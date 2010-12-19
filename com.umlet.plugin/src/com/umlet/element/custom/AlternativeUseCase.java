// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class AlternativeUseCase extends com.umlet.element.base.Entity {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
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
		g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		boolean center = false;

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = (int) this.getHandler().getZoomedDistLineToText();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getZoomedFontsize();
			if (s.equals("--")) {
				yPos = (int) (35 * zoom);
				center = true;
			}
			else if (center == true) {
				this.getHandler().writeText(g2, s, (getWidth() - 1) / 2, yPos, true);
				center = false;
			}
			else {
				this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize() / 2, yPos, false);
				yPos += this.getHandler().getZoomedDistTextToText();
			}
		}

		g2.drawLine(0, (int) (30 * zoom), getWidth() - 1, (int) (30 * zoom));
		g2.drawOval(getWidth() - (int) (59 * zoom), (int) (3 * zoom), (int) (55 * zoom), (int) (20 * zoom));
	}
}
