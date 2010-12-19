// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class InitialFinalState extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		g2.setColor(Color.red);
		this.handler.getFRC(g2);

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		boolean initialState=false;
		for (int i=0; i<tmp.size(); i++) {
			String s=(String)tmp.elementAt(i);
			if(s.equals("i")) initialState=true;
		}

		

		if(!initialState) {
			g2.drawOval(0,0,this.getWidth()-1,this.getHeight()-1);

			g2.fillOval(4,4,this.getWidth()-8,this.getHeight()-8);
		} else {
			g2.fillOval(0,0,this.getWidth(),this.getHeight());
		}
	}
}
