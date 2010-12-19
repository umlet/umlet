// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class Systemborder extends com.umlet.element.base.Entity {

  public void paintEntity(Graphics g) {

    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2); //enable colors
    g2.setColor(_activeColor); this.handler.getFRC(g2);

    g2.setComposite(composites[1]);
    g2.setColor(_fillColor);
    g2.fillRect(0,0,getWidth()-1,getHeight()-1);
    g2.setComposite(composites[0]);
    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
    g2.drawRect(0,0,getWidth()-1,getHeight()-1);
    
    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=this.handler.getDistLineToText();
    boolean center = false;
    boolean downleft = false;
    boolean upcenter = false;
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      if (s.startsWith("center:")&& !s.equals("center:")) {
          if (tmp.size() == 1) {
              yPos=((getHeight()-1)/2)-10;
          }
          else {
              yPos=((getHeight()-1)/2)-10-this.handler.getFontsize()*(tmp.size()/2);
          }
          center = true;
          s = s.replace("center:","");
      }
      else if (s.startsWith("bottomleft:")&& !s.equals("bottomleft:")) {
          downleft = true;
          s = s.replace("bottomleft:","");
          yPos=(getHeight()-1)-this.handler.getFontsize();
      }
      else if (s.startsWith("topcenter:")&& !s.equals("topcenter:")) {
          upcenter = true;
          s = s.replace("topcenter:","");
          //yPos+=this.handler.getFontsize();
      } 
      if (center) {
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s,(getWidth()-1)/2, yPos, true);
          yPos+=2*this.handler.getDistTextToText(); 
      }
      else if (downleft) {
          this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
      }
      else if (upcenter) {
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s,(getWidth()-1)/2, yPos, true);
          yPos+=this.handler.getDistTextToText();
      }
      else {
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
          yPos+=this.handler.getDistTextToText();
      }
    }
 
  }
  
  // Change this method if you want to set the resize-attributes of
  // your custom element
  public int getPossibleResizeDirections() {
    // Remove from this list the borders you don't want to be resizeable.
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }
} 
