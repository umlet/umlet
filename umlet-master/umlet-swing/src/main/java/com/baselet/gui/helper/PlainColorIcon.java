package com.baselet.gui.helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import com.baselet.control.basics.Converter;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;

public class PlainColorIcon implements Icon {

	private Color color;

	public PlainColorIcon(String color) {
		this.color = Converter.convert(ColorOwn.forStringOrNull(color, Transparency.FOREGROUND));
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
