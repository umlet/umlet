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
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.Resize;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class SynchBarHorizontal extends Entity {
	private static int textWidth=0;
	
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		textWidth=0; //reset
		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=this.getHeight()/2-tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;
		boolean ADAPT_SIZE_X=false;
		int textHeight=tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText());
		
		for (int i=0; i<tmp.size(); i++) {
	    	String s=(String)tmp.elementAt(i);

	    	TextLayout l=new TextLayout(s, this.handler.getFont(), this.handler.getFRC(g2));
	    	Rectangle2D r2d=l.getBounds();
	    	textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);

	    	if ((this.getWidth()-textWidth)<0) { ADAPT_SIZE_X=true; break; }
	    	yPos+=this.handler.getFontsize();
	    	this.handler.writeText(g2, s, 0, yPos, false);
	    	yPos+=this.handler.getDistTextToText();
		}

		if (ADAPT_SIZE_X) {
			(new Resize(this,-Umlet.getInstance().getMainUnit(),0,0,0)).execute(this.handler);
			(new Resize(this,0,0,Umlet.getInstance().getMainUnit(),0)).execute(this.handler);
			return;
		}
		
		if (textHeight>this.getHeight()) {
		 (new Resize(this,0,0,0,20)).execute(this.handler);
		  return;
		}

		g2.fillRect(textWidth+this.handler.getDistTextToLine(),getHeight()/2-3,this.getWidth()-textWidth-this.handler.getDistTextToLine()*2,5);
	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret=0;
		int tmpX=p.x-this.getX();
		int tmpY=p.y-this.getY();
	    
	    if (tmpX>(textWidth+4) && tmpX<this.getWidth()+4) {
	        //if (tmpY>0 && tmpY<8) ret+=1; 
	        if (tmpY>this.getHeight()/2-8 && tmpY<this.getHeight()/2+8) ret+=4;
	    }
		return ret;
	}*/
	  @Override
	  public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	    StickingPolygon p = new StickingPolygon();
	    p.addLine(new Point(x + textWidth, y + height/2), new Point(x + width, y + height/2));
	    return p;
	}
	
	public int getPossibleResizeDirections() { //allow width changes only
		return Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT;
	}
}
