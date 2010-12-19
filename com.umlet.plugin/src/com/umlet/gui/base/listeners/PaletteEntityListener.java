package com.umlet.gui.base.listeners;

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
		DiagramHandler palette = handler; // The actual handler is the selected palette!

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = d.getHandler().getGridSize();
		int oldZoomPalette = palette.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		d.getHandler().setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		palette.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		if (d != null) {
			Entity e = me.CloneFromMe();
			Command cmd;
			Point viewp = d.getScrollPanel().getViewport().getViewPosition();
			int upperLeftX = (int) (viewp.getX() - viewp.getX() % oldZoomDiagram);
			int upperLeftY = (int) (viewp.getY() - viewp.getY() % oldZoomDiagram);
			cmd = new AddEntity(e,
					handler.ensureValueIsOnGrid(upperLeftX / oldZoomDiagram * Constants.DEFAULTGRIDSIZE + Constants.PASTE_DISPLACEMENT),
					handler.ensureValueIsOnGrid(upperLeftY / oldZoomDiagram * Constants.DEFAULTGRIDSIZE + Constants.PASTE_DISPLACEMENT)
					);

			d.getHandler().getController().executeCommand(cmd);
			d.getSelector().singleSelect(e);
		}

		// After inserting the new entity we restore the old zoom level of both diagrams
		d.getHandler().setGridAndZoom(oldZoomDiagram, false);
		palette.setGridAndZoom(oldZoomPalette, false);

	}

}
