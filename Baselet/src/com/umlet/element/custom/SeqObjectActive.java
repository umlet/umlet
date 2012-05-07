package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.baselet.control.Constants;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class SeqObjectActive extends GridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

	}

	@Override
	public int getPossibleResizeDirections() { // allow height changes only
		return Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM;
	}
}
