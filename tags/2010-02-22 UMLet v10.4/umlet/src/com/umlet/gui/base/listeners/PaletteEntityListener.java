package com.umlet.gui.base.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.AddEntity;
import com.umlet.control.command.Command;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;

public class PaletteEntityListener extends EntityListener {

	Map<Entity,Point> previousDraggingLocation;

	private static HashMap<DiagramHandler, PaletteEntityListener> entitylistener = new HashMap<DiagramHandler, PaletteEntityListener>();
	private final static Logger log = Logger.getLogger(PaletteEntityListener.class);
	private Vector<Entity> copiedEntities;

	public static PaletteEntityListener getInstance(DiagramHandler handler) {
		if (!entitylistener.containsKey(handler)) entitylistener.put(handler, new PaletteEntityListener(handler));
		return entitylistener.get(handler);
	}

	protected PaletteEntityListener(DiagramHandler handler) {
		super(handler);
		previousDraggingLocation = new Hashtable<Entity, Point>();
		copiedEntities = new Vector<Entity>();
	}

	@Override
	public void mouseDoubleClicked(Entity me) {
		copyEntity(me);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		Entity entity = (Entity) me.getComponent();
		Vector<Entity> selectedEntities = handler.getDrawPanel().getSelector().getSelectedEntities();
		for(Entity currentEntity : selectedEntities){
			currentEntity.setStickBorders(false);
			if (this.IS_DRAGGING)
				previousDraggingLocation.put(currentEntity, currentEntity.getLocation());			
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		Entity entity = (Entity) me.getComponent();

		if (IS_DRAGGED_FROM_PALETTE){
			moveDraggedEntities();
		}
		else {
			if (entity.getLocationOnScreen().x+entity.getWidth() <= handler.getDrawPanel().getLocationOnScreen().x) {
				resetEntities();							
				insertDraggedEntities(me);		
				handler.getDrawPanel().getSelector().deselectAllWithoutUpdatePropertyPanel();				
			}
		}
	}
	
	private void moveDraggedEntities(){
		for(Entity copiedEntity : copiedEntities){
			int x = this.getNewCoordinate().x - this.getOldCoordinate().x;
			int y = this.getNewCoordinate().y - this.getOldCoordinate().y;
			x = Umlet.getInstance().getDiagramHandler().realignToGrid(false, x);
			y = Umlet.getInstance().getDiagramHandler().realignToGrid(false, y);			
			copiedEntity.changeLocation(x, y);
		}
	}
	
	private void resetEntities(){
		Vector<Entity> selectedEntities = handler.getDrawPanel().getSelector().getSelectedEntities();
		for(Entity currentEntity: selectedEntities){
			Point previousLocation = previousDraggingLocation.get(currentEntity);
			currentEntity.setStickBorders(true);
			currentEntity.setLocation(previousLocation.x, previousLocation.y);
		}
	}
	
	private void insertDraggedEntities(MouseEvent me){
		Entity entity = (Entity) me.getComponent();
		DrawPanel currentDiagram = Umlet.getInstance().getGUI().getCurrentDiagram();
		Vector<Entity> selectedEntities = handler.getDrawPanel().getSelector().getSelectedEntities();		
		
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
		
		for(Entity currentEntity : selectedEntities){
			Entity copiedEntity = copyEntity(currentEntity);
			copiedEntities.add(copiedEntity);
			DiagramHandler currentHandler = Umlet.getInstance().getDiagramHandler();
			EntityListener eListener = currentHandler.getEntityListener(copiedEntity);
			int x = (currentEntity.getX()-entity.getX());
			int y = (currentEntity.getY()-entity.getY());
			x -= (entity.getWidth() / 2);
			y -= (entity.getHeight() / 2);			
			copiedEntity.setLocation(x, y);
		}
		
		// After inserting the new entity we restore the old zoom level of both diagrams
		currentDiagram.getHandler().setGridAndZoom(oldZoomDiagram, false);
		handler.setGridAndZoom(oldZoomPalette, false);	
		
		//set entity positions relative to current mouse pointer location on screen
		updateEntityPositions(me);
	}
	
	private void updateEntityPositions(MouseEvent me){
		DiagramHandler currentHandler = Umlet.getInstance().getDiagramHandler();
		Point mousePosition = me.getLocationOnScreen();
		int mouseX = mousePosition.x - currentHandler.getDrawPanel().getLocationOnScreen().x;
		int mouseY = mousePosition.y - currentHandler.getDrawPanel().getLocationOnScreen().y;
		for(Entity copiedEntity: copiedEntities){
			int x = copiedEntity.getLocation().x;
			int y = copiedEntity.getLocation().y;
			x += mouseX;
			y += mouseY;
			x = Umlet.getInstance().getDiagramHandler().realignToGrid(false, x);
			y = Umlet.getInstance().getDiagramHandler().realignToGrid(false, y);
			copiedEntity.setLocation(x, y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		Umlet.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	protected boolean allowCopyEntity() {
		return true;
	}

	protected Entity copyEntity(Entity me) {
		DrawPanel currentDiagram = Umlet.getInstance().getGUI().getCurrentDiagram();

		// We save the actual zoom level of the diagram and the palette
		int oldZoomDiagram = currentDiagram.getHandler().getGridSize();
		int oldZoomPalette = handler.getGridSize();
		// and reset the zoom level of both to default before inserting the new entity (to avoid problems with entity-size)
		currentDiagram.getHandler().setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);
		
		Entity e = me.CloneFromMe();
		if (currentDiagram != null) {
			Command cmd;
			Point viewp = currentDiagram.getScrollPanel().getViewport().getViewPosition();
			int upperLeftX = (int) (viewp.getX() - viewp.getX() % oldZoomDiagram);
			int upperLeftY = (int) (viewp.getY() - viewp.getY() % oldZoomDiagram);
			cmd = new AddEntity(e,
					handler.realignToGrid(((upperLeftX / oldZoomDiagram) + Constants.PASTE_DISPLACEMENT_GRIDS) * Constants.DEFAULTGRIDSIZE),
					handler.realignToGrid(((upperLeftY / oldZoomDiagram) + Constants.PASTE_DISPLACEMENT_GRIDS) * Constants.DEFAULTGRIDSIZE));
			currentDiagram.getHandler().getController().executeCommand(cmd);
			currentDiagram.getSelector().singleSelect(e);
		}
		e.setStickBorders(false);

		// After inserting the new entity we restore the old zoom level of both diagrams
		currentDiagram.getHandler().setGridAndZoom(oldZoomDiagram, false);
		handler.setGridAndZoom(oldZoomPalette, false);
		return e;
	}
}
