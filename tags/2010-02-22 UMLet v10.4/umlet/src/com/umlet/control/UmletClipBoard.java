// The UMLet source code is distributed under the terms of the GPL; see license.txtpackage com.umlet.control;
package com.umlet.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.io.Gen;
import com.umlet.element.base.Entity;

/** Copies and Pastes images to the system clipboard. Requires Java 2, v1.4. */
public class UmletClipBoard implements Transferable {

	private final static Logger log = Logger.getLogger(UmletClipBoard.class);		
	
	private Clipboard clipboard;
	private DiagramHandler copiedfrom;
	private Vector<Entity> entities;

	public static UmletClipBoard _instance;

	public static UmletClipBoard getInstance() {
		if (_instance == null) {
			_instance = new UmletClipBoard();
		}
		return _instance;
	}

	private UmletClipBoard() {
		this.entities = new Vector<Entity>();
		if (Float.parseFloat(Constants.JAVAVERSION) < 1.4) return;
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public DiagramHandler copiedFrom() {
		return this.copiedfrom;
	}

	public void copy(Vector<Entity> entities, DiagramHandler handler) {
		this.copiedfrom = handler;
		this.entities = new Vector<Entity>(entities);
		if (clipboard != null) clipboard.setContents(this, null);
		//AB: clipboard zooms entities to 100% 
		//NOTE has to be done here because it doesn't fit with cut/copy and GenPic.getImageFromDiagram otherwise)
		DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, this.entities);
		Umlet.getInstance().getGUI().setPaste(true);
	}

	public Vector<Entity> paste() {
		return this.entities;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
		return Gen.createImageForClipboard(copiedfrom, entities);
	}

}
