// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class InitialFinalState extends Entity {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(Color.red);
		this.getHandler().getFRC(g2);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		boolean initialState = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("i")) initialState = true;
		}

		if (!initialState) {
			g2.drawOval(0, 0, this.getWidth() - 1, this.getHeight() - 1);

			g2.fillOval((int) (4 * zoom), (int) (4 * zoom), this.getWidth() - (int) (8 * zoom), this.getHeight() - (int) (8 * zoom));
		}
		else {
			g2.fillOval(0, 0, this.getWidth(), this.getHeight());
		}
	}
}
