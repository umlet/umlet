package com.baselet.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Vector;

import com.baselet.control.Constants.SystemInfo;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.io.OutputHandler;
import com.baselet.element.GridElement;

/** Copies and Pastes images to the system clipboard. Requires Java 2, v1.4. */
public class ClipBoard implements Transferable {

	private Clipboard clipboard;
	private DiagramHandler copiedfrom;
	private Vector<GridElement> entities;

	public static ClipBoard _instance;

	public static ClipBoard getInstance() {
		if (_instance == null) {
			_instance = new ClipBoard();
		}
		return _instance;
	}

	private ClipBoard() {
		this.entities = new Vector<GridElement>();
		if (Float.parseFloat(SystemInfo.JAVA_VERSION) < 1.4) return;
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public DiagramHandler copiedFrom() {
		return this.copiedfrom;
	}

	public void copy(Vector<GridElement> entities, DiagramHandler handler) {
		this.copiedfrom = handler;
		this.entities = new Vector<GridElement>(entities);
		if (clipboard != null) clipboard.setContents(this, null);
		// AB: clipboard zooms entities to 100%
		// NOTE has to be done here because it doesn't fit with cut/copy and GenPic.getImageFromDiagram otherwise)
		DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, this.entities);
		Main.getInstance().getGUI().setPaste(true);
	}

	public Vector<GridElement> paste() {
		return this.entities;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
		return OutputHandler.createImageForClipboard(copiedfrom, entities);
	}

}
