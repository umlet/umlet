package com.umlet.control.diagram;

import java.io.File;

import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.gui.base.listeners.EntityListener;
import com.umlet.gui.base.listeners.PaletteEntityListener;
import com.umlet.gui.base.listeners.PaletteRelationListener;

public class PaletteHandler extends DiagramHandler {

	public PaletteHandler(File palettefile) {
		super(palettefile);
	}

	@Override
	public EntityListener getEntityListener(Entity e) {
		if (e instanceof Relation) return PaletteRelationListener.getInstance(this);
		return PaletteEntityListener.getInstance(this);
	}
}
