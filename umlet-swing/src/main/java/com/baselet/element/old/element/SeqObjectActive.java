package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.Direction;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class SeqObjectActive extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);

	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> returnSet = super.getResizeArea(x, y);
		returnSet.remove(Direction.LEFT);
		returnSet.remove(Direction.RIGHT);
		return returnSet;
	}
}
