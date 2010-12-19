// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class ActiveClass extends Entity {	
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos=this.getHeight()/2-tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;


		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=this.handler.getFontsize();
			this.handler.writeText(g2,s,(int)this.getWidth()/2, yPos, true);
			yPos+=this.handler.getDistTextToText();
		}


		g2.drawLine(0,0,this.getWidth(),0);
		g2.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
		g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
		g2.drawLine(0, this.getHeight()-1, 0, 0);
		
		g2.drawLine(this.handler.getFontsize()/2,0,this.handler.getFontsize()/2,this.getHeight()-1);
		g2.drawLine(this.getWidth()-this.handler.getFontsize()/2,0,this.getWidth()-this.handler.getFontsize()/2,this.getHeight()-1);
	}
}
