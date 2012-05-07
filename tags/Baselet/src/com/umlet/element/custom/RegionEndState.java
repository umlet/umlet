package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class RegionEndState extends GridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.drawOval(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform) at.clone();
		at2.rotate(Math.toRadians(45), getWidth() / 2, getHeight() / 2);
		g2.setTransform(at2);
		g2.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
		g2.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
		g2.setTransform(at);

	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
