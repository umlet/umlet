package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.baselet.element.OldGridElement;


/**
 * @author Ludwig
 */
@SuppressWarnings("serial")
public class FinalState extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.drawOval(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		g2.fillOval((int) (4 * zoom), (int) (4 * zoom), this.getSize().width - (int) (8 * zoom), this.getSize().height - (int) (8 * zoom));
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
