// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 29.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
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
public class TimeSignal extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);
		boolean ADAPT_SIZE=false;

		
		int x0,y0,b,h;
		x0=this.getWidth()/2-Umlet.getInstance().getMainUnit()-10;
		y0=0;
		b=this.getWidth()/2-Umlet.getInstance().getMainUnit()+2*Umlet.getInstance().getMainUnit()+10;
		h=2*Umlet.getInstance().getMainUnit()+20;
    
		//g2.drawLine(x0,y0,b,y0);
		//g2.drawLine(b, h, x0, h);
		//g2.drawLine(x0,y0,b, h);
		//g2.drawLine(b,y0,x0, h);
		
		Polygon poly = new Polygon();
		poly.addPoint(x0,y0);
		poly.addPoint(b,y0);
		poly.addPoint(x0,h);
		poly.addPoint(b,h);
		
		
		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillPolygon(poly);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
		
	    g2.drawPolygon(poly);
		
		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos+=4*Umlet.getInstance().getMainUnit();
		yPos+=this.handler.getDistLineToText();

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			if (s.equals("--")) {
				yPos+=this.handler.getDistTextToLine();
				g2.drawLine(this.getWidth()/2-this.handler.getFontsize()*4,yPos,this.getWidth()/2+this.handler.getFontsize()*4,yPos);
				yPos+=this.handler.getDistLineToText();
			} else {
				yPos+=this.handler.getFontsize();
				TextLayout l=new TextLayout(s, this.handler.getFont(), this.handler.getFRC(g2));
				Rectangle2D r2d=l.getBounds();
				int width=(int)r2d.getWidth();
				int xPos=this.getWidth()/2-width/2;
				if (xPos<0) { ADAPT_SIZE=true; break; }
				this.handler.writeText(g2, s, this.getWidth()/2, yPos, true);
				yPos+=this.handler.getDistTextToText();
			}
		}

		if (ADAPT_SIZE) {
			(new Resize(this,-Umlet.getInstance().getMainUnit(),0,0,0)).execute(this.handler);
			(new Resize(this,0,0,Umlet.getInstance().getMainUnit(),0)).execute(this.handler);
			return;
		}
		if (yPos>this.getHeight()) {
			(new Resize(this,0,0,0,20)).execute(this.handler);
			return;
		}

		
	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int tmpX=p.x-this.getX()-this.getWidth()/2;
		int tmpY=p.y-this.getY()-(2*Umlet.getInstance().getMainUnit()+20)/2;
		
		if ((tmpX>-4 && tmpX<+4)&&(tmpY>-4 && tmpY<+4)) {
			return 15;
		} else return 0;
	} */
	  @Override
	  public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	    StickingPolygon p = new StickingPolygon();
		int px = x + width/2;
		int py = y + (2*Umlet.getInstance().getMainUnit()+20)/2;
	    p.addPoint(new Point(px-4,py-4));
	    p.addPoint(new Point(px+4,py-4));
	    p.addPoint(new Point(px+4,py+4));
	    p.addPoint(new Point(px-4,py+4), true);
	    return p;
	  }
	
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}