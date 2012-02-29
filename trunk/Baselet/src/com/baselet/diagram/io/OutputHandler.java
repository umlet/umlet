package com.baselet.diagram.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.FontMapper;


public class OutputHandler {

	private OutputHandler() {} // private constructor to avoid instantiation

	public static void createAndOutputToFile(String extension, File file, DiagramHandler handler) throws Exception {
		OutputStream ostream = new FileOutputStream(file);
		createToStream(extension, ostream, handler);
	}

	public static void createToStream(String extension, OutputStream ostream, DiagramHandler handler) throws Exception {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		Vector<GridElement> entities = handler.getDrawPanel().getSelector().getSelectedEntities();
		if (entities.isEmpty()) entities = handler.getDrawPanel().getAllEntities();
		OutputHandler.exportToOutputStream(extension, ostream, handler, entities);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution
	}

	public static BufferedImage createImageForClipboard(DiagramHandler handler, Vector<GridElement> entities) {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		if (entities.isEmpty()) entities = handler.getDrawPanel().getAllEntities();
		BufferedImage returnImg = OutputHandler.getImageFromDiagram(handler, entities);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution

		return returnImg;
	}

	private static void exportToOutputStream(String extension, OutputStream ostream, DiagramHandler handler, Vector<GridElement> entities) throws IOException {
		if (extension.equals("eps")) exportEps(ostream, handler, entities);
		else if (extension.equals("pdf")) exportPdf(ostream, handler, entities);
		else if (extension.equals("svg")) exportSvg(ostream, handler, entities);
		else if (isImageExtension(extension)) exportImg(extension, ostream, handler, entities);
		else throw new IllegalArgumentException(extension + " is an invalid format");
	}

	private static void exportEps(OutputStream ostream, DiagramHandler handler, Vector<GridElement> entities) throws IOException {
		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
		EpsGraphics2D graphics2d = new EpsGraphics2D(Program.PROGRAM_NAME + " Diagram", ostream, 0, 0, bounds.width, bounds.height);
		setGraphicsBorders(bounds, graphics2d);
		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);
		graphics2d.flush();
		graphics2d.close();
	}

	private static void exportPdf(OutputStream ostream, DiagramHandler handler, Vector<GridElement> entities) throws IOException {
		try {
			Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
			com.itextpdf.text.Rectangle drawSpace = new com.itextpdf.text.Rectangle((float) bounds.getWidth(), (float) bounds.getHeight());

			com.itextpdf.text.Document document = new com.itextpdf.text.Document(drawSpace);
			com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, ostream);

			document.open();

			FontMapper mapper = new FontMapper() {
				@Override
				public BaseFont awtToPdf(Font font) {
					try {
						return BaseFont.createFont(Constants.pdfExportFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					} catch (Exception e) {/*is handled later by returning null*/}
					return null;
				}
				@Override
				public Font pdfToAwt(BaseFont font, int size) {
					return null;
				}
			};
			
			if (mapper.awtToPdf(null) == null) mapper = new DefaultFontMapper();

			Graphics2D graphics2d = writer.getDirectContent().createGraphics(drawSpace.getWidth(), drawSpace.getHeight(), mapper);

			// We shift the diagram to the upper left corner, so we shift it by (minX,minY) of the contextBounds
			Dimension trans = new Dimension((int) bounds.getMinX(), (int) bounds.getMinY());
			graphics2d.translate(-trans.getWidth(), -trans.getHeight());

			handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);
			graphics2d.dispose();
			document.close();
		} catch (com.itextpdf.text.DocumentException e) {
			throw new IOException(e.getMessage());
		}
	}

	private static void exportSvg(OutputStream ostream, DiagramHandler handler, Vector<GridElement> entities) throws IOException {
		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);

		SVGGraphics2D graphics2d = new SVGGraphics2D(document);
		graphics2d.setSVGCanvasSize(bounds.getSize());
		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);

		Element root = graphics2d.getRoot();
		root.setAttributeNS(null, "viewBox", String.format("%d %d %d %d", bounds.x, bounds.y, bounds.width, bounds.height));
		Writer out = new OutputStreamWriter(ostream, "UTF-8"); // Stream out SVG to the standard output using UTF-8 character to byte encoding
		graphics2d.stream(root, out, false, false);
	}

	private static void exportImg(String imgType, OutputStream ostream, DiagramHandler handler, Vector<GridElement> entities) throws IOException {
		ImageIO.write(getImageFromDiagram(handler, entities), imgType, ostream);
		ostream.flush();
		ostream.close();
	}

	private static BufferedImage getImageFromDiagram(DiagramHandler handler, Vector<GridElement> entities) {

		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
		BufferedImage im = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = im.createGraphics();
		graphics2d.setRenderingHints(Utils.getUxRenderingQualityHigh(true));

		setGraphicsBorders(bounds, graphics2d);
		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);

		return im;
	}

	private static void setGraphicsBorders(Rectangle bounds, Graphics2D graphics2d) {
		graphics2d.translate(-bounds.x, -bounds.y);
		graphics2d.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	private static boolean isImageExtension(String ext) {
		return ImageIO.getImageWritersBySuffix(ext).hasNext();
	}

}
