// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Note extends Entity {
  public Entity CloneFromMe() {
    Note c=new Note();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }

  public Note() {
    super();
  }

  private Vector<String> getStringVector() {
    return Constants.decomposeStrings(this.getPanelAttributes(), "\n");
  }

  public void paintEntity(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.setFont(this.handler.getFont());
    //g2.setColor(_activeColor);
    this.handler.getFRC(g2);
    Composite[] composites = colorize(g2); //enable colors
	int yPos=0;
    yPos+=this.handler.getDistLineToText();

    Vector<String> tmp=this.getStringVector();

    Polygon poly = new Polygon();
    poly.addPoint(0,0); poly.addPoint(this.getWidth()-this.handler.getFontsize(),0);
    poly.addPoint(this.getWidth()-this.handler.getFontsize(),0); poly.addPoint(this.getWidth()-1, this.handler.getFontsize());
    poly.addPoint(this.getWidth()-1, this.handler.getFontsize()); poly.addPoint(this.getWidth()-1, this.getHeight()-1);
    poly.addPoint(this.getWidth()-1, this.getHeight()-1); poly.addPoint(0, this.getHeight()-1);
    poly.addPoint(0, this.getHeight()-1); poly.addPoint(0,0);
    //p.addPoint(this.getWidth()-Constants.getFontsize(),0); p.addPoint(this.getWidth()-Constants.getFontsize(), Constants.getFontsize());
    //p.addPoint(this.getWidth()-Constants.getFontsize(),Constants.getFontsize()); p.addPoint(this.getWidth()-1, Constants.getFontsize());

    
    g2.setComposite(composites[1]);
    g2.setColor(_fillColor);
    g2.fillPolygon(poly);
    g2.setComposite(composites[0]);
    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
    
    for (int i=0; i<tmp.size(); i++) {
        String s=(String)tmp.elementAt(i);
        yPos+=this.handler.getFontsize();
        this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
        yPos+=this.handler.getDistTextToText();
      }
    
    g2.drawLine(0,0,this.getWidth()-this.handler.getFontsize(),0);
    g2.drawLine(this.getWidth()-this.handler.getFontsize(),0, this.getWidth()-1, this.handler.getFontsize());
    g2.drawLine(this.getWidth()-1, this.handler.getFontsize(), this.getWidth()-1, this.getHeight()-1);
    g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
    g2.drawLine(0, this.getHeight()-1, 0, 0);
    g2.drawLine(this.getWidth()-this.handler.getFontsize(),0, this.getWidth()-this.handler.getFontsize(), this.handler.getFontsize());
    g2.drawLine(this.getWidth()-this.handler.getFontsize(),this.handler.getFontsize(), this.getWidth()-1, this.handler.getFontsize());
  }
}