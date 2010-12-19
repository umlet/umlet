package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class Text extends com.umlet.element.base.Entity {

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
      this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
      yPos+=this.handler.getDistTextToText();
    }


    // Finally, change other graphical attributes using
    // drawLine, getWidth, getHeight..
    /*  int fnt = this.handler.getFontsize()/3;
    int w = getWidth();
    int h = getHeight();

    g2.drawLine(1,1,fnt,1);
    g2.drawLine(1,1,1,fnt);
    g2.drawLine(w-1,h,w-1,h-fnt);
    g2.drawLine(w-1,h-1,w-fnt,h-1);*/

}


  // Change this method if you want to set the resize-attributes of
  // your custom element
  public int getPossibleResizeDirections() {
    // Remove from this list the borders you don't want to be resizeable.
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }


  // Advanced: change this method to modify the area where relations
  // stick to your custom element.
  @Override
  public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
    // By default, the element returns its outer borders. Change it,
    // if your element needs to stick to relations differently.
    // See, for example, the source code of the UML interface element.
	return null;
  }
}
