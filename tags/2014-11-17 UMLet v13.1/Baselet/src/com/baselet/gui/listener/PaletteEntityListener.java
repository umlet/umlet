package com.baselet.gui.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.Constants;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.Selector;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.AddElement;
import com.baselet.gui.command.Command;

public class PaletteEntityListener extends GridElementListener {

	Map<GridElement, Rectangle> previousDraggingLocation;

	private static HashMap<DiagramHandler, PaletteEntityListener> entitylistener = new HashMap<DiagramHandler, PaletteEntityListener>();
	private Vector<GridElement> copiedEntities;

	public static PaletteEntityListener getInstance(DiagramHandler handler) {
		if (!entitylistener.containsKey(handler)) {
			entitylistener.put(handler, new PaletteEntityListener(handler));
		}
		return entitylistener.get(handler);
	}

	protected PaletteEntityListener(DiagramHandler handler) {
		super(handler);
		previousDraggingLocation = new Hashtable<GridElement, Rectangle>();
		copiedEntities = new Vector<GridElement>();
	}

	@Override
	public void mouseDoubleClicked(GridElement me) {
		selector.deselectAll(); // deselect elements in palette
		copyEntity(me);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		List<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedElements();
		for (GridElement currentEntity : selectedEntities) {
			if (IS_DRAGGING) {
				previousDraggingLocation.put(currentEntity, currentEntity.getRectangle());
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		GridElement entity = handler.getDrawPanel().getElementToComponent(me.getComponent());

		if (IS_DRAGGED_FROM_PALETTE) {
			moveDraggedEntities();
		}
		else if (entity.getRectangle().x + entity.getRectangle().width <= 0) {
			resetEntities();
			insertDraggedEntities(me);
			handler.getDrawPanel().getSelector().deselectAllWithoutUpdatePropertyPanel();
		}
	}

	private void moveDraggedEntities() {
		for (GridElement copiedEntity : copiedEntities) {
			int x = getNewCoordinate().x - getOldCoordinate().x;
			int y = getNewCoordinate().y - getOldCoordinate().y;
			x = CurrentDiagram.getInstance().getDiagramHandler().realignToGrid(false, x);
			y = CurrentDiagram.getInstance().getDiagramHandler().realignToGrid(false, y);
			copiedEntity.setLocationDifference(x, y);
		}
	}

	private void resetEntities() {
		List<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedElements();
		for (GridElement currentEntity : selectedEntities) {
			Rectangle previousLocation = previousDraggingLocation.get(currentEntity);
			currentEntity.setLocation(previousLocation.x, previousLocation.y);
		}
	}

	private void insertDraggedEntities(MouseEvent me) {
		GridElement entity = handler.getDrawPanel().getElementToComponent(me.getComponent());
		DrawPanel currentDiagram = CurrentGui.getInstance().getGui().getCurrentDiagram();
		List<GridElement> selectedEntities = handler.getDrawPanel().getSelector().getSelectedElements();

		if (!allowCopyEntity()) {
			return;
		}

		copiedEntities.clear();

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = currentDiagram.getHandler().getGridSize();
		int oldZoomPalette = handler.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		currentDiagram.getHandler().setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		IS_DRAGGING = false;
		IS_DRAGGED_FROM_PALETTE = true;

		for (GridElement currentEntity : selectedEntities) {
			GridElement copiedEntity = copyEntity(currentEntity);
			copiedEntities.add(copiedEntity);
			int x = currentEntity.getRectangle().x - entity.getRectangle().x;
			int y = currentEntity.getRectangle().y - entity.getRectangle().y;
			x -= entity.getRectangle().width / 2;
			y -= entity.getRectangle().height / 2;
			copiedEntity.setLocation(x, y);
		}
		Selector.replaceGroupsWithNewGroups(copiedEntities, selector);

		// After inserting the new entity we restore the old zoom level of both diagrams
		currentDiagram.getHandler().setGridAndZoom(oldZoomDiagram, false);
		handler.setGridAndZoom(oldZoomPalette, false);

		// set entity positions relative to current mouse pointer location on screen
		updateEntityPositions(me);
	}

	private void updateEntityPositions(MouseEvent me) {
		DiagramHandler currentHandler = CurrentDiagram.getInstance().getDiagramHandler();
		Point mousePosition = me.getLocationOnScreen();
		int mouseX = mousePosition.x - currentHandler.getDrawPanel().getLocationOnScreen().x;
		int mouseY = mousePosition.y - currentHandler.getDrawPanel().getLocationOnScreen().y;
		for (GridElement copiedEntity : copiedEntities) {
			int x = copiedEntity.getRectangle().x;
			int y = copiedEntity.getRectangle().y;
			x += mouseX;
			y += mouseY;
			x = CurrentDiagram.getInstance().getDiagramHandler().realignToGrid(false, x);
			y = CurrentDiagram.getInstance().getDiagramHandler().realignToGrid(false, y);
			copiedEntity.setLocation(x, y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	protected boolean allowCopyEntity() {
		return true;
	}

	protected GridElement copyEntity(GridElement me) {
		DrawPanel currentDiagram = CurrentGui.getInstance().getGui().getCurrentDiagram();

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = currentDiagram.getHandler().getGridSize();
		int oldZoomPalette = handler.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		currentDiagram.getHandler().setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		GridElement e = ElementFactorySwing.createCopy(me);
		e.setProperty(GroupFacet.KEY, null);

		Command cmd;
		Point viewp = currentDiagram.getScrollPane().getViewport().getViewPosition();
		int upperLeftX = (int) (viewp.getX() - viewp.getX() % oldZoomDiagram);
		int upperLeftY = (int) (viewp.getY() - viewp.getY() % oldZoomDiagram);
		cmd = new AddElement(e,
				handler.realignToGrid((upperLeftX / oldZoomDiagram + Constants.PASTE_DISPLACEMENT_GRIDS) * Constants.DEFAULTGRIDSIZE),
				handler.realignToGrid((upperLeftY / oldZoomDiagram + Constants.PASTE_DISPLACEMENT_GRIDS) * Constants.DEFAULTGRIDSIZE));
		currentDiagram.getHandler().getController().executeCommand(cmd);
		currentDiagram.getSelector().selectOnly(e);

		// After inserting the new entity we restore the old zoom level of both diagrams
		currentDiagram.getHandler().setGridAndZoom(oldZoomDiagram, false);
		handler.setGridAndZoom(oldZoomPalette, false);
		return e;
	}
}
