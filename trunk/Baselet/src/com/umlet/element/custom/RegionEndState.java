package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class RegionEndState extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.drawOval(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform) at.clone();
		at2.rotate(Math.toRadians(45), getSize().width / 2, getSize().height / 2);
		g2.setTransform(at2);
		g2.drawLine(0, this.getSize().height / 2, this.getSize().width, this.getSize().height / 2);
		g2.drawLine(this.getSize().width / 2, 0, this.getSize().width / 2, this.getSize().height);
		g2.setTransform(at);

	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
