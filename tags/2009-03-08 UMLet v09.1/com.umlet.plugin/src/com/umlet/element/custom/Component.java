// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;

import com.umlet.constants.Constants;

import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Component extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);
		boolean normal = false; //G.M

		//symbol outline
		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillRect(0,0,this.getWidth()-1,this.getHeight()-1);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
	    
		g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
		
		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		int startY=this.getHeight()/2-tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);

			if (s.startsWith("'") || normal==true) { //G.M
				startY=this.handler.getFontsize();
				s=s.substring(1,s.length());
				yPos+=this.handler.getFontsize();
				this.handler.writeText(g2,s,10,startY+ yPos, false);
				yPos+=this.handler.getDistTextToText();
				normal = true;
			} else {
				if(s.startsWith("*")) {
					startY=this.handler.getFontsize();
					s=s.substring(1,s.length());
				}
				yPos+=this.handler.getFontsize();
				this.handler.writeText(g2,s,(int)this.getWidth()/2,startY+ yPos, true);
				yPos+=this.handler.getDistTextToText();
			}
		}

		//small component symbol
		g2.drawLine(this.getWidth()-50,10,this.getWidth()-15,10);
		g2.drawLine(this.getWidth()-50,35,this.getWidth()-15,35);
		g2.drawLine(this.getWidth()-15,10,this.getWidth()-15,35);
		g2.drawLine(this.getWidth()-50,10,this.getWidth()-50,15);
		g2.drawLine(this.getWidth()-55,15,this.getWidth()-45,15);
		g2.drawLine(this.getWidth()-55,20,this.getWidth()-45,20);
		g2.drawLine(this.getWidth()-55,15,this.getWidth()-55,20);
		g2.drawLine(this.getWidth()-45,15,this.getWidth()-45,20);
		g2.drawLine(this.getWidth()-50,20,this.getWidth()-50,25);
		g2.drawLine(this.getWidth()-55,25,this.getWidth()-45,25);
		g2.drawLine(this.getWidth()-55,30,this.getWidth()-45,30);
		g2.drawLine(this.getWidth()-55,25,this.getWidth()-55,30);
		g2.drawLine(this.getWidth()-45,25,this.getWidth()-45,30);
		g2.drawLine(this.getWidth()-50,30,this.getWidth()-50,35);
	}

}
