package com.baselet.gui.standalone;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.FontHandler;
import com.baselet.diagram.command.AddElement;
import com.baselet.element.GridElement;
import com.umlet.element.ClassDiagramConverter;

public class FileDropListener implements FileDrop.Listener {

	private static Logger log = Logger.getLogger(Utils.getClassName());

	@Override
	public void filesDropped(File[] files) {
		for (int i = 0; i < files.length; i++) {
			try {
				String filename = files[i].getCanonicalPath();
				if (isJavaFile(filename)) { 
					createClassDiagram(filename);
				} else {
					Main.getInstance().doOpen(filename);
				}
			} catch (IOException e) {
				log.error("Cannot open file dropped");
			}
		}
	}
	
	private boolean isJavaFile(String filename) {
		int dotPosition = filename.lastIndexOf(".");
		String extension = filename.substring(dotPosition + 1, filename.length()); 
		if (extension.equals("class") || extension.equals("java")) return true;
		return false;
	}

	private void createClassDiagram(String filename) {
		DiagramHandler handler = Main.getInstance().getCurrentInfoDiagramHandler();

		int offsetX = handler.getDrawPanel().getOriginAtDefaultZoom().x * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;
		int offsetY = handler.getDrawPanel().getOriginAtDefaultZoom().y * handler.getGridSize() / Constants.DEFAULTGRIDSIZE;

		ClassDiagramConverter converter = new ClassDiagramConverter();
		GridElement clazz = converter.createElement(filename);
		
		new AddElement(clazz, 
				handler.realignToGrid(clazz.getLocation().x + offsetX),
				handler.realignToGrid(clazz.getLocation().y + offsetY), false).execute(handler);
		
		converter.adjustSize(clazz);
		
		handler.setChanged(true);		
	}
}
