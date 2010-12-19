// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;
// Some import to have access to more Java features
import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class AlternativeUseCase extends com.umlet.element.base.Entity {

  // Change this method if you want to edit the graphical 
  // representation of your custom element.
  public void paintEntity(Graphics g) {

    // Some unimportant initialization stuff; setting color, font 
    // quality, etc. You should not have to change this.
    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2); //enable colors
    g2.setColor(_activeColor); this.handler.getFRC(g2);

    g2.setComposite(composites[1]);
    g2.setColor(_fillColor);
    g2.fillRect(0,0,getWidth()-1,getHeight()-1);
    g2.setComposite(composites[0]);
    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
    
    g2.drawRect(0,0,getWidth()-1,getHeight()-1);
    
    boolean center = false;

    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=this.handler.getDistLineToText();
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      yPos+=this.handler.getFontsize();
      if (s.equals("--")) {
    	  yPos=35;
          center = true;
      }
      else if (center == true) {
    	  this.handler.writeText(g2,s,(getWidth()-1)/2, yPos, true);
    	  center = false;
      }
      else {
    	  this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
    	  yPos+=this.handler.getDistTextToText();
      }
    }
   
    g2.drawLine(0,30,getWidth()-1,30);
    g2.drawOval(getWidth()-59,3,55,20);
  }
}
