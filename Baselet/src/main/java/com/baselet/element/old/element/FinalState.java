package com.baselet.element.old.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.Direction;
import com.baselet.element.old.OldGridElement;

/**
 * @author Ludwig
 */
@SuppressWarnings("serial")
public class FinalState extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);

		g2.drawOval(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.fillOval((int) (4 * zoom), (int) (4 * zoom), getRectangle().width - (int) (8 * zoom), getRectangle().height - (int) (8 * zoom));
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
}
