// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
// G. Wirrer start
@SuppressWarnings("serial")
public class UseCase extends Entity {
	@Override
	public Entity CloneFromMe() {
		UseCase c = new UseCase();
		c.setState(this.getPanelAttributes());
		// c.setVisible(true);
		c.setBounds(this.getBounds());
		return c;
	}

	public UseCase() {
		super();
	}

	private Vector<String> getStringVector() {
		Vector<String> ret = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {
		int a = Math.max(1, (getWidth() - 1) / 2);
		int b = (getHeight() - 1) / 2;
		boolean found = false;
		int x = ((getWidth() - 1) / 9 * 4);
		int y = (int) Math.round((Math.sqrt(((a * a * b * b) - (b * b * x * x)) / (a * a))));
		int yPos = 0;
		int yPos1 = b;
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(_activeColor);

		this.getHandler().getFRC(g2);

		g2.setComposite(composites[1]);
		g2.setColor(_fillColor);
		g2.fillOval(0, 0, 2 * a, 2 * b);
		g2.setComposite(composites[0]);
		if (_selected) g2.setColor(_activeColor);
		else g2.setColor(_deselectedColor);

		Vector<String> tmp = new Vector<String>(getStringVector());
		if (tmp.contains("lt=.")) {
			tmp.remove("lt=.");
			g2.setStroke(Constants.getStroke(1, 1));
		}
		g2.drawOval(0, 0, 2 * a, 2 * b);

		if (tmp.contains("--")) {
			yPos = ((b - y) / 2);
			g2.drawLine(a - x, b - y, a + x, b - y);
			found = true;
		}
		else {
			yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;
		}

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if ((s.equals("--")) && found) {
				yPos = yPos1;
			}
			else if (found) {
				this.getHandler().writeText(g2, s, a, yPos + 5, true);
				yPos += 5 * this.getHandler().getZoomedDistTextToText();

			}
			else {
				yPos += (int) this.getHandler().getZoomedFontsize();
				this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				yPos += this.getHandler().getZoomedDistTextToText();
			}
		}
		g2.setStroke(Constants.getStroke(0, 1));
	}
}
// G. Wirrer end
