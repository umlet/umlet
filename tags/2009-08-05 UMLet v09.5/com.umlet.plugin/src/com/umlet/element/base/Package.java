// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Package extends Entity {

  Area lastKnown = new Area();
    
  public Entity CloneFromMe() {
    Package c=new Package();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }

  public Package() {
    super();
  }

  private Vector<String> getStringVector() {
    Vector<String> ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    return ret;
  }

  public void paintEntity(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2); //enable colors
    g2.setColor(_activeColor);
    this.handler.getFRC(g2);

    Vector<String> tmp=new Vector<String>(getStringVector()); // in order to make the addition of "--" possible
    if (tmp.size()==0) tmp.add(" ");
    if (!tmp.contains("--")) tmp.add("--");

    int yPos=this.handler.getDistLineToText();
    boolean borders=false;
    // G. Mueller start
    boolean normal=false;
    // int maxUpperBox=5*this.handler.getFontsize();
    int maxUpperBox=(int)(this.getWidth()*0.4); // I think this looks better
    int lines = 0;
    
    int yPosBorder = yPos;

    //LME: coloring (some code doubled)
    for (int i=0; i<tmp.size(); i++) {
    	String s=tmp.elementAt(i);
    	if (tmp.elementAt(0).equals("--") && borders==false) yPosBorder = this.handler.getDistLineToText()+this.handler.getDistTextToText()+this.handler.getFontsize(); // if there is no Packagename
    	if (s.equals("--") && borders==false) {
    		g2.setComposite(composites[1]);
    		g2.setColor(_fillColor);
    		g2.fillRect(0,0,maxUpperBox,yPosBorder);
    		g2.fillRect(0,yPosBorder,this.getWidth()-1,this.getHeight()-yPosBorder-1);
    		g2.setComposite(composites[0]);
    		if(_selected) g2.setColor(_activeColor);
    		else g2.setColor(_deselectedColor);

    		g2.drawRect(0,0,maxUpperBox,yPosBorder);
    		g2.drawRect(0,yPosBorder,this.getWidth()-1,this.getHeight()-yPosBorder-1);
    		// yPos to write the String centered
    		yPosBorder=this.handler.getFontsize()/2+yPosBorder/2+this.getHeight()/2-(tmp.size()-lines)*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;
    	} else if (!normal && i>1 && (tmp.elementAt(i-1).equals("--")&& tmp.elementAt(i).startsWith("left:"))) {
    		yPosBorder=(lines+1)*(this.handler.getFontsize()+this.handler.getDistTextToText())/2+this.handler.getDistTextToText()+this.handler.getFontsize()+this.handler.getDistTextToText();
    	} else if (!borders){
    		maxUpperBox=Math.max(maxUpperBox,this.handler.getPixelWidth(g2,s)+this.handler.getFontsize());
    		yPosBorder+=this.handler.getFontsize()+this.handler.getDistTextToText();
    	} else if (normal) {
    		yPosBorder+=this.handler.getFontsize()+this.handler.getDistTextToText();
    	} else if(!normal){
    		yPosBorder+=this.handler.getFontsize()+this.handler.getDistTextToText();
    	}
    }
    
    
    for (int i=0; i<tmp.size(); i++) {
      String s=tmp.elementAt(i);
      
      if (tmp.elementAt(0).equals("--") && borders==false) yPos = this.handler.getDistLineToText()+this.handler.getDistTextToText()+this.handler.getFontsize(); // if there is no Packagename
      
      if (s.equals("--") && borders==false) {
        borders=true;
//        g2.drawRect(0,0,maxUpperBox,yPos);
//        g2.drawRect(0,yPos,this.getWidth()-1,this.getHeight()-yPos-1);
        // yPos to write the String centered
        yPos=this.handler.getFontsize()/2+yPos/2+this.getHeight()/2-(tmp.size()-lines)*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;
      } else if (!normal && i>1 && (tmp.elementAt(i-1).equals("--")&& tmp.elementAt(i).startsWith("left:"))) {
          // writes the string normal
          yPos=(lines+1)*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;
          yPos+=this.handler.getDistTextToText();
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s.substring(5),this.handler.getFontsize()/2, yPos, false);
          yPos+=this.handler.getDistTextToText();
          normal = true;
      } else if (!borders){
          
          lines++;
          maxUpperBox=Math.max(maxUpperBox,this.handler.getPixelWidth(g2,s)+this.handler.getFontsize());
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
          yPos+=this.handler.getDistTextToText();
          
      } else if (normal) {
          
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
          yPos+=this.handler.getDistTextToText();
      
      } else if(!normal){
           
          yPos+=this.handler.getFontsize();
          this.handler.writeText(g2,s,(int)this.getWidth()/2, yPos, true);
          yPos+=this.handler.getDistTextToText();
               
      }
      // G. Mueller End
    }

    /*Rectangle r=this.getBounds();
    g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
    if (_selected) {
      g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
    }*/
  }
  @Override
  public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
    StickingPolygon p = new StickingPolygon();
    Vector<String> tmp=new Vector<String>(getStringVector());
    if (tmp.size()==0) tmp.add(" ");
    int yPos=this.handler.getDistLineToText();
    boolean borders=false;
    //int maxUpperBox=5*this.handler.getFontsize();
    int maxUpperBox=(int)(width*0.4);
    for (int i=0; i<tmp.size(); i++) {
        String s=tmp.elementAt(i);
        // G. Mueller start
        if (tmp.elementAt(0).equals("--") && borders==false) yPos = this.handler.getDistLineToText()+this.handler.getDistTextToText()+this.handler.getFontsize(); // if there is no Packagename
        // G.Mueller End
        if (s.equals("--") && borders==false) borders=true;
        else if(!borders) {
            maxUpperBox=Math.max(maxUpperBox,this.handler.getPixelWidth((Graphics2D)this.getGraphics(),s)+this.handler.getFontsize());
            yPos+=this.handler.getFontsize()+this.handler.getDistTextToText();
        }
    }
    p.addPoint(new Point(x,y));
    p.addPoint(new Point(x + maxUpperBox, y));
    p.addPoint(new Point(x + maxUpperBox, y + yPos));   
    p.addPoint(new Point(x + width,y + yPos));
    p.addPoint(new Point(x + width,y + height));
    p.addPoint(new Point(x , y + height), true);
    return p;
  }
}