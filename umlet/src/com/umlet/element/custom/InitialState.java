package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class InitialState extends Entity {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		colorize(g2); // enable colors
		g2.setColor(activeColor);
		Constants.getFRC(g2);

		g2.fillOval(0, 0, this.getWidth(), this.getHeight());
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
