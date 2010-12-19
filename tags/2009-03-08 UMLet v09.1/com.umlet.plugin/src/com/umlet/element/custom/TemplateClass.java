// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;
import com.umlet.element.base.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class TemplateClass extends Entity {
  public Entity CloneFromMe() {
    TemplateClass c=new TemplateClass();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }

  public TemplateClass() {
    super();
  }

  private Vector<String> getStringVector() {
    return Constants.decomposeStrings(this.getPanelAttributes(), "\n");
  }

  public void paintEntity(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.setFont(this.handler.getFont());
    g2.setColor(_activeColor);

    //this.handler.getFRC(g2);

    Vector<String> tmp=getStringVector();

    int yPos=0;
    yPos+=this.handler.getDistLineToText();

    boolean CENTER=true;
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      if (s.equals("--")) {
        CENTER=false;
        g2.drawLine(0,yPos,this.getWidth(),yPos);
        yPos+=this.handler.getDistLineToText();
      } else {
        yPos+=this.handler.getFontsize();
        if (CENTER) {
          this.handler.writeText(g2,s,(int)this.getWidth()/2, yPos, true);
        } else {
          this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
        }
        yPos+=this.handler.getDistTextToText();
      }
    }

    Rectangle r=this.getBounds();
    g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
    /*if (_selected) {
      g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
    }*/
  }


}