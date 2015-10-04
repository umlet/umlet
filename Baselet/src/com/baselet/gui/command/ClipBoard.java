package com.baselet.gui.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

import com.baselet.control.constants.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.io.OutputHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;

/** Copies and Pastes images to the system clipboard. Requires Java 2, v1.4. */
public class ClipBoard implements Transferable {

	private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private List<GridElement> entities;

	private static ClipBoard _instance = new ClipBoard();

	public static ClipBoard getInstance() {
		return _instance;
	}

	public void copyAndZoomToDefaultLevel(List<GridElement> entities, DiagramHandler handler) {
		if (entities.isEmpty()) {
			return;
		}
		// clipboard zooms entities to 100% (to make them zoom-independent)
		// therefore we need to set a DiagramHandler with 100% zoom and resize the elements
		this.entities = new ArrayList<GridElement>(entities.size());
		DiagramHandler dhNew = new DiagramHandler(null);
		for (GridElement entitiy : entities) {
			this.entities.add(ElementFactorySwing.createCopy(entitiy, dhNew));
		}
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
		return OutputHandler.createImageForGridElements(entities, null);
	}

}
