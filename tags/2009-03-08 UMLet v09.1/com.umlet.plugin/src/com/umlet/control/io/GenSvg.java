// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.awt.*;
import java.io.*;
import java.util.*;

import org.apache.batik.dom.*;
import org.apache.batik.svggen.*;
import org.w3c.dom.*;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;
     
public class GenSvg {
	private static GenSvg _instance;
	public static GenSvg getInstance() {
		if (_instance==null) {
			_instance=new GenSvg();
		}
		return _instance;
	}

    public void paint(Graphics2D g2d, DrawPanel panel) {   
	    Vector<Entity> v=panel.getAllEntities();
    
        for (int i=v.size()-1; i>=0; i--) {
        	Entity e= v.elementAt(i);
            g2d.translate(e.getX(), e.getY());
            e.paintEntity(g2d);
            g2d.translate(-e.getX(), -e.getY());
        }
    }

    public static void genSvg(DiagramHandler handler) {
      try {
        // Get a DOMImplementation
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation
        GenSvg test = new GenSvg();
        test.paint(svgGenerator,handler.getDrawPanel());

        // Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        svgGenerator.stream(out, useCSS);
      } catch (IOException e) {
      	System.out.println("IO Exception.");
      }
    }
    
    public static void createAndOutputSvgToFile(String filename, DiagramHandler handler) {
      try {
		OutputStream ostream = new FileOutputStream(filename);
		createSvgToStream(ostream, handler);
      } catch (IOException e) {
      	System.out.println("IO Exception.");
      }
    }
    
	public static void createSvgToStream(OutputStream ostream, DiagramHandler handler) {
		try {
			// Get a DOMImplementation
			 DOMImplementation domImpl =
				 GenericDOMImplementation.getDOMImplementation();

			 // Create an instance of org.w3c.dom.Document
			 Document document = domImpl.createDocument(null, "svg", null);

			 // Create an instance of the SVG Generator
			 SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

			 // Ask the test to render into the SVG Graphics2D implementation
			 GenSvg test = new GenSvg();
			 test.paint(svgGenerator, handler.getDrawPanel());

			 // Finally, stream out SVG to the standard output using UTF-8
			 // character to byte encoding
			 boolean useCSS = true; // we want to use CSS style attribute
			 //
			Writer out = new OutputStreamWriter(ostream,"UTF-8");
			 //Writer out = new OutputStreamWriter( new FileOutputStream(filename), "UTF-8");
			 //BufferedWriter bw = new BufferedWriter(
		
			 svgGenerator.stream(out, useCSS);
			//OutputStream ostream = new FileOutputStream("c:\\t\\out.pdf");
			/*TranscoderOutput output = new TranscoderOutput(ostream);
			// save the image
			t.transcode(input, output);
			// flush and close the stream then exit
			ostream.flush();
			ostream.close();*/			 		
		} catch (Exception e) {
		  	 System.out.println("UMLet: Error: Exception in outputPdf: "+e);
		}
    }
}



















