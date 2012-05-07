package com.baselet.gui.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.command.AddElement;
import com.baselet.diagram.command.Command;
import com.baselet.element.GridElement;

public class PaletteEntityListener extends GridElementListener {

	Map<GridElement, Point> previousDraggingLocation;

	private static HashMap<DiagramHandler, PaletteEntityListener> entitylistener = new HashMap<DiagramHandler, PaletteEntityListener>();
	private Vector<GridElement> copiedEntities;

	public static PaletteEntityListener getInstance(DiagramHandler handler) {
		if (!entitylistener.containsKey(handler)) entitylistener.put(handler, new PaletteEntityListener(handler));
		return entitylistener.get(handler);
	}

	protected PaletteEntityListener(DiagramHandler handler) {
		super(handler);
		previousDraggingLocation = new Hashtable<GridElement, Point>();
		copiedEntities = new Vector<GridElement>();
	}

	@Override
	public void mouseDoubleClicked(GridElement me) {
		copyEntity(me);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		Vector<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedEntities();
		for (GridElement currentEntity : selectedEntities) {
			currentEntity.setStickingBorderActive(false);
			if (this.IS_DRAGGING) previousDraggingLocation.put(currentEntity, currentEntity.getLocation());
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		GridElement entity = (GridElement) me.getComponent();

		if (IS_DRAGGED_FROM_PALETTE) {
			moveDraggedEntities();
		}
		else {
			if (entity.getLocationOnScreen().x + entity.getWidth() <= handler.getDrawPanel().getLocationOnScreen().x) {
				resetEntities();
				insertDraggedEntities(me);
				handler.getDrawPanel().getSelector().deselectAllWithoutUpdatePropertyPanel();
			}
		}
	}

	private void moveDraggedEntities() {
		for (GridElement copiedEntity : copiedEntities) {
			int x = this.getNewCoordinate().x - this.getOldCoordinate().x;
			int y = this.getNewCoordinate().y - this.getOldCoordinate().y;
			x = Main.getInstance().getDiagramHandler().realignToGrid(false, x);
			y = Main.getInstance().getDiagramHandler().realignToGrid(false, y);
			copiedEntity.changeLocation(x, y);
		}
	}

	private void resetEntities() {
		Vector<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedEntities();
		for (GridElement currentEntity : selectedEntities) {
			Point previousLocation = previousDraggingLocation.get(currentEntity);
			currentEntity.setStickingBorderActive(true);
			currentEntity.setLocation(previousLocation.x, previousLocation.y);
		}
	}

	private void insertDraggedEntities(MouseEvent me) {
		GridElement entity = (GridElement) me.getComponent();
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();
		Vector<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedEntities();

		if (!allowCopyEntity()) return;

		copiedEntities.clear();

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = currentDiagram.getHandler().getGridSize();
		int oldZoomPalette = handler.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		currentDiagram.getHandler().setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		this.IS_DRAGGING = false;
		this.IS_DRAGGED_FROM_PALETTE = true;

		for (GridElement currentEntity : selectedEntities) {
			GridElement copiedEntity = copyEntity(currentEntity);
			copiedEntities.add(copiedEntity);
			int x = (currentEntity.getX() - entity.getX());
			int y = (currentEntity.getY() - entity.getY());
			x -= (entity.getWidth() / 2);
			y -= (entity.getHeight() / 2);
			copiedEntity.setLocation(x, y);
		}

		// After inserting the new entity we restore the old zoom level of both diagrams
		currentDiagram.getHandler().setGridAndZoom(oldZoomDiagram, false);
		handler.setGridAndZoom(oldZoomPalette, false);

		// set entity positions relative to current mouse pointer location on screen
		updateEntityPositions(me);
	}

	private void updateEntityPositions(MouseEvent me) {
		DiagramHandler currentHandler = Main.getInstance().getDiagramHandler();
		Point mousePosition = me.getLocationOnScreen();
		int mouseX = mousePosition.x - currentHandler.getDrawPanel().getLocationOnScreen().x;
		int mouseY = mousePosition.y - currentHandler.getDrawPanel().getLocationOnScreen().y;
		for (GridElement copiedEntity : copiedEntities) {
			int x = copiedEntity.getLocation().x;
			int y = copiedEntity.getLocation().y;
			x += mouseX;
			y += mouseY;
			x = Main.getInstance().getDiagramHandler().realignToGrid(false, x);
			y = Main.getInstance().getDiagramHandler().realignToGrid(false, y);
			copiedEntity.setLocation(x, y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	protected boolean allowCopyEntity() {
		return true;
	}

	protected GridElement copyEntity(GridElement me) {
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = currentDiagram.getHandler().getGridSize();
		int oldZoomPalette = handler.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		currentDiagram.getHandler().setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		GridElement e = me.CloneFromMe();

		Command cmd;
		Point viewp = currentDiagram.getScrollPane().getViewport().getViewPosition();
		int upperLeftX = (int) (viewp.getX() - viewp.getX() % oldZoomDiagram);
		int upperLeftY = (int) (viewp.getY() - viewp.getY() % oldZoomDiagram);
		cmd = new AddElement(e,
				handler.realignToGrid(((upperLeftX / oldZoomDiagram) + Constants.PASTE_DISPLACEMENT_GRIDS) * Constants.DEFAULTGRIDSIZE),
				handler.realignToGrid(((upperLeftY / oldZoomDiagram) + Constants.PASTE_DISPLACEMENT_GRIDS) * Constants.DEFAULTGRIDSIZE));
		currentDiagram.getHandler().getController().executeCommand(cmd);
		currentDiagram.getSelector().singleSelect(e);
		e.setStickingBorderActive(false);

		// After inserting the new entity we restore the old zoom level of both diagrams
		currentDiagram.getHandler().setGridAndZoom(oldZoomDiagram, false);
		handler.setGridAndZoom(oldZoomPalette, false);
		return e;
	}
}
