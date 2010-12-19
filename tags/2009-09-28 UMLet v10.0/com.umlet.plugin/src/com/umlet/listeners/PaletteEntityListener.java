package com.umlet.listeners;

import java.awt.Point;
import java.util.HashMap;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.AddEntity;
import com.umlet.control.command.Command;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;

public class PaletteEntityListener extends EntityListener {

	private static HashMap<DiagramHandler, PaletteEntityListener> entitylistener = new HashMap<DiagramHandler, PaletteEntityListener>();

	public static PaletteEntityListener getInstance(DiagramHandler handler) {
		if (!entitylistener.containsKey(handler)) entitylistener.put(handler, new PaletteEntityListener(handler));
		return entitylistener.get(handler);
	}

	protected PaletteEntityListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mouseDoubleClicked(Entity me) {
		DrawPanel d = Umlet.getInstance().getGUI().getCurrentDiagram();
		DiagramHandler palette = Umlet.getInstance().getDiagramHandler();

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = d.getHandler().getGridSize();
		int oldZoomPalette = palette.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		d.getHandler().setGridAndZoom(Constants.defaultGridSize);
		palette.setGridAndZoom(Constants.defaultGridSize);

		if (d != null) {
			Entity e = me.CloneFromMe();
			Command cmd;
			int MAIN_UNIT = Umlet.getInstance().getDiagramHandler().getGridSize();
			Point viewp = d.getScrollPanel().getViewport().getViewPosition();
			cmd = new AddEntity(e, viewp.x - (viewp.x % MAIN_UNIT)
					+ MAIN_UNIT * 2, viewp.y - (viewp.y % MAIN_UNIT)
					+ MAIN_UNIT * 2);

			d.getHandler().getController().executeCommand(cmd);
			d.getSelector().singleSelect(e);
		}

		// After inserting the new entity we restore the old zoom level of both diagrams
		d.getHandler().setGridAndZoom(oldZoomDiagram);
		palette.setGridAndZoom(oldZoomPalette);

	}

}
