package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.Main;
import com.baselet.control.enumerations.Direction;
import com.baselet.element.OldGridElement;


/**
 * @author Ludwig
 */
@SuppressWarnings("serial")
public class FinalState extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.drawOval(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		g2.fillOval((int) (4 * zoom), (int) (4 * zoom), this.getZoomedSize().width - (int) (8 * zoom), this.getZoomedSize().height - (int) (8 * zoom));
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
}
