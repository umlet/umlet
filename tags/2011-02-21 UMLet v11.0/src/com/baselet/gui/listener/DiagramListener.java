package com.baselet.gui.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.SelectorFrame;
import com.baselet.diagram.command.Command;
import com.baselet.diagram.command.Macro;
import com.baselet.diagram.command.Move;
import com.baselet.element.GridElement;

public class DiagramListener extends UniversalListener implements MouseWheelListener {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	public DiagramListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		selector.deselectAll();
		if (!handler.getDrawPanel().equals(Main.getInstance().getPalettePanel())) {
			Main.getInstance().getPalettePanel().getSelector().deselectAllWithoutUpdatePropertyPanel();
		}
		log.debug("mousePressed!!");

		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
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
		Main.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
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

		Vector<GridElement> v = this.diagram.getAllEntities();
		Vector<Command> moveCommands = new Vector<Command>();
		for (int i = 0; i < v.size(); i++) {
			GridElement e = v.elementAt(i);
			if (e.isPartOfGroup()) continue;
			moveCommands.add(new Move(e, diffx, diffy));
		}

		this.controller.executeCommand(new Macro(moveCommands));
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX(), me.getY());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// Only if Ctrl is pressed while scrolling, we zoom in and out
		if ((e.getModifiersEx() & SystemInfo.META_KEY.getMaskDown()) == SystemInfo.META_KEY.getMaskDown()) {
			int actualZoom = Main.getInstance().getDiagramHandler().getGridSize();
			// e.getWheelRotation is -1 if scrolling up and +1 if scrolling down therefore we subtract it
			Main.getInstance().getDiagramHandler().setGridAndZoom(actualZoom - e.getWheelRotation());
		}
	}
}
