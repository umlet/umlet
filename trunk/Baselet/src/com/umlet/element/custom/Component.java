package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Component extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		
		boolean normal = false; // G.M

		// symbol outline
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawRect(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		int startY = this.getZoomedSize().height / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (s.startsWith("'") || (normal == true)) { // G.M
				startY = (int) this.getHandler().getFontHandler().getFontSize();
				s = s.substring(1, s.length());
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, (int) (10 * zoom), startY + yPos, AlignHorizontal.LEFT);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				normal = true;
			}
			else {
				if (s.startsWith("*")) {
					startY = (int) this.getHandler().getFontHandler().getFontSize();
					s = s.substring(1, s.length());
				}
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, this.getZoomedSize().width / 2, startY + yPos, AlignHorizontal.CENTER);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}

		// small component symbol

		g2.drawLine(getZoomedSize().width - (int) (50 * zoom), (int) (10 * zoom), getZoomedSize().width - (int) (15 * zoom), (int) (10 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (50 * zoom), (int) (35 * zoom), getZoomedSize().width - (int) (15 * zoom), (int) (35 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (15 * zoom), (int) (10 * zoom), getZoomedSize().width - (int) (15 * zoom), (int) (35 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (50 * zoom), (int) (10 * zoom), getZoomedSize().width - (int) (50 * zoom), (int) (15 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (55 * zoom), (int) (15 * zoom), getZoomedSize().width - (int) (45 * zoom), (int) (15 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (55 * zoom), (int) (20 * zoom), getZoomedSize().width - (int) (45 * zoom), (int) (20 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (55 * zoom), (int) (15 * zoom), getZoomedSize().width - (int) (55 * zoom), (int) (20 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (45 * zoom), (int) (15 * zoom), getZoomedSize().width - (int) (45 * zoom), (int) (20 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (50 * zoom), (int) (20 * zoom), getZoomedSize().width - (int) (50 * zoom), (int) (25 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (55 * zoom), (int) (25 * zoom), getZoomedSize().width - (int) (45 * zoom), (int) (25 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (55 * zoom), (int) (30 * zoom), getZoomedSize().width - (int) (45 * zoom), (int) (30 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (55 * zoom), (int) (25 * zoom), getZoomedSize().width - (int) (55 * zoom), (int) (30 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (45 * zoom), (int) (25 * zoom), getZoomedSize().width - (int) (45 * zoom), (int) (30 * zoom));
		g2.drawLine(getZoomedSize().width - (int) (50 * zoom), (int) (30 * zoom), getZoomedSize().width - (int) (50 * zoom), (int) (35 * zoom));
	}

}
