package com.baselet.diagram;

import java.io.File;

import com.baselet.element.GridElement;
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
}
