// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Artefact extends Entity
{
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		//symbol outline
		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
		g2.fillRect(0,0,this.getWidth()-1,this.getHeight()-1);
		g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
	    g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
		
		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=10;
		int startY=this.handler.getDistLineToText();

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=this.handler.getFontsize();
			this.handler.writeText(g2,s,this.handler.getDistLineToText(),startY+ yPos, false);
			yPos+=this.handler.getDistTextToText();
		}

		//small component symbol
		g2.drawLine(this.getWidth()-30,10,this.getWidth()-30,40);
		g2.drawLine(this.getWidth()-30,40,this.getWidth()-5,40);
		g2.drawLine(this.getWidth()-5,40,this.getWidth()-5,20);
		g2.drawLine(this.getWidth()-5,20,this.getWidth()-15,10);
		g2.drawLine(this.getWidth()-15,10,this.getWidth()-30,10);
		
		g2.drawLine(this.getWidth()-5,20,this.getWidth()-15,20);
		g2.drawLine(this.getWidth()-15,20,this.getWidth()-15,10);
		
	}
}
