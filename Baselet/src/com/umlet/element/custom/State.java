package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class State extends GridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, (int) (30 * zoom), (int) (30 * zoom));
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, (int) (30 * zoom), (int) (30 * zoom));

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		// A.Mueller start
		if (tmp.contains("--") || tmp.contains("-.")) yPos = 2 * (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		else
		// A.Mueller end
		yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;

		boolean CENTER = true;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			// A.Mueller start
			if (s.equals("--")) {
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(0, yPos, getWidth(), yPos);
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
				CENTER = false;
			}
			else if (s.equals("-.")) {
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, yPos, getWidth(), yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
				CENTER = false;
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				if (CENTER) this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				else this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			// A.Mueller end
		}

	}
}
