package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class InitialFinalState extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(Color.red);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());

		boolean initialState = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("i")) {
				initialState = true;
			}
		}

		if (!initialState) {
			g2.drawOval(0, 0, getRectangle().width - 1, getRectangle().height - 1);

			g2.fillOval((int) (4 * zoom), (int) (4 * zoom), getRectangle().width - (int) (8 * zoom), getRectangle().height - (int) (8 * zoom));
		}
		else {
			g2.fillOval(0, 0, getRectangle().width, getRectangle().height);
		}
	}
}
