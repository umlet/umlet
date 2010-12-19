// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;

import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Decision extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		Polygon poly = new Polygon();
		poly.addPoint(this.getWidth()/2,0); 
		poly.addPoint(this.getWidth(),this.getHeight()/2);
		poly.addPoint(this.getWidth()/2,this.getHeight());
		poly.addPoint(0,this.getHeight()/2);
		
		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillPolygon(poly);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
		
	    g2.drawPolygon(poly);
	    
		//g2.drawLine(this.getWidth()/2,0,this.getWidth(),this.getHeight()/2);
		//g2.drawLine(this.getWidth()/2,0,0,this.getHeight()/2);
		//g2.drawLine(0,this.getHeight()/2,this.getWidth()/2,this.getHeight());
		//g2.drawLine(this.getWidth()-1,this.getHeight()/2,this.getWidth()/2-1,this.getHeight());
	}
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
