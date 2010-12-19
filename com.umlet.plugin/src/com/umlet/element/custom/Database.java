package com.umlet.element.custom;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class Database extends com.umlet.element.base.Entity {

  // Change this method if you want to edit the graphical
  // representation of your custom element.
  public void paintEntity(Graphics g) {

    // Some unimportant initialization stuff; setting color, font
    // quality, etc. You should not have to change this.
    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    g2.setColor(_activeColor); this.handler.getFRC(g2);

    int inset = this.handler.getFontsize();

    // It's getting interesting here:
    // First, the strings you type in the element editor are read and
    // split into lines.
    // Then, by default, they are printed out on the element, aligned
    // to the left.
    // Change this to modify this default text printing and to react
    // to special strings
    // (like the "--" string in the UML class elements which draw a line).
    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=inset+this.handler.getDistLineToText();
    boolean CENTER = true;
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      if (s.equals("--")) {
          CENTER=false;
          g2.drawLine(0,yPos,this.getWidth(),yPos);
          yPos+=this.handler.getDistLineToText();
        } else {
          yPos+=this.handler.getFontsize();
          if (CENTER) {
            this.handler.writeText(g2,s,this.getWidth()/2, yPos, true);
          } else {
            this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
          }
          yPos+=this.handler.getDistTextToText();
        }
    }


    // Finally, change other graphical attributes using
    // drawLine, getWidth, getHeight..
    g2.drawLine(0, this.getHeight()-1-inset/2, 0, inset/2);
    g2.drawOval(0, 0, this.getWidth(), inset);
    g2.drawArc(0, this.getHeight()-1-inset, this.getWidth(), this.handler.getFontsize(), 180, 180);
    g2.drawLine(this.getWidth()-1, inset/2,this.getWidth()-1, this.getHeight()-1-inset/2);
  }


  // Change this method if you want to set the resize-attributes of
  // your custom element
  public int getPossibleResizeDirections() {
    // Remove from this list the borders you don't want to be resizeable.
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }
}

