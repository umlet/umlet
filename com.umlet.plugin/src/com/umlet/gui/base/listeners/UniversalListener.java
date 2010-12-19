// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.gui.base.listeners;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.Controller;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.Selector;
import com.umlet.control.diagram.SelectorFrame;
import com.umlet.gui.standalone.StandaloneGUI;

public abstract class UniversalListener extends ComponentAdapter implements MouseListener, MouseMotionListener {

	protected DiagramHandler handler;
	protected DrawPanel diagram;
	protected Selector selector;
	protected Controller controller;

	protected UniversalListener(DiagramHandler handler) {
		_return = false;
		this.handler = handler;
		this.diagram = handler.getDrawPanel();
		this.selector = diagram.getSelector();
		this.controller = handler.getController();
	}

	private int _xOffset, _yOffset;
	private boolean _return; // variable used to coordinate listener exits with inherited listeners
	private int old_x_eff, old_y_eff;
	private int new_x_eff, new_y_eff;

	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent me) {}

	public void mousePressed(MouseEvent me) {
		Umlet.getInstance().getGUI().requestFocus(); // to avoid beeing stuck in the propertyPanel
		Point off = this.getOffset(me);
		_xOffset = off.x;
		_yOffset = off.y;

		// everytime a mouse is pressed within a listener the gui gets the current diagram!
		Umlet.getInstance().setCurrentDiagram(this.handler);

		// every click on any diagram or entity sets the actual zoom value of the diagram in the zoombox on top of the StandaloneGUI
		if ((Umlet.getInstance().getGUI() instanceof StandaloneGUI) && (Umlet.getInstance().getDiagramHandler() != null)) {
			int factor = Umlet.getInstance().getDiagramHandler().getGridSize();
			((StandaloneGUI) Umlet.getInstance().getGUI()).setValueOfZoomDisplay(factor);
		}
	}

	public void mouseReleased(MouseEvent me) {
		_return = false;
		if (this.selector.isSelectorFrameActive()) {
			SelectorFrame selframe = this.selector.getSelectorFrame();
			this.diagram.remove(selframe);
			this.selector.deselectAll();
			this.selector.multiSelect(selframe.getLocation(), selframe.getSize());
			this.selector.setSelectorFrameActive(false);
			this.diagram.repaint();
		}

		diagram.insertUpperLeftWhitespaceIfNeeded();
		diagram.removeUnnecessaryWhitespaceAroundDiagram();
		this.diagram.revalidate();
	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent me) {

	}

	public void mouseDragged(MouseEvent me) {
		// Get new mouse coordinates
		if (this.selector.isSelectorFrameActive()) {
			this.selector.getSelectorFrame().resizeTo(me.getX(), me.getY());
			_return = true;
			return;
		}
		_return = false;

		Point off = this.getOffset(me);
		int xNewOffset = off.x;
		int yNewOffset = off.y;
		int gridSize = Umlet.getInstance().getDiagramHandler().getGridSize();

		new_x_eff = gridSize * ((xNewOffset - gridSize / 2) / gridSize);
		new_y_eff = gridSize * ((yNewOffset - gridSize / 2) / gridSize);
		old_x_eff = gridSize * ((_xOffset - gridSize / 2) / gridSize);
		old_y_eff = gridSize * ((_yOffset - gridSize / 2) / gridSize);

		_xOffset = xNewOffset;
		_yOffset = yNewOffset;
	}

	// only call after mouseDragged
	protected final boolean doReturn() {
		return _return;
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
}
