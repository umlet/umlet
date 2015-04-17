package com.baselet.gui.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.io.OutputHandler;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;

/** Copies and Pastes images to the system clipboard. Requires Java 2, v1.4. */
public class ClipBoard implements Transferable {

	private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private DiagramHandler copiedfrom;
	private List<GridElement> entities;

	private static ClipBoard _instance;

	public static ClipBoard getInstance() {
		if (_instance == null) {
			_instance = new ClipBoard();
		}
		return _instance;
	}

	private ClipBoard() {
		entities = new Vector<GridElement>();
	}

	public void copy(Vector<GridElement> entities, DiagramHandler handler) {
		copiedfrom = handler;
		this.entities = new Vector<GridElement>(entities);
		// clipboard zooms entities to 100% (to make them zoom-independent)
		DiagramHandler.zoomEntities(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, this.entities);
		CurrentGui.getInstance().getGui().enablePasteMenuEntry();

		if (clipboard != null) { // Issue 230: copy after zooming the entities
			clipboard.setContents(this, null);
		}
	}

	public List<GridElement> paste() {
		return entities;
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
		if (!isDataFlavorSupported(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		return ClipBoard.createImageForClipboard(copiedfrom, entities);
	}

	private static BufferedImage createImageForClipboard(DiagramHandler handler, Collection<GridElement> entities) {

		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false); // Zoom to the defaultGridsize before execution

		if (entities.isEmpty()) {
			entities = handler.getDrawPanel().getGridElements();
		}
		BufferedImage returnImg = OutputHandler.createImageForGridElements(entities);

		handler.setGridAndZoom(oldZoom, false); // Zoom back to the oldGridsize after execution

		return returnImg;
	}

}
