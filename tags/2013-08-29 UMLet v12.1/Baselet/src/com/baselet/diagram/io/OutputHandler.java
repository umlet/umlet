package com.baselet.diagram.io;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

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
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.swing.Converter;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.FontMapper;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;


public class OutputHandler {

	private OutputHandler() {} // private constructor to avoid instantiation

	public static void createAndOutputToFile(String extension, File file, DiagramHandler handler) throws Exception {
		OutputStream ostream = new FileOutputStream(file);
		createToStream(extension, ostream, handler);
		ostream.close();
	}

	public static void createToStream(String extension, OutputStream ostream, DiagramHandler handler) throws Exception {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		// if some GridElements are selected, only export them
		Collection<GridElement> elementsToDraw = degroupElements(handler.getDrawPanel().getSelector().getSelectedElements());
		// if nothing is selected, draw everything
		if (elementsToDraw.isEmpty()) {
			elementsToDraw = handler.getDrawPanel().getAllEntitiesNotInGroup();
		}

		OutputHandler.exportToOutputStream(extension, ostream, handler, elementsToDraw);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution
	}

	/**
	 * helper method to degroup elements (because a paint-call on a group does not paint every contained member element)
	 */
	private static Collection<GridElement> degroupElements(Collection<GridElement> selectedEntities) {
		Collection<GridElement> resultCol = new ArrayList<GridElement>();
		for (GridElement selectedElement : selectedEntities) {
			if (selectedElement instanceof Group) {
				// if it's a top group, add all members recursively (groups can be part of groups)
				if (!selectedElement.isPartOfGroup()) resultCol.addAll(((Group) selectedElement).getMembersRecursive());
			}
			else resultCol.add(selectedElement);
		}
		return resultCol;
	}

	public static BufferedImage createImageForClipboard(DiagramHandler handler, Collection<GridElement> entities) {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		Collection<GridElement> elementsToDraw = degroupElements(entities);
		if (elementsToDraw.isEmpty()) {
			elementsToDraw = handler.getDrawPanel().getAllEntities();
		}
		BufferedImage returnImg = OutputHandler.getImageFromDiagram(handler, elementsToDraw);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution

		return returnImg;
	}

	private static void exportToOutputStream(String extension, OutputStream ostream, DiagramHandler handler, Collection<GridElement> entities) throws IOException {
		if (extension.equals("eps")) exportEps(ostream, handler, entities);
		else if (extension.equals("pdf")) exportPdf(ostream, handler, entities);
		else if (extension.equals("svg")) exportSvg(ostream, handler, entities);
		else if (isImageExtension(extension)) exportImg(extension, ostream, handler, entities);
		else throw new IllegalArgumentException(extension + " is an invalid format");
	}

	private static void exportEps(OutputStream ostream, DiagramHandler handler, Collection<GridElement> entities) throws IOException {
		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
		EpsGraphics2D graphics2d = new EpsGraphics2D(Program.PROGRAM_NAME + " Diagram", ostream, 0, 0, bounds.width, bounds.height);
		setGraphicsBorders(bounds, graphics2d);
		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);
		graphics2d.flush();
		graphics2d.close();
	}

	private static void exportPdf(OutputStream ostream, DiagramHandler handler, Collection<GridElement> entities) throws IOException {
		try {
			// use special font if defined in properties
			FontMapper mapper = new FontMapper() {
				@Override
				public BaseFont awtToPdf(Font font) {
					try {
						return BaseFont.createFont(Constants.pdfExportFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					} catch (Exception e) {
						return null;
					}
				}
				@Override
				public Font pdfToAwt(BaseFont font, int size) {
					return null;
				}
			};
			if (mapper.awtToPdf(null) == null) mapper = new DefaultFontMapper();

			Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
			com.itextpdf.text.Document document = new com.itextpdf.text.Document(new com.itextpdf.text.Rectangle(bounds.getWidth(), bounds.getHeight()));
			PdfWriter writer = PdfWriter.getInstance(document, ostream);
			document.open();

			Graphics2D graphics2d = new PdfGraphics2D(writer.getDirectContent(), bounds.getWidth(), bounds.getHeight(), mapper);

			// We shift the diagram to the upper left corner, so we shift it by (minX,minY) of the contextBounds
			Dimension trans = new Dimension(bounds.getX(), bounds.getY());
			graphics2d.translate(-trans.getWidth(), -trans.getHeight());

			handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);
			graphics2d.dispose();
			document.close();
		} catch (com.itextpdf.text.DocumentException e) {
			throw new IOException(e.getMessage());
		}
	}

	private static void exportSvg(OutputStream ostream, DiagramHandler handler, Collection<GridElement> entities) throws IOException {
		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);

		SVGGraphics2D graphics2d = new SVGGraphics2D(document);
		graphics2d.setSVGCanvasSize(Converter.convert(bounds.getSize()));
		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);

		Element root = graphics2d.getRoot();
		root.setAttributeNS(null, "viewBox", String.format("%d %d %d %d", bounds.x, bounds.y, bounds.width, bounds.height));
		Writer out = new OutputStreamWriter(ostream, "UTF-8"); // Stream out SVG to the standard output using UTF-8 character to byte encoding
		graphics2d.stream(root, out, false, false);
		graphics2d.dispose();
	}

	private static void exportImg(String imgType, OutputStream ostream, DiagramHandler handler, Collection<GridElement> entities) throws IOException {
		ImageIO.write(getImageFromDiagram(handler, entities), imgType, ostream);
		ostream.flush();
		ostream.close();
	}

	private static BufferedImage getImageFromDiagram(DiagramHandler handler, Collection<GridElement> entities) {

		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.printPadding, entities);
		BufferedImage im = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = im.createGraphics();
		graphics2d.setRenderingHints(Utils.getUxRenderingQualityHigh(true));

		setGraphicsBorders(bounds, graphics2d);
		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d, entities);
		graphics2d.dispose();

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
