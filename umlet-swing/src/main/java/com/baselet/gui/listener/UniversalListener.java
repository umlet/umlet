package com.baselet.gui.listener;

import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.Vector;

import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.SelectorFrame;
import com.baselet.diagram.SelectorOld;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.Command;
import com.baselet.gui.command.Controller;
import com.baselet.gui.command.Macro;
import com.baselet.gui.command.Move;

public abstract class UniversalListener extends ComponentAdapter implements MouseListener, MouseMotionListener {

	protected DiagramHandler handler;
	protected DrawPanel diagram;
	public SelectorOld selector;
	protected Controller controller;

	private int _xOffset, _yOffset;
	private boolean disableElementMovement = true; // is true after mouseReleased until the next mousePressed AND if the lasso is active
	private int old_x_eff, old_y_eff;
	private int new_x_eff, new_y_eff;

	protected UniversalListener(DiagramHandler handler) {
		this.handler = handler;
		diagram = handler.getDrawPanel();
		selector = diagram.getSelector();
		controller = handler.getController();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent me) {}

	@Override
	public void mousePressed(MouseEvent me) {
		disableElementMovement = false;
		CurrentGui.getInstance().getGui().requestFocus(); // to avoid beeing stuck in the propertyPanel
		Point off = getOffset(me);
		_xOffset = off.x;
		_yOffset = off.y;

		// everytime a mouse is pressed within a listener the gui gets the current diagram!
		CurrentDiagram.getInstance().setCurrentDiagramHandlerAndZoom(handler);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		disableElementMovement = true;
		if (selector.isSelectorFrameActive()) {
			SelectorFrame selframe = selector.getSelectorFrame();
			diagram.remove(selframe);
			selector.deselectAll();
			selector.multiSelect(Converter.convert(selframe.getBounds()));
			selector.setSelectorFrameActive(false);
			diagram.repaint();
		}

		diagram.updatePanelAndScrollbars();
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent me) {

	}

	@Override
	public void mouseDragged(MouseEvent me) {
		// Get new mouse coordinates
		if (selector.isSelectorFrameActive()) {
			selector.getSelectorFrame().resizeTo(getOffset(me).getX(), getOffset(me).getY());
			disableElementMovement = true;
			return;
		}
		else if (disableElementMovement()) {
			return;
		}

		Point off = getOffset(me);
		int xNewOffset = off.x;
		int yNewOffset = off.y;
		int gridSize = CurrentDiagram.getInstance().getDiagramHandler().getGridSize();

		new_x_eff = gridSize * ((xNewOffset - gridSize / 2) / gridSize);
		new_y_eff = gridSize * ((yNewOffset - gridSize / 2) / gridSize);
		old_x_eff = gridSize * ((_xOffset - gridSize / 2) / gridSize);
		old_y_eff = gridSize * ((_yOffset - gridSize / 2) / gridSize);

		_xOffset = xNewOffset;
		_yOffset = yNewOffset;
	}

	// only call after mouseDragged
	protected final boolean disableElementMovement() {
		return disableElementMovement;
	}

	// only call after mouseDragged
	protected final Point getOldCoordinate() {
		return new Point(old_x_eff, old_y_eff);
	}

	// only call after mouseDragged
	protected final Point getNewCoordinate() {
		return new Point(new_x_eff, new_y_eff);
	}

	protected abstract Point getOffset(MouseEvent me);

	protected void dragDiagram() {
		if (disableElementMovement()) {
			return;
		}

		Point newp = getNewCoordinate();
		Point oldp = getOldCoordinate();

		int diffx = newp.x - oldp.x;
		int diffy = newp.y - oldp.y;

		if (diffx != 0 || diffy != 0) {
			Vector<Command> moveCommands = new Vector<Command>();
			for (GridElement e : diagram.getGridElements()) {
				moveCommands.add(new Move(Collections.<Direction> emptySet(), e, diffx, diffy, oldp, false, false, true, StickableMap.EMPTY_MAP));
			}

			controller.executeCommand(new Macro(moveCommands));
		}
	}
}
