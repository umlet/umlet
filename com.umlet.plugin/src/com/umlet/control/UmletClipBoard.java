// The UMLet source code is distributed under the terms of the GPL; see license.txtpackage com.umlet.control;
package com.umlet.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Vector;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.io.GenPic;
import com.umlet.element.base.Entity;

/** Copies and Pastes images to the system clipboard. Requires Java 2, v1.4. */
public class UmletClipBoard implements Transferable {

	private Clipboard clipboard;
	private DiagramHandler copiedfrom;
	private Vector<Entity> entities;

	static UmletClipBoard _instance;

	public static UmletClipBoard getInstance() {
		if (_instance == null) {
			_instance = new UmletClipBoard();
		}
		return _instance;
	}

	private UmletClipBoard() {
		String s = System.getProperty("java.version");
		this.entities = new Vector<Entity>();
		if (Float.parseFloat(s.substring(0, 3)) < 1.4) { // LME: 1.4+
			// System.out.println ("This plugin will only run using Java 2, v1.4");
			return;
		}

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		clipboard = toolkit.getSystemClipboard();
	}

	public DiagramHandler copiedFrom() {
		return this.copiedfrom;
	}

	public void copy(Vector<Entity> entities, DiagramHandler handler) {
		this.copiedfrom = handler;
		this.entities.clear();
		this.entities = entities;
		if (entities.size() > 0) Umlet.getInstance().getGUI().enablePaste();
		// System.out.println(this.entities.size() + " copied.");
		if (clipboard != null) clipboard.setContents(this, null);
	}

	public Vector<Entity> paste() {
		// System.out.println(this.entities.size() + " pasted.");
		return this.entities;
	}

	// public void moveBy(int diffx, int diffy) {
	// Vector<Command> moveCommands = new Vector<Command>();
	// for (Entity e : entities) {
	// moveCommands.add(new Move(e, diffx, diffy));
	// }
	// copiedfrom.getController().executeCommand(new Macro(moveCommands));
	// }

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);

		DiagramHandler d = Umlet.getInstance().getDiagramHandler();
		if (d == null) return null;
		return GenPic.getInstance().getImageFromDiagram(d);
	}

	/*
	 * public Insets getInsets() {
	 * Insets i= super.getInsets();
	 * return new Insets(i.top+5, i.left+40, i.bottom+5, i.right+40);
	 * }
	 */
}
