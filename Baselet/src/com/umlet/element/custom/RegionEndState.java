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
		

		g2.drawOval(0, 0, this.getDimension().width - 1, this.getDimension().height - 1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform) at.clone();
		at2.rotate(Math.toRadians(45), getDimension().width / 2, getDimension().height / 2);
		g2.setTransform(at2);
		g2.drawLine(0, this.getDimension().height / 2, this.getDimension().width, this.getDimension().height / 2);
		g2.drawLine(this.getDimension().width / 2, 0, this.getDimension().width / 2, this.getDimension().height);
		g2.setTransform(at);

	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
