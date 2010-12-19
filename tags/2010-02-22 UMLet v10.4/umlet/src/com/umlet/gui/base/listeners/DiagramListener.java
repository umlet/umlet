package com.umlet.gui.base.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.Command;
import com.umlet.control.command.Macro;
import com.umlet.control.command.Move;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.SelectorFrame;
import com.umlet.element.base.Entity;

public class DiagramListener extends UniversalListener implements MouseWheelListener {

	private final static Logger log = Logger.getLogger(DiagramListener.class);

	public DiagramListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		selector.deselectAll();
		if(!handler.getDrawPanel().equals(Umlet.getInstance().getPalettePanel())){
			Umlet.getInstance().getPalettePanel().getSelector().deselectAllWithoutUpdatePropertyPanel();
		}		
		log.debug("mousePRessed!!");

		if ((me.getModifiers() & Constants.CTRLMETA_MASK) != 0) {
			SelectorFrame selframe = this.selector.getSelectorFrame();
			selframe.setLocation((int) getOffset(me).getX(), (int) getOffset(me).getY());
			selframe.setSize(1, 1);
			((JComponent) me.getComponent()).add(selframe, 0);
			selector.setSelectorFrameActive(true);
		}
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		log.debug("DiagramLister move");
		// for (Entity e : this.diagram.getSelector().getSelectedEntities()) {
			// EntityListener listener = handler.getEntityListener(e);
			// if(listener.IS_DRAGGED_FROM_PALETTE){
			// log.debug("IS_DRAGGED_FROM_PALETTE");
			// listener.IS_FIRST_MOVE = false;
			// e.changeLocation(me.getX()-e.getX(), me.getY()-e.getY());
			// handler.getEntityListener(e).IS_DRAGGED_FROM_PALETTE = false;
			// handler.getEntityListener(e).IS_DRAGGING = true;
			// e.setLocation(me.getX()-100, me.getY()-20);
			// }
		// }
		Umlet.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		// for(Entity e: this.diagram.getSelector().getSelectedEntities()){
		// if(handler.getEntityListener(e).IS_DRAGGED_FROM_PALETTE){
		// handler.getEntityListener(e).IS_DRAGGED_FROM_PALETTE = false;
		// }
		// }
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		log.debug("mouseDragged!!");

		if (this.doReturn()) return;

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();

		int diffx = newp.x - oldp.x;
		int diffy = newp.y - oldp.y;

		Vector<Entity> v = this.diagram.getAllEntities();
		Vector<Command> moveCommands = new Vector<Command>();
		for (int i = 0; i < v.size(); i++) {
			Entity e = v.elementAt(i);
			if (e.isPartOfGroup()) continue;
			moveCommands.add(new Move(e, diffx, diffy));
		}

		this.controller.executeCommand(new Macro(moveCommands));
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX(), me.getY());
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// Only if Ctrl is pressed while scrolling, we zoom in and out
		if ((e.getModifiersEx() & Constants.CTRLMETA_DOWN_MASK) == Constants.CTRLMETA_DOWN_MASK) {
			int actualZoom = Umlet.getInstance().getDiagramHandler().getGridSize();
			// e.getWheelRotation is -1 if scrolling up and +1 if scrolling down therefore we subtract it
			Umlet.getInstance().getDiagramHandler().setGridAndZoom(actualZoom - e.getWheelRotation());
		}
	}
}
