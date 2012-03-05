package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Artefact extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		// symbol outline
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) (10 * zoom);
		int startY = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), startY + yPos, AlignHorizontal.LEFT);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		// small component symbol
		g2.drawLine(this.getSize().width - (int) (30 * zoom), (int) (10 * zoom), this.getSize().width - (int) (30 * zoom), (int) (40 * zoom));
		g2.drawLine(this.getSize().width - (int) (30 * zoom), (int) (40 * zoom), this.getSize().width - (int) (5 * zoom), (int) (40 * zoom));
		g2.drawLine(this.getSize().width - (int) (5 * zoom), (int) (40 * zoom), this.getSize().width - (int) (5 * zoom), (int) (20 * zoom));
		g2.drawLine(this.getSize().width - (int) (5 * zoom), (int) (20 * zoom), this.getSize().width - (int) (15 * zoom), (int) (10 * zoom));
		g2.drawLine(this.getSize().width - (int) (15 * zoom), (int) (10 * zoom), this.getSize().width - (int) (30 * zoom), (int) (10 * zoom));
		g2.drawLine(this.getSize().width - (int) (5 * zoom), (int) (20 * zoom), this.getSize().width - (int) (15 * zoom), (int) (20 * zoom));
		g2.drawLine(this.getSize().width - (int) (15 * zoom), (int) (20 * zoom), this.getSize().width - (int) (15 * zoom), (int) (10 * zoom));

	}
}
