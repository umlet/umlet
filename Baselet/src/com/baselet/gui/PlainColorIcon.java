package com.baselet.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import com.baselet.diagram.draw.ColorOwn;
import com.baselet.element.Converter;

public class PlainColorIcon implements Icon {

	private Color color;

	public PlainColorIcon(String color) {
		this.color = Converter.convert(ColorOwn.forString(color));
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color old_color = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, 10, 10);
		g.setColor(old_color);
	}

	@Override
	public int getIconWidth() {
		return 10;
	}

	@Override
	public int getIconHeight() {
		return 10;
	}

}
