// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 21.07.2003
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.umlet.control.diagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.umlet.constants.Constants;

/**
 * @author unknown
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class SelectorFrame extends JComponent {

	private int offset_x;
	private int offset_y;

	public SelectorFrame() {
		super();
		reset();
	}

	public void reset() {
		offset_x = 0;
		offset_y = 0;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.setStroke(Constants.getStroke(1, 1));
		if (Constants.displaceDrawingByOnePixel()) g2.drawRect(1, 1, getWidth() - 1, getHeight() - 1);
		else g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setStroke(Constants.getStroke(0, 1));
	}

	public void resizeTo(int x, int y) {
		int locx = this.getX(), locy = this.getY();
		int sizx = x - this.getX();
		int sizy = y - this.getY();

		if (sizx - this.offset_x < 0) {
			locx += sizx;
			sizx = sizx * (-1) + this.offset_x;
			this.offset_x = sizx;
		}
		else {
			sizx -= this.offset_x;
			locx += this.offset_x;
			this.offset_x = 0;
		}

		if (sizy - this.offset_y < 0) {
			locy += sizy;
			sizy = sizy * (-1) + this.offset_y;
			this.offset_y = sizy;
		}
		else {
			sizy -= this.offset_y;
			locy += this.offset_y;
			this.offset_y = 0;
		}

		this.setLocation(locx, locy);
		this.setSize(sizx, sizy);
	}
}
