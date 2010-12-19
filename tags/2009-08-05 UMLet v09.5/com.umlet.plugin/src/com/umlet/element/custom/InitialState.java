// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 29.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class InitialState extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		g2.fillOval(0,0,this.getWidth(),this.getHeight());
	}
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
