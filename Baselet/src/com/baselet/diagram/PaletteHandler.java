package com.baselet.diagram;

import java.io.File;

import com.baselet.element.GridElement;
import com.baselet.gui.base.listener.EntityListener;
import com.baselet.gui.base.listener.PaletteEntityListener;
import com.baselet.gui.base.listener.PaletteRelationListener;
import com.umlet.element.Relation;


public class PaletteHandler extends DiagramHandler {

	public PaletteHandler(File palettefile) {
		super(palettefile);
	}

	@Override
	public EntityListener getEntityListener(GridElement e) {
		if (e instanceof Relation) return PaletteRelationListener.getInstance(this);
		return PaletteEntityListener.getInstance(this);
	}
}
