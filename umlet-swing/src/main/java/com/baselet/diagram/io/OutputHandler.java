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
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
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

	private OutputHandler() {
	} // private constructor to avoid instantiation

	public static void createAndOutputToFile(String extension, File file, DiagramHandler handler) throws Exception {
		OutputStream ostream = new FileOutputStream(file);
		createToStream(extension, ostream, handler);
		ostream.close();
		createToStreamForPages(extension, file, handler);
	}

	public static void createToStream(String extension, OutputStream ostream, DiagramHandler handler) throws Exception {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		// if some GridElements are selected, only export them
		Collection<GridElement> elementsToDraw = handler.getDrawPanel().getSelector().getSelectedElements();

		// if no page frame is defined, draw everything
		if (elementsToDraw.isEmpty()) {
			elementsToDraw = handler.getDrawPanel().getGridElements();
		}

		OutputHandler.exportToOutputStream(extension, ostream, elementsToDraw, handler.getFontHandler());

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution
	}

	private static void createToStreamForPages(String extension, File file, DiagramHandler handler) throws Exception {

		// Skip export of pages if something is selected
		if (!handler.getDrawPanel().getSelector().getSelectedElements().isEmpty()) {
			return;
		}

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		// File without extension
		String filenameWholeExport = file.getAbsolutePath().replaceFirst("[.][^.]+$", "");

		for (GridElement page : handler.getDrawPanel().getPages()) {
			OutputStream ostream = new FileOutputStream(new File(filenameWholeExport + "_" + page.getSetting("page") + "." + extension));
			Collection<GridElement> elementsToDraw = handler.getDrawPanel().getPageElements(page);
			OutputHandler.exportToOutputStream(extension, ostream, elementsToDraw, handler.getFontHandler());
			ostream.close();
		}

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
		setGraphicsBorders(bounds, graphics2d, 1);
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
		Integer scale = Config.getInstance().getExportScale();
		// #510 If DPI setting is used, try to export using correct DPI settings (for high dpi/retina displays) see https://stackoverflow.com/questions/321736/how-to-set-dpi-information-in-an-image/4833697
		boolean exportedWithDpi = exportImgAndSetDpi(imgType, ostream, entities, diagramFont, scale);
		if (!exportedWithDpi) { // no dpi setting is used or the format doesnt support the setting, use the simple one (at the moment only png seems to support it)
			ImageIO.write(createImageForGridElements(entities, diagramFont, scale), imgType, ostream);
		}
		ostream.flush();
		ostream.close();
	}

	private static boolean exportImgAndSetDpi(String imgType, OutputStream ostream, Collection<GridElement> entities, FontHandler diagramFont, Integer scale) throws IIOInvalidTreeException, IOException {
		Integer dpi = Config.getInstance().getExportDpi();
		if (dpi != null) {
			for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(imgType); iw.hasNext();) {
				ImageWriter writer = iw.next();
				ImageWriteParam writeParam = writer.getDefaultWriteParam();
				ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
				IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
				if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
					continue;
				}
				setImgDPI(dpi, metadata);

				final ImageOutputStream stream = ImageIO.createImageOutputStream(ostream);
				try {
					writer.setOutput(stream);
					writer.write(metadata, new IIOImage(createImageForGridElements(entities, diagramFont, scale), null, metadata), writeParam);
					return true;
				} finally {
					stream.close();
				}
			}
		}
		return false;
	}

	private static void setImgDPI(Integer dpi, IIOMetadata metadata) throws IIOInvalidTreeException {
		final double INCH_2_CM = 2.54;

		double dotsPerMilli = 1.0 * dpi / 10 / INCH_2_CM;

		IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
		horiz.setAttribute("value", Double.toString(dotsPerMilli));

		IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
		vert.setAttribute("value", Double.toString(dotsPerMilli));

		IIOMetadataNode dim = new IIOMetadataNode("Dimension");
		dim.appendChild(horiz);
		dim.appendChild(vert);

		IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
		root.appendChild(dim);

		metadata.mergeTree("javax_imageio_1.0", root);
	}

	public static BufferedImage createImageForGridElements(Collection<GridElement> entities, FontHandler diagramFont, int scale) {

		Rectangle bounds = DrawPanel.getContentBounds(Config.getInstance().getPrintPadding(), entities);
		BufferedImage im = new BufferedImage(bounds.width == 0 ? 1 : bounds.width * scale, bounds.height == 0 ? 1 : bounds.height * scale, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = im.createGraphics();
		graphics2d.setRenderingHints(Utils.getUxRenderingQualityHigh(true));

		setGraphicsBorders(bounds, graphics2d, scale);
		graphics2d.scale(scale, scale);
		paintEntitiesIntoGraphics2D(graphics2d, entities, diagramFont);
		graphics2d.dispose();

		return im;
	}

	private static void setGraphicsBorders(Rectangle bounds, Graphics2D graphics2d, int scale) {
		graphics2d.translate(-bounds.x * scale, -bounds.y * scale);
		graphics2d.clipRect(bounds.x * scale, bounds.y * scale, bounds.width * scale, bounds.height * scale);
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(bounds.x * scale, bounds.y * scale, bounds.width * scale, bounds.height * scale);
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
