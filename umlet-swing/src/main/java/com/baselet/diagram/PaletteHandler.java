package com.baselet.diagram;

import java.io.File;

import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.listener.GridElementListener;
import com.baselet.gui.listener.PaletteEntityListener;

public class PaletteHandler extends DiagramHandler {

	public PaletteHandler(File palettefile) {
		super(palettefile);
	}

	@Override
	public GridElementListener getEntityListener(GridElement e) {
		return PaletteEntityListener.getInstance(this);
	}

	@Override
	protected void initDiagramPopupMenu(boolean extendedPopupMenu) {
		/* no diagram popup menu */
	}

	@Override
	protected DrawPanel createDrawPanel() {
		return new DrawPanel(this, false); /* no startup and filedrop */
	}
}
