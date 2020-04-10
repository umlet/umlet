package com.baselet.element.old.element;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.baselet.control.HandlerElementMap;

@SuppressWarnings("serial")
public class StateSubDiagram extends State {

	@Override
	public void paintEntity(Graphics g) {
		super.paintEntity(g);

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();
		Graphics2D g2 = (Graphics2D) g;
		int w = getRectangle().width;
		int h = getRectangle().height;

		// draw substate sign
		g2.drawRoundRect(w - (int) (65 * zoom), h - (int) (16 * zoom), (int) (20 * zoom), (int) (10 * zoom), (int) (8 * zoom), (int) (8 * zoom));
		g2.drawLine(w - (int) (45 * zoom), h - (int) (11 * zoom), w - (int) (35 * zoom), h - (int) (11 * zoom));
		g2.drawRoundRect(w - (int) (35 * zoom), h - (int) (16 * zoom), (int) (20 * zoom), (int) (10 * zoom), (int) (8 * zoom), (int) (8 * zoom));
	}

}
