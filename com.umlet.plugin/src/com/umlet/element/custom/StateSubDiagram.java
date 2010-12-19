package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;

@SuppressWarnings("serial")
public class StateSubDiagram extends com.umlet.element.base.Entity {

	public StateSubDiagram() {
		super();
	}

	@Override
	public void assignToDiagram(DiagramHandler handler) {
		super.assignToDiagram(handler);
		this.setSize((int) this.getHandler().getZoomedFontsize() * 9, (int) this.getHandler().getZoomedFontsize() * 3);
	}

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = 0;
		yPos = this.getHeight() / 3 - tmp.size() * ((int) (this.getHandler().getZoomedFontsize() + this.getHandler().getZoomedDistTextToText())) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getZoomedFontsize();
			this.getHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getZoomedDistTextToText();
		}

		int w = this.getWidth();
		int h = this.getHeight();

		g2.drawRoundRect(0, 0, w - 1, h - 1, (int) (30 * zoom), (int) (30 * zoom));

		// draw substate sign
		g2.drawRoundRect(w - (int) (65 * zoom), h - (int) (16 * zoom), (int) (20 * zoom), (int) (10 * zoom), (int) (8 * zoom), (int) (8 * zoom));
		g2.drawLine(w - (int) (45 * zoom), h - (int) (11 * zoom), w - (int) (35 * zoom), h - (int) (11 * zoom));
		g2.drawRoundRect(w - (int) (35 * zoom), h - (int) (16 * zoom), (int) (20 * zoom), (int) (10 * zoom), (int) (8 * zoom), (int) (8 * zoom));
		/*
		 * g2.drawLine(w-(f+(f/2)),f/2,w-(f+(f/2)),(int)(f*1.5)); //vertical center line
		 * g2.drawLine(w-(f*2),f,w-f,f); // horizontal line
		 * g2.drawLine(w-(f*2),f,w-(f*2),(int)(f*1.5)); // left vertical line
		 * g2.drawLine(w-f,f,w-f,(int)(f*1.5)); // right vertical line
		 */

	}

	// Change this method if you want to set the resize-attributes of

	// your custom element
	@Override
	public int getPossibleResizeDirections() {
		// Remove from this list the borders you don't want to be resizeable.
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;

	}

}
