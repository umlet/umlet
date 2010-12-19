// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;

public class GenPdf {

	private static GenPdf _instance;

	public static GenPdf getInstance() {
		if (_instance == null) {
			_instance = new GenPdf();
		}
		return _instance;
	}

	private GenPdf() {}

	public void createAndOutputPdfToFile(String filename, DiagramHandler handler) {
		try {
			OutputStream ostream = new FileOutputStream(filename);
			createPdfToStream(ostream, handler);
		} catch (Exception e) {
			System.out.println("UMLet: Error: Exception in outputPdf: " + e);
		}
	}

	public void createPdfToStream(OutputStream ostream, DiagramHandler handler) {
		try {
			// We get the Rectangle of our DrawPanel
			java.awt.Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.PRINTPADDING);
			// and create an iText specific Rectangle from (0,0) to (width,height) in which we draw the diagram
			Rectangle drawSpace = new Rectangle((float) bounds.getWidth(), (float) bounds.getHeight());

			// Create document in which we write the pdf
			Document document = new Document(drawSpace);
			PdfWriter writer = PdfWriter.getInstance(document, ostream);
			document.open();

			PdfContentByte cb = writer.getDirectContent();
			Graphics2D graphics2d = cb.createGraphics(drawSpace.getWidth(), drawSpace.getHeight());

			// We shift the diagram to the upper left corner, so we shift it by (minX,minY) of the contextBounds
			Dimension trans = new Dimension((int) bounds.getMinX(), (int) bounds.getMinY());
			graphics2d.translate(-trans.getWidth(), -trans.getHeight());

			handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d);

			graphics2d.dispose();
			document.close();

		} catch (Exception e) {
			System.out.println("UMLet: Error: Exception in outputPdf: " + e);
		}
	}

}
