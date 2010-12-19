// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.umlet.control.diagram.DiagramHandler;

public class GenSvg {
	private static GenSvg _instance;

	public static GenSvg getInstance() {
		if (_instance == null) {
			_instance = new GenSvg();
		}
		return _instance;
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
			DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

			// Create an instance of org.w3c.dom.Document
			Document document = domImpl.createDocument(null, "svg", null);

			// Create an instance of the SVG Generator
			SVGGraphics2D graphics2d = new SVGGraphics2D(document);

			// Render into the SVG Graphics2D
			handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d);

			// Finally, stream out SVG to the standard output using UTF-8 character to byte encoding
			Writer out = new OutputStreamWriter(ostream, "UTF-8");
			// Writer out = new OutputStreamWriter( new FileOutputStream(filename), "UTF-8");
			// BufferedWriter bw = new BufferedWriter(

			graphics2d.stream(out, false);
			// OutputStream ostream = new FileOutputStream("c:\\t\\out.pdf");
			/*
			 * TranscoderOutput output = new TranscoderOutput(ostream);
			 * // save the image
			 * t.transcode(input, output);
			 * // flush and close the stream then exit
			 * ostream.flush();
			 * ostream.close();
			 */
		} catch (Exception e) {
			System.out.println("UMLet: Error: Exception in outputSvg: " + e);
		}
	}
}
