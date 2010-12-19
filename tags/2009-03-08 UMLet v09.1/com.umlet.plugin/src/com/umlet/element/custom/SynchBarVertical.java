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
public class SynchBarVertical extends Entity {
	private static int textHeight=0;
	
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		int yPos=0;
		textHeight=0; //reset

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		textHeight=tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText());
		boolean ADAPT_SIZE=false;

	    for (int i=0; i<tmp.size(); i++) {
	      String s=(String)tmp.elementAt(i);
	        yPos+=this.handler.getFontsize();
	        TextLayout l=new TextLayout(s, this.handler.getFont(), this.handler.getFRC(g2));
	        Rectangle2D r2d=l.getBounds();
	        int width=(int)r2d.getWidth();
	        if ((this.getWidth()/2-width/2)<0) { ADAPT_SIZE=true; break; }
	        this.handler.writeText(g2, s, this.getWidth()/2, yPos, true);
	        yPos+=this.handler.getDistTextToText();
	    }

	    if (ADAPT_SIZE) {
	      (new Resize(this,-Umlet.getInstance().getMainUnit(),0,0,0)).execute(this.handler);
	      (new Resize(this,0,0,Umlet.getInstance().getMainUnit(),0)).execute(this.handler);
	      return;
	    }
	    if (yPos>this.getHeight()) {
	      (new Resize(this,0,0,0,this.handler.getFontsize()+this.handler.getDistTextToText())).execute(this.handler);
	      return;
	    }

	    g2.fillRect(this.getWidth()/2-3,textHeight+this.handler.getDistTextToLine(),5,this.getHeight()-textHeight-this.handler.getDistTextToLine()*2);
	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret=0;
		int tmpX=p.x-this.getX();
		int tmpY=p.y-this.getY();

		if (tmpY>textHeight+4 && tmpY<this.getHeight()+4) {
			//if (tmpX>0 && tmpX<16) ret+=8;
			if (tmpX>this.getWidth()/2-4 && tmpX<this.getWidth()/2+4) ret+=2;
		}
		return ret;
	}*/
	
	  @Override
	  public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	    StickingPolygon p = new StickingPolygon();
	    p.addLine(new Point(x + width/2, y + textHeight), new Point(x + width/2, y + height));
	    return p;
	  }

	public int getPossibleResizeDirections() { //allow height changes only
		return Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM;
	}
}
