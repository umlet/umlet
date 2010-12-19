// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class SeqObjectActive extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
		g2.fillRect(0,0,this.getWidth()-1,this.getHeight()-1);
		g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
		
	    g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);




	}
	public int getPossibleResizeDirections() { //allow height changes only
		return Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM;
	}
}
