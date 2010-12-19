package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.*;
import java.util.*;

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
		this.setSize(this.handler.getFontsize()*9,this.handler.getFontsize()*3);
	}



	public void paintEntity(Graphics g) {

		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);


		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos=this.getHeight()/3-tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=this.handler.getFontsize();
			this.handler.writeText(g2,s,(int)this.getWidth()/2, yPos, true);
			yPos+=this.handler.getDistTextToText();
		}

        int w = this.getWidth();
        int h = this.getHeight();

        g2.drawRoundRect(0,0,w-1,h-1,30,30);

        //draw substate sign
        g2.drawRoundRect(w-65, h-16, 20, 10, 8, 8);
        g2.drawLine(w-45, h-11, w-35, h-11);
        g2.drawRoundRect(w-35, h-16, 20, 10, 8, 8);
        /*g2.drawLine(w-(f+(f/2)),f/2,w-(f+(f/2)),(int)(f*1.5));   //vertical center line
        g2.drawLine(w-(f*2),f,w-f,f);    // horizontal line
        g2.drawLine(w-(f*2),f,w-(f*2),(int)(f*1.5));  // left vertical line
        g2.drawLine(w-f,f,w-f,(int)(f*1.5));  // right vertical line*/

	}
 // Change this method if you want to set the resize-attributes of
 
// your custom element
  public int getPossibleResizeDirections() {
    // Remove from this list the borders you don't want to be resizeable.
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;

  }

}	