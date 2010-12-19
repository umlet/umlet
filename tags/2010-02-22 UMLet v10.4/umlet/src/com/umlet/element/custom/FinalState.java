package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 */
@SuppressWarnings("serial")
public class FinalState extends Entity {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		colorize(g2); // enable colors
		g2.setColor(activeColor);
		Constants.getFRC(g2);

		g2.drawOval(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.fillOval((int) (4 * zoom), (int) (4 * zoom), this.getWidth() - (int) (8 * zoom), this.getHeight() - (int) (8 * zoom));
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
