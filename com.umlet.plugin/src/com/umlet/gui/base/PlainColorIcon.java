// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.gui.base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class PlainColorIcon implements Icon {

	private Color color;

	public PlainColorIcon(Color color) {
		this.color = color;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color old_color = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, 10, 10);
		g.setColor(old_color);
	}

	public int getIconWidth() {
		return 10;
	}

	public int getIconHeight() {
		return 10;
	}

}
