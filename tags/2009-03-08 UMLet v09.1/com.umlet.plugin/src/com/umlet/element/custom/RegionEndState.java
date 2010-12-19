// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.umlet.element.base.Entity;
import java.awt.geom.AffineTransform;

@SuppressWarnings("serial")
public class RegionEndState extends Entity
{

	
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		g2.drawOval(0,0,this.getWidth()-1,this.getHeight()-1);
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform)at.clone();
		at2.rotate(Math.toRadians(45),getWidth()/2, getHeight()/2);
		g2.setTransform(at2);
		g2.drawLine(0,this.getHeight()/2,this.getWidth(),this.getHeight()/2);
		g2.drawLine(this.getWidth()/2,0,this.getWidth()/2,this.getHeight());
		g2.setTransform(at);
		
	}
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
