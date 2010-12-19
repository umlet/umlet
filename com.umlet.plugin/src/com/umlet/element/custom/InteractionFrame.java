// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class InteractionFrame extends Entity {
    public void paintEntity(Graphics g) {
        Graphics2D g2=(Graphics2D) g;
        g2.setFont(this.handler.getFont());
        Composite[] composites = colorize(g2); //enable colors
        g2.setColor(_activeColor);
        this.handler.getFRC(g2); 

        int yPos=0;
        yPos+=this.handler.getDistLineToText();
        
        g2.setComposite(composites[1]);
        g2.setColor(_fillColor);
        g2.fillRect(0,0,this.getWidth()-1, this.getHeight()-1);
        g2.setComposite(composites[0]);
        if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
        g2.drawRect(0,0,this.getWidth()-1, this.getHeight()-1);
        

        Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");

        int textWidth=0;
//      A.Mueller start
        boolean center = false;
        int topHeight=tmp.size();
        int maxWidthTemp = 0;
        
        for (int i=0; i<tmp.size(); i++) {
            String s = tmp.elementAt(i);
            maxWidthTemp = Math.max(this.handler.getPixelWidth(g2,s),maxWidthTemp);
            if (s.equals("--"))
            {
                textWidth=this.handler.getDistLineToText()+maxWidthTemp+this.handler.getDistTextToLine();
                topHeight=i;
                yPos += (this.handler.getDistTextToLine() + this.handler.getDistLineToText());
                center = true;
            } else if (s.equals("-."))
            {
                yPos += this.handler.getDistTextToLine();
                g2.setStroke(Constants.getStroke(1, 1));
                g2.drawLine(0, yPos, this.getWidth(), yPos);
                g2.setStroke(Constants.getStroke(0, 1));
                yPos += this.handler.getDistLineToText();
            } else
            {
                yPos += this.handler.getFontsize();
                if (center)
                    this.handler.writeText(g2, s, this.getWidth() / 2, yPos, true);
                else
                    this.handler.writeText(g2, s, this.handler.getFontsize() / 2, yPos,
                            false);
                yPos += this.handler.getDistTextToText();
            }
        }
        if (textWidth == 0) textWidth=maxWidthTemp;
        /*<OLDCODE>
         for (int i=0; i<tmp.size(); i++) {
            String s=tmp.elementAt(i);
            yPos+=this.handler.getFontsize();
            this.handler.write(g2,s,this.handler.getFontsize()/2, yPos, false);
            yPos+=this.handler.getDistTextToText();
            TextLayout l=new TextLayout(s, this.handler.getFont(), this.handler.getFRC(g2));
            Rectangle2D r2d=l.getBounds();
            textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);
        }
         </OLDCODE>*/
//A.Mueller end
        
        
        
        
        int w=((this.handler.getFontsize()*7)>textWidth)?(this.handler.getFontsize()*7):(textWidth);
        //A.Mueller start
        int h=topHeight*(this.handler.getFontsize()+3)+this.handler.getFontsize();
        int sw=w-(topHeight-1)*this.handler.getFontsize();
        //<OLDCODE>
        //int h=tmp.size()*(this.handler.getFontsize()+3)+this.handler.getFontsize();
        //int sw=w-(tmp.size()-1)*this.handler.getFontsize();
        //</OLDCODE>
        //A.Mueller end
        
        
        g2.drawLine(0,h,sw,h);
        g2.drawLine(w+this.handler.getFontsize(),0,w+this.handler.getFontsize(),this.handler.getFontsize());
        g2.drawLine(sw,h,w+this.handler.getFontsize(),this.handler.getFontsize());
    }
}