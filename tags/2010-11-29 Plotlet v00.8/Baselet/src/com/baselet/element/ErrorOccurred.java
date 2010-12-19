package com.baselet.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class ErrorOccurred extends GridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setColor(Color.red);
		g2.setFont(this.getHandler().getFontHandler().getFont());
		this.getHandler().getFontHandler().writeText(g2, "This Element has an Error!", this.getWidth() / 2, this.getHeight() / 2 - (int) (10 * zoom), true);
		g2.setColor(fgColor);
	}

}
