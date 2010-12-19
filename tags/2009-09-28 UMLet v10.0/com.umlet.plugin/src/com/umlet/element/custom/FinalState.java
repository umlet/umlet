// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 29.07.2004
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

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
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		g2.drawOval(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.fillOval((int) (4 * zoom), (int) (4 * zoom), this.getWidth() - (int) (8 * zoom), this.getHeight() - (int) (8 * zoom));
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
