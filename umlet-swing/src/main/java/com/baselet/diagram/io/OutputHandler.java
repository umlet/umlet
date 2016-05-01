package com.baselet.diagram.io;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;

import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.Config;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.FontHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.interfaces.GridElement;
import com.itextpdf.awt.FontMapper;
import com.itextpdf.awt.PdfGraphics2D;
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
		Collection<GridElement> elementsToDraw = handler.getDrawPanel().getSelector().getSelectedElements();
		// if nothing is selected, draw everything
		if (elementsToDraw.isEmpty()) {
			elementsToDraw = handler.getDrawPanel().getGridElements();
		}

		OutputHandler.exportToOutputStream(extension, ostream, elementsToDraw, handler.getFontHandler());

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution
	}

	private static void exportToOutputStream(String extension, OutputStream ostream, Collection<GridElement> entities, FontHandler diagramFont) throws IOException {
		for (GridElement ge : entities) {
			ge.getDeprecatedAddons().doBeforeExport();
		}
		if (extension.equals("eps")) {
			exportEps(ostream, entities, diagramFont);
		}
		else if (extension.equals("pdf")) {
			exportPdf(ostream, entities, diagramFont);
		}
		else if (extension.equals("svg")) {
			exportSvg(ostream, entities, diagramFont);
		}
		else if (isImageExtension(extension)) {
			exportImg(extension, ostream, entities, diagramFont);
		}
		else {
			throw new IllegalArgumentException(extension + " is an invalid format");
		}
	}

	private static void exportEps(OutputStream ostream, Collection<GridElement> entities, FontHandler diagramFont) throws IOException {
		Rectangle bounds = DrawPanel.getContentBounds(Config.getInstance().getPrintPadding(), entities);
		EpsGraphics2D graphics2d = new EpsGraphics2D(Program.getInstance().getProgramName() + " Diagram", ostream, 0, 0, bounds.width, bounds.height);
		setGraphicsBorders(bounds, graphics2d);
		paintEntitiesIntoGraphics2D(graphics2d, entities, diagramFont);
		graphics2d.flush();
		graphics2d.close();
	}

	private static void exportPdf(OutputStream ostream, Collection<GridElement> entities, FontHandler diagramFont) throws IOException {
		try {
			FontMapper mapper = new PdfFontMapper();

			Rectangle bounds = DrawPanel.getContentBounds(Config.getInstance().getPrintPadding(), entities);
			com.itextpdf.text.Document document = new com.itextpdf.text.Document(new com.itextpdf.text.Rectangle(bounds.getWidth(), bounds.getHeight()));
			PdfWriter writer = PdfWriter.getInstance(document, ostream);
			document.open();

			Graphics2D graphics2d = new PdfGraphics2D(writer.getDirectContent(), bounds.getWidth(), bounds.getHeight(), mapper);

			// We shift the diagram to the upper left corner, so we shift it by (minX,minY) of the contextBounds
			Dimension trans = new Dimension(bounds.getX(), bounds.getY());
			graphics2d.translate(-trans.getWidth(), -trans.getHeight());

			paintEntitiesIntoGraphics2D(graphics2d, entities, diagramFont);
			graphics2d.dispose();
			document.close();
		} catch (com.itextpdf.text.DocumentException e) {
			throw new IOException(e.getMessage());
		}
	}

	private static void exportSvg(OutputStream ostream, Collection<GridElement> entities, FontHandler diagramFont) throws IOException {
		Rectangle bounds = DrawPanel.getContentBounds(Config.getInstance().getPrintPadding(), entities);
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);

		SVGGraphics2D graphics2d = new SVGGraphics2D(document);
		graphics2d.setSVGCanvasSize(Converter.convert(bounds.getSize()));
		paintEntitiesIntoGraphics2D(graphics2d, entities, diagramFont);

		Element root = graphics2d.getRoot();
		root.setAttributeNS(null, "viewBox", String.format("%d %d %d %d", bounds.x, bounds.y, bounds.width, bounds.height));
		Writer out = new OutputStreamWriter(ostream, "UTF-8"); // Stream out SVG to the standard output using UTF-8 character to byte encoding
		graphics2d.stream(root, out, false, false);
		graphics2d.dispose();
	}

	private static void exportImg(String imgType, OutputStream ostream, Collection<GridElement> entities, FontHandler diagramFont) throws IOException {
		ImageIO.write(createImageForGridElements(entities, diagramFont), imgType, ostream);
		ostream.flush();
		ostream.close();
	}

	public static BufferedImage createImageForGridElements(Collection<GridElement> entities, FontHandler diagramFont) {

		Rectangle bounds = DrawPanel.getContentBounds(Config.getInstance().getPrintPadding(), entities);
		BufferedImage im = new BufferedImage(bounds.width == 0 ? 1 : bounds.width, bounds.height == 0 ? 1 : bounds.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = im.createGraphics();
		graphics2d.setRenderingHints(Utils.getUxRenderingQualityHigh(true));

		setGraphicsBorders(bounds, graphics2d);
		paintEntitiesIntoGraphics2D(graphics2d, entities, diagramFont);
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

	public static void paintEntitiesIntoGraphics2D(Graphics2D g2d, Collection<GridElement> entities, FontHandler diagramFont) {
		DiagramHandler handler = DiagramHandler.forExport(diagramFont); // #290: pass fontHandler from original diagramHandler to let the export use diagram specific fontsize+family
		JLayeredPane tempPanel = new JLayeredPane();
		for (GridElement entity : entities) {
			GridElement clone = ElementFactorySwing.createCopy(entity, handler);
			com.baselet.element.interfaces.Component component = clone.getComponent();
			// Issue 138: when PDF and Swing Export draw on (0,0) a part of the drawn image is cut, therefore it's displaced by 0.5px in that case.
			// also Issue 270: makes arrow ending placement better
			component.translateForExport();
			tempPanel.add((Component) component, clone.getLayer());
		}
		tempPanel.validate();
		tempPanel.setBackground(Color.WHITE);
		tempPanel.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		tempPanel.update(g2d);
	}
}
