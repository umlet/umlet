package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.baselet.control.Main;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class RegionEndState extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.drawOval(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform) at.clone();
		at2.rotate(Math.toRadians(45), getZoomedSize().width / 2, getZoomedSize().height / 2);
		g2.setTransform(at2);
		g2.drawLine(0, this.getZoomedSize().height / 2, this.getZoomedSize().width, this.getZoomedSize().height / 2);
		g2.drawLine(this.getZoomedSize().width / 2, 0, this.getZoomedSize().width / 2, this.getZoomedSize().height);
		g2.setTransform(at);

	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
