package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class TemplateClass extends GridElement {

	public TemplateClass() {
		super();
	}

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(this.getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		g2.setColor(fgColor);

		// Constants.getFRC(g2);

		Vector<String> tmp = getStringVector();

		int yPos = 0;
		yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				g2.drawLine(0, yPos, this.getWidth(), yPos);
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				if (CENTER) {
					this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				}
				else {
					this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				}
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
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
