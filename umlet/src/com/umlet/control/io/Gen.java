package com.umlet.control.io;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Gen {

	private Gen() {} //private constructor to avoid instantiation

	public static void createAndOutputToFile(String extension, String filename, DiagramHandler handler) throws Exception {
		OutputStream ostream = new FileOutputStream(filename);
		createToStream(extension, ostream, handler);
	}

	public static void createToStream(String extension, OutputStream ostream, DiagramHandler handler) throws Exception {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		Vector<Entity> entities = handler.getDrawPanel().getSelector().getSelectedEntities();
		if (entities.isEmpty()) entities = handler.getDrawPanel().getAllEntities();
		GenImpl.exportToOutputStream(extension, ostream, handler, entities);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution
	}

	public static BufferedImage createImageForClipboard(DiagramHandler handler, Vector<Entity> entities) {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		if (entities.isEmpty()) entities = handler.getDrawPanel().getAllEntities();
		BufferedImage returnImg = GenImpl.getImageFromDiagram(handler, entities);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution

		return returnImg;
	}

}
