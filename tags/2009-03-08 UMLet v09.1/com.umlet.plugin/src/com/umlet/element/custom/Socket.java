package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.*;

@SuppressWarnings("serial")
public class Socket extends com.umlet.element.base.Entity {

  public void paintEntity(Graphics g) {

    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    g2.setColor(_activeColor); this.handler.getFRC(g2);

    int fnt = this.handler.getFontsize()+(this.handler.getFontsize()/2);
    this.setSize(fnt+2,fnt+2);
    int h = this.getHeight();
    

    g2.drawArc(0,0,h-1,h-1,90,180); 
    g2.drawOval(4,4,h-9,h-9);

  }


  public int getPossibleResizeDirections() {
    return 0; }

}
