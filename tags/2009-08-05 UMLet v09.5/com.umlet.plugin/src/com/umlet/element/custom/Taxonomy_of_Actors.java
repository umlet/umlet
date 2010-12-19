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

@SuppressWarnings("serial")
public class Taxonomy_of_Actors extends com.umlet.element.base.Entity {
    
    public void paintEntity(Graphics g) {
        Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
        Composite[] composites = colorize(g2); //enable colors
        g2.setColor(_activeColor); this.handler.getFRC(g2);
        
        g2.setComposite(composites[1]);
        g2.setColor(_fillColor);
        g2.fillRect(0,0,this.getWidth()-1,this.getHeight()-1);
        g2.setComposite(composites[0]);
        if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
        g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
      
        Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
        
        boolean first = true;
        boolean root = true;
        int level = 0;
        int yPos=10;
        int head = 14;
        int abstand =10;
        int count = 2;
        String help="";
        Point nextDock = new Point(0 ,0);
        
        Vector<Point> dock = new Vector<Point>();
        // dock.add(nextDock);
        
        for (int i=0; i<tmp.size(); i++) {
            String s=(String)tmp.elementAt(i);
            
            if (root){
                // Root
                g2.drawOval(10,0+yPos,head,head);
                g2.drawLine(10+head/2,head+yPos,10+head/2,head*3);
                g2.drawLine(6,yPos+head+head/3,14+head,yPos+head+head/3);
                g2.drawLine(10+head/2,head*3,10+head,(head*4));
                g2.drawLine(10+head/2,head*3,10,(head*4));
                this.handler.writeText(g2,s,10,head*5,false);
                yPos+=8*head;
                nextDock = new Point(10+head/2, 5*head+4);
                dock.add(nextDock);
                count = count*2;
                level++;
                root = false;
                help = s;
                
            } else if (s.startsWith(">") && level > 0 && !root && !first) {
                
                level++;
                nextDock = dock.elementAt(level-1);
                nextDock = new Point(nextDock.x,nextDock.y);
                dock.add(nextDock);
                nextDock = dock.elementAt(level-1);
                int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
                int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
                int kanten_zahl = 3;
                
                g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
                
                g2.drawLine(nextDock.x,nextDock.y+9,nextDock.x,yPos-abstand);
                
                g2.drawLine(nextDock.x,yPos-abstand,nextDock.x+abstand*6,yPos-abstand);
                g2.drawOval(nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos-3*head,head,head);
                
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-2*head,nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand);
                g2.drawLine((nextDock.x+abstand*6+this.handler.getFontsize()/2)-4,yPos-2*head+head/3,(nextDock.x+abstand*6+this.handler.getFontsize()/2)+4+head,yPos-2*head+head/3);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand,nextDock.x+abstand*6+this.handler.getFontsize()/2+head,yPos+count);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos+count,nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand);
                if (s.length() > 1) {
                    this.handler.writeText(g2,s.substring(1),nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos+head+count,false);
                }
                nextDock = new Point(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos+head+count*2);
                dock.add(level,nextDock);
                yPos+=5*head;
                if (i<tmp.size()-1 && ((String)tmp.elementAt(i+1)).startsWith(">")){
                    help = help+s;
                }
            }else if (s.startsWith("<") && level > 1){
                do {
                    level--;
                    s = s.substring(1,s.length());
                }  while(s.startsWith("<") && level > 1);
                
                nextDock = dock.elementAt(level-1);
                int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
                int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
                int kanten_zahl = 3;
                g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
                
                g2.drawLine(nextDock.x,nextDock.y+9,nextDock.x,yPos-abstand);
                
                g2.drawLine(nextDock.x,yPos-abstand,nextDock.x+abstand*6,yPos-abstand);
                g2.drawOval(nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos-3*head,head,head);
                                
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-2*head,nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand);
                g2.drawLine((nextDock.x+abstand*6+this.handler.getFontsize()/2)-4,yPos-2*head+head/3,(nextDock.x+abstand*6+this.handler.getFontsize()/2)+4+head,yPos-2*head+head/3);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand,nextDock.x+abstand*6+this.handler.getFontsize()/2+head,yPos+count);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos+count,nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand);
                if (s.length() > 0) {
                    this.handler.writeText(g2,s,nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos+head+count,false);
                }
                nextDock = new Point(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos+head+count*2);
                dock.add(level,nextDock);
                yPos+=5*head;
            }else {
                first = false;
                nextDock = dock.elementAt(level-1);
                int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
                int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
                int kanten_zahl = 3;
                g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
                
                g2.drawLine(nextDock.x,nextDock.y+9,nextDock.x,yPos-abstand);
                
                g2.drawLine(nextDock.x,yPos-abstand,nextDock.x+abstand*6,yPos-abstand);
                
                g2.drawOval(nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos-3*head,head,head);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-2*head,nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand);
                g2.drawLine((nextDock.x+abstand*6+this.handler.getFontsize()/2)-4,yPos-2*head+head/3,(nextDock.x+abstand*6+this.handler.getFontsize()/2)+4+head,yPos-2*head+head/3);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand,nextDock.x+abstand*6+this.handler.getFontsize()/2+head,yPos+count);
                g2.drawLine(nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos+count,nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos-abstand);
                
                this.handler.writeText(g2,s,nextDock.x+abstand*6+this.handler.getFontsize()/2,yPos+count+head,false);
                
                nextDock = new Point(nextDock.x+abstand*6+this.handler.getFontsize()/2+head/2,yPos+head+count*2);
                dock.add(level,nextDock);
                yPos+=5*head;
                
                //count = count*2;
                if (i<tmp.size()-1 && ((String)tmp.elementAt(i+1)).startsWith("::")){
                    help = help+s;
                }
            }
        }
    }

    public int getPossibleResizeDirections() {
        return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
    }
    
    public StickingPolygon generateStickingBorder() {
        StickingPolygon p = new StickingPolygon();
        return p;
    }
}