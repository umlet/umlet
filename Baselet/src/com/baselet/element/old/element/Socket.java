package com.baselet.element.old.element;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.Direction;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class Socket extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(fgColor);

		int fnt = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2;
		this.setSize(fnt + 2, fnt + 2);
		int h = getRectangle().height;

		g2.drawArc(0, 0, h - 1, h - 1, 90, 180);
		g2.drawOval((int) (4 * zoom), (int) (4 * zoom), h - (int) (9 * zoom), h - (int) (9 * zoom));

	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}

}
