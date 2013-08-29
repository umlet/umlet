package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class RegionEndState extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.drawOval(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform) at.clone();
		at2.rotate(Math.toRadians(45), getRectangle().width / 2, getRectangle().height / 2);
		g2.setTransform(at2);
		g2.drawLine(0, this.getRectangle().height / 2, this.getRectangle().width, this.getRectangle().height / 2);
		g2.drawLine(this.getRectangle().width / 2, 0, this.getRectangle().width / 2, this.getRectangle().height);
		g2.setTransform(at);

	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
}
