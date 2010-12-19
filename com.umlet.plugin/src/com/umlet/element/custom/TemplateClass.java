// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class TemplateClass extends Entity {
	@Override
	public Entity CloneFromMe() {
		TemplateClass c = new TemplateClass();
		c.setState(this.getPanelAttributes());
		// c.setVisible(true);
		c.setBounds(this.getBounds());
		return c;
	}

	public TemplateClass() {
		super();
	}

	private Vector<String> getStringVector() {
		return Constants.decomposeStrings(this.getPanelAttributes(), "\n");
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(_activeColor);

		// this.getHandler().getFRC(g2);

		Vector<String> tmp = getStringVector();

		int yPos = 0;
		yPos += (int) this.getHandler().getZoomedDistLineToText();

		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				g2.drawLine(0, yPos, this.getWidth(), yPos);
				yPos += (int) this.getHandler().getZoomedDistLineToText();
			}
			else {
				yPos += (int) this.getHandler().getZoomedFontsize();
				if (CENTER) {
					this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				}
				else {
					this.getHandler().writeText(g2, s, (int) this.getHandler().getZoomedFontsize() / 2, yPos, false);
				}
				yPos += this.getHandler().getZoomedDistTextToText();
			}
		}

		Rectangle r = this.getBounds();
		g.drawRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
		/*
		 * if (_selected) {
		 * g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
		 * }
		 */
	}

}
