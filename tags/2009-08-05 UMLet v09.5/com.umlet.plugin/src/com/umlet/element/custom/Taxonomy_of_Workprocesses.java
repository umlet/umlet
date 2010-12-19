// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class Taxonomy_of_Workprocesses extends com.umlet.element.base.Entity {

public void paintEntity(Graphics g) {

    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2); //enable colors
    g2.setColor(_activeColor); this.handler.getFRC(g2);
    
    g2.setComposite(composites[1]);
    g2.setColor(_fillColor);
    g2.fillRect(0,0,this.getWidth()-1,this.getHeight()-1);
    g2.setComposite(composites[0]);
    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
    g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
    
    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    
    boolean root = true;
    int level = 0;
    int yPos=10;
    int dist = 10*this.handler.getDistTextToText();
    int ovalHeight = 3*this.handler.getFontsize();
    int ovalWidth = 10*this.handler.getFontsize();
    int in = (int)(ovalWidth*0.5);
    Point nextDock = new Point(yPos+ovalWidth/2 ,10+ovalHeight);

    Vector<Point> dock = new Vector<Point>();
    dock.add(nextDock);
    
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      
      if (root){
    	  
    	  // Root
    	  g2.drawOval(10 ,0+yPos, ovalWidth ,ovalHeight);
    	  this.handler.writeText(g2,s,yPos+(ovalWidth/2),ovalHeight/2+yPos+3,true);
    	nextDock = new Point(in+10, yPos+ovalHeight);
  
    	yPos+=2*ovalHeight - (dist/2);
       	  
       	  dock.add(nextDock);
    	  level++;
    	  root = false;
    	  
      } else if (s.startsWith(">") && level > 0 && !root ) {
         	  
    	  level++;
    	  nextDock = dock.elementAt(level-1);
    	  nextDock = new Point(nextDock.x,nextDock.y);
    	  dock.add(nextDock);
    	  nextDock = dock.elementAt(level-1);
	  int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
    	  int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
    	  int kanten_zahl = 3;
    	  g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
    	  g2.drawLine (nextDock.x,nextDock.y+9,nextDock.x,yPos);
    	  g2.drawLine (nextDock.x,yPos,nextDock.x+(ovalWidth/2),yPos);
    	  g2.drawOval(nextDock.x+(ovalWidth/2) ,yPos-(ovalHeight/2), ovalWidth ,ovalHeight);

    	  if (s.length() >1) {
    		  this.handler.writeText(g2,s.substring(1),nextDock.x+ovalWidth,yPos+3,true);
    	  }
    	  nextDock = new Point(nextDock.x+ovalWidth,yPos+(ovalHeight/2) );
    	  dock.set(level, nextDock);
    	  yPos+=2*ovalHeight - dist;

      }else if (s.startsWith("<") && level > 1){
    	 do {
    		 level--;
             // in-=(int)(packageWidth*1.5);
             s = s.substring(1,s.length());
         }  while(s.startsWith("<") && level > 1);

    	 nextDock = dock.elementAt(level-1);
    	 int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
    	 int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
    	 int kanten_zahl = 3;
         g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
         g2.drawLine (nextDock.x,nextDock.y+9,nextDock.x,yPos);
         g2.drawLine (nextDock.x,yPos,nextDock.x+(ovalWidth/2),yPos);
         g2.drawOval(nextDock.x+(ovalWidth/2) ,yPos-(ovalHeight/2), ovalWidth ,ovalHeight);
         this.handler.writeText(g2,s,nextDock.x+ovalWidth,yPos+3,true);
         nextDock = new Point(nextDock.x+ovalWidth,yPos+(ovalHeight/2) );
         dock.set(level, nextDock);
         yPos+=2*ovalHeight - dist;
      } else {
    	  nextDock = dock.elementAt(level-1);
    	  int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
    	  int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
    	  int kanten_zahl = 3;
    	  g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
    	  g2.drawLine (nextDock.x,nextDock.y+9,nextDock.x,yPos);
    	  g2.drawLine (nextDock.x,yPos,nextDock.x+(ovalWidth/2),yPos);
    	  g2.drawOval(nextDock.x+(ovalWidth/2) ,yPos-(ovalHeight/2), ovalWidth ,ovalHeight);
    	  this.handler.writeText(g2,s,nextDock.x+ovalWidth,yPos+3,true);
    	  nextDock = new Point(nextDock.x+ovalWidth,yPos+(ovalHeight/2) );
    	  dock.set(level, nextDock);
    	  yPos+=2*ovalHeight - dist;

      }
      
    }
  }

	public int getPossibleResizeDirections() {
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	public StickingPolygon generateStickingBorder() {
		StickingPolygon p = new StickingPolygon();
		return p;
	}
}
