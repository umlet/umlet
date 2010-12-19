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
import com.umlet.element.base.Entity;

/** 
* <p>Title: </p> 
* <p>Description: </p> 
* <p>Copyright: Copyright (c) 2001</p> 
* <p>Company: </p> 
* @author unascribed 
* @version 1.0 
*/ 

@SuppressWarnings("serial")
public class EER_Entity extends Entity {
	int ySave = 0;
	boolean hasAttributes = false;
	
	private Vector<String> getStringVector() {
	    Vector<String> ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
	    return ret;
	}
	
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
	    g2.setFont(this.handler.getFont());
	    Composite[] composites = colorize(g2); //enable colors
	    g2.setColor(_activeColor);

	    Vector<String> tmp=getStringVector();
	    boolean CENTER=true;
	    boolean drawInnerRect = false;
	    int yPos=this.handler.getDistLineToText()*2;

	    //### draw rectangles and lines (some duplicated code)
	    Polygon poly = new Polygon();
	    for (int i=0; i<tmp.size(); i++) {
	      String s=(String)tmp.elementAt(i);
	      if (s.equals("--")) {
	        CENTER=false;
	        ySave = yPos+this.handler.getDistLineToText()*2;
	        yPos+=this.handler.getDistLineToText()*3;
	      } else {
	        yPos+=this.handler.getFontsize();
	        if (CENTER && s.startsWith("##")) drawInnerRect = true;
	        yPos+=this.handler.getDistTextToText();
	        if(CENTER) ySave = yPos;
	      }
	    }
    	poly.addPoint(0,0);
    	poly.addPoint(this.getWidth()-1,0);
	    if(CENTER) {
	    	hasAttributes = false; //see getStickingBorder()
	    	ySave = this.getHeight();
	    	poly.addPoint(this.getWidth()-1,this.getHeight()-1);
	    	poly.addPoint(0,this.getHeight()-1);
	    } else {
	    	hasAttributes = true; //see getStickingBorder()
	    	g.drawLine(10,ySave,10,yPos+this.handler.getDistTextToText()-this.handler.getDistLineToText());
			poly.addPoint(this.getWidth()-1,ySave);
			poly.addPoint(0,ySave);
	    }
    
	    g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillPolygon(poly);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
	    g2.drawPolygon(poly);
	    
	    if(drawInnerRect) {
	    	if(CENTER) g.drawRect(3,3,this.getWidth()-7,this.getHeight()-7);
	    	else g.drawRect(3,3,this.getWidth()-7,ySave -6);
	    }

		    
	    //#### draw text
    	CENTER=true;
	    yPos=this.handler.getDistLineToText()*2;

	    for (int i=0; i<tmp.size(); i++) {
	      String s=(String)tmp.elementAt(i);
	      if (s.equals("--")) {
	        CENTER=false;
	        ySave = yPos+this.handler.getDistLineToText()*2;
	        yPos+=this.handler.getDistLineToText()*3;
	      } else {
	        yPos+=this.handler.getFontsize();
	        if (CENTER) {
	        	String s1 = s;
	        	if(s.startsWith("##")) {
	    	      	drawInnerRect = true;
	    	      	s1 = s1.substring(2);
	        	}
	        	this.handler.writeText(g2,s1,(int)this.getWidth()/2, yPos, true);
	        } else {
	        	this.handler.writeText(g2,s,this.handler.getFontsize(), yPos, false);
	        }
	        yPos+=this.handler.getDistTextToText();
	        if(CENTER) ySave = yPos;
	      }
	    }
	}
	
	  @Override
	  public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + width, y + ySave-1));
		if(!hasAttributes) 
			p.addPoint(new Point(x, y + ySave-1), true);
		else
			p.addLine(new Point(x, y + ySave-1), new Point(x, y));
		return p;
	}
} 

