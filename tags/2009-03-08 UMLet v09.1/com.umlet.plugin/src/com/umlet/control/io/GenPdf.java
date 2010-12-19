// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.awt.*;
import java.io.*;
import java.util.*;

import org.apache.batik.dom.*;
import org.apache.batik.svggen.*;
import org.apache.batik.transcoder.*;
import org.apache.fop.svg.*;
import org.w3c.dom.*;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;

public class GenPdf {

    private static GenPdf _instance;
    public static GenPdf getInstance() {
    	if (_instance==null) {
    		_instance=new GenPdf();
    	}
    	return _instance;
    }
    private GenPdf() {
    }
    
    private Dimension calculateCanvas(DrawPanel panel) {  	
    	int maxx=50;
    	int minx=0;
    	int maxy=50;
    	int miny=0;
    	
    	Vector<Entity> v = panel.getAllEntities();
    	for (int i=0; i<v.size(); i++) {
    		Entity e = v.elementAt(i);
    		maxx=Math.max(maxx,e.getX()+e.getWidth()+20);
    		maxy=Math.max(maxy,e.getY()+e.getHeight()+20);
    		minx=Math.min(minx,e.getX()-20);
    		miny=Math.min(miny,e.getY()-20);
    	}
    	return new Dimension(maxx-minx,maxy-miny);
    }
    private Dimension calculateTranslate(DrawPanel panel) {   	
    	int maxx=50;
    	int minx=0;
    	int maxy=50;
    	int miny=0;
    	
    	Vector<Entity> v=panel.getAllEntities();
    	for (int i=0; i<v.size(); i++) {
    		Entity e = v.elementAt(i);
    		maxx=Math.max(maxx,e.getX()+e.getWidth()+20);
    		maxy=Math.max(maxy,e.getY()+e.getHeight()+20);
    		minx=Math.min(minx,e.getX()-20);
    		miny=Math.min(miny,e.getY()-20);
    	}
    	return new Dimension(minx,miny);
    }
    
    public void createAndOutputPdfToFile(String filename, DiagramHandler handler) {
		try {
			OutputStream ostream = new FileOutputStream(filename);
			createPdfToStream(ostream, handler);
		} catch (Exception e) {
			System.out.println("UMLet: Error: Exception in outputPdf: "+e);
		}
    }
        
    public void createPdfToStream(OutputStream ostream, DiagramHandler handler) {
      try {
        // Get a DOMImplementation
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		Dimension trans=calculateTranslate(handler.getDrawPanel());
        svgGenerator.translate(-(double)trans.getWidth(),-(double)trans.getHeight());
		svgGenerator.setSVGCanvasSize(calculateCanvas(handler.getDrawPanel()));

        // Ask the test to render into the SVG Graphics2D implementation
        GenSvg test = new GenSvg();
        test.paint(svgGenerator,handler.getDrawPanel());

        // Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(baos, "UTF-8");

        svgGenerator.stream(out, useCSS);
		ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());

        // create a PDF transcoder
        PDFTranscoder t = new PDFTranscoder();

        // set the transcoding hints
        // t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,new Float(.8));

        // create the transcoder input
        TranscoderInput input = new TranscoderInput(bais);

        // create the transcoder output
       
        TranscoderOutput output = new TranscoderOutput(ostream);

        // save
        t.transcode(input, output);

        // flush and close the stream then exit
        ostream.flush();
        ostream.close();
        //System.exit(0);
      } catch (Exception e) {
      	System.out.println("UMLet: Error: Exception in outputPdf: "+e);
      }
    }
    
    
    
    
    
    
}
