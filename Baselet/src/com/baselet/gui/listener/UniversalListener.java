package com.baselet.gui.listener;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.baselet.control.Main;
import com.baselet.diagram.Controller;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.Selector;
import com.baselet.diagram.SelectorFrame;


public abstract class UniversalListener extends ComponentAdapter implements MouseListener, MouseMotionListener {

	protected DiagramHandler handler;
	protected DrawPanel diagram;
	public Selector selector;
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

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent me) {}

	@Override
	public void mousePressed(MouseEvent me) {
		Main.getInstance().getGUI().requestFocus(); // to avoid beeing stuck in the propertyPanel
		Point off = this.getOffset(me);
		_xOffset = off.x;
		_yOffset = off.y;

		// everytime a mouse is pressed within a listener the gui gets the current diagram!
		Main.getInstance().setCurrentDiagramHandler(this.handler);

		if ((Main.getInstance().getDiagramHandler() != null)) {
			int factor = Main.getInstance().getDiagramHandler().getGridSize();
			Main.getInstance().getGUI().setValueOfZoomDisplay(factor);
		}
	}

	@Override
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
		if (this.selector.isSelectorFrameActive()) {
			// TODO
			this.selector.getSelectorFrame().resizeTo((int) getOffset(me).getX(), (int) getOffset(me).getY());
			_return = true;
			return;
		}
		_return = false;

		Point off = this.getOffset(me);
		int xNewOffset = off.x;
		int yNewOffset = off.y;
		int gridSize = Main.getInstance().getDiagramHandler().getGridSize();

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
