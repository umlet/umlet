// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class GenPic {

    private static GenPic _instance;
    public static GenPic getInstance() {
    	if (_instance==null) {
    		_instance=new GenPic();
    	}
    	return _instance;
    }
    
    private GenPic() {
    }

    public void createImgToStream(String imgtype, OutputStream ostream, DiagramHandler handler) {
    	try {
			ImageIO.write(this.getImageFromDiagram(handler),imgtype,ostream);
			ostream.flush();
			ostream.close();
		} catch (IOException e) {
			System.out.println("UMLet: Error: Exception in outputIMG: "+e);
		}
    }
    
    public void createAndOutputImgToFile(String imgtype, String filename, DiagramHandler handler) {
      try {
		File file = new File(filename);
		ImageIO.write(this.getImageFromDiagram(handler), imgtype, file);
      } catch (Exception e) {
      	System.out.println("UMLet: Error: Exception in outputJpeg: "+e);
      }
    }
    
    public BufferedImage getImageFromDiagram(DiagramHandler handler) {
		/*get height and width of drawing area to clip the image*/
    	Rectangle bounds = handler.getDrawPanel().getContentBounds();

        BufferedImage im=new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics g=im.createGraphics();
        
        //tanslate needed for clipping
        g.translate(-bounds.x, -bounds.y);
        g.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);
		        
        g.setColor(Color.white);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        for(Entity e : handler.getDrawPanel().getAllEntities()) {
        	g.translate(e.getX(), e.getY());
        	e.paint(g);
        	g.translate(-e.getX(), -e.getY());
        }
        
		return im;
    }   
}
