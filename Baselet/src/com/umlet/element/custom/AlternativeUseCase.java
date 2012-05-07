package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Utils;


@SuppressWarnings("serial")
public class AlternativeUseCase extends com.baselet.element.GridElement {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		boolean center = false;

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			if (s.equals("--")) {
				yPos = (int) (35 * zoom);
				center = true;
			}
			else if (center == true) {
				this.getHandler().getFontHandler().writeText(g2, s, (getWidth() - 1) / 2, yPos, true);
				center = false;
			}
			else {
				this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}

		g2.drawLine(0, (int) (30 * zoom), getWidth() - 1, (int) (30 * zoom));
		g2.drawOval(getWidth() - (int) (59 * zoom), (int) (3 * zoom), (int) (55 * zoom), (int) (20 * zoom));
	}
}
