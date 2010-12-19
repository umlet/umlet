package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class SimpleComponent extends com.umlet.element.base.Entity {

  // Change this method if you want to edit the graphical 
  // representation of your custom element.
  public void paintEntity(Graphics g) {

    // Some unimportant initialization stuff; setting color, font 
    // quality, etc. You should not have to change this.
    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    g2.setColor(_activeColor); this.handler.getFRC(g2);


    // It's getting interesting here:
    // First, the strings you type in the element editor are read and
    // split into lines.
    // Then, by default, they are printed out on the element, aligned
    // to the left.
    // Change this to modify this default text printing and to react
    // to special strings
    // (like the "--" string in the UML class elements which draw a line).
    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=this.handler.getDistLineToText();
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);

      yPos+=this.handler.getFontsize();
      this.handler.writeText(g2,s,this.handler.getFontsize()*3, yPos, false);
 //offset into box
      yPos+=this.handler.getDistTextToText();
    }


    // Finally, change other graphical attributes using
    // drawLine, getWidth, getHeight..

    int fnt=this.handler.getFontsize();

    g2.drawLine(this.handler.getFontsize(),0,this.getWidth()-1,0);
 //top
    g2.drawLine(this.handler.getFontsize(),getHeight()-1,getWidth()-1,getHeight()-1); //bottom

    //left side
    g2.drawLine(this.handler.getFontsize(),0,this.handler.getFontsize(),fnt); //top
    g2.drawLine(fnt,fnt*2,fnt,fnt*3); //middle
    g2.drawLine(fnt,fnt*4,fnt,getHeight()-1);
 //bottom


    g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight()-1); //right side

    g2.drawLine(0,fnt,fnt*2,fnt); //top box top line
    g2.drawLine(0,fnt*2,fnt*2,fnt*2); //top box bottom line
    g2.drawLine(0,fnt,0,fnt*2); //top box left line
    g2.drawLine(fnt*2,fnt,fnt*2,fnt*2); //top box right line


    g2.drawLine(0,fnt*3,fnt*2,fnt*3); //bottom box top line
    g2.drawLine(0,fnt*4,fnt*2,fnt*4); //bottom box bottom line
    g2.drawLine(0,fnt*3,0,fnt*4); //bottom box left line
    g2.drawLine(fnt*2,fnt*3,fnt*2,fnt*4); //bottom box right line

  }


  // Change this method if you want to set the resize-attributes of
  // your custom element
  public int getPossibleResizeDirections() {
    // Remove from this list the borders you don't want to be resizeable.
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }
}
