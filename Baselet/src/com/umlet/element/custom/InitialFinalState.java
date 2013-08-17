package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class InitialFinalState extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(Color.red);
		

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		boolean initialState = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("i")) initialState = true;
		}

		if (!initialState) {
			g2.drawOval(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);

			g2.fillOval((int) (4 * zoom), (int) (4 * zoom), this.getRectangle().width - (int) (8 * zoom), this.getRectangle().height - (int) (8 * zoom));
		}
		else {
			g2.fillOval(0, 0, this.getRectangle().width, this.getRectangle().height);
		}
	}
}
