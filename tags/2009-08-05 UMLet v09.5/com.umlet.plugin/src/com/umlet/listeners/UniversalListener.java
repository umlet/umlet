// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.Controller;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.Selector;
import com.umlet.control.diagram.SelectorFrame;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public abstract class UniversalListener extends ComponentAdapter implements
		MouseListener, MouseMotionListener {

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

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mousePressed(MouseEvent me) {		
		Umlet.getInstance().getGUI().requestFocus(); //to avoid beeing stuck in the propertyPanel
		Point off = this.getOffset(me);
		_xOffset = off.x;
		_yOffset = off.y;
		
		//everytime a mouse is pressed within a listener the gui gets the current diagram!
		Umlet.getInstance().setCurrentDiagram(this.handler);
	}

	protected void adjustLeftAndUpperCoorsNonNegative(DrawPanel panel) {
		int minx = 0;
		int miny = 0;
		for (int i = 0; i < panel.getComponents().length; i++) {
			Component c = panel.getComponent(i);
			int x = c.getX();
			int y = c.getY();
			if (x < minx)
				minx = x;
			if (y < miny)
				miny = y;
		}
		if (Math.abs(miny) % 10 != 0)
			miny -= miny % 10;
		if (Math.abs(minx) % 10 != 0)
			minx -= minx % 10;
		if (minx < 0 || miny < 0) {
			for (int i = 0; i < panel.getComponents().length; i++) {
				Component c = (JComponent) panel.getComponent(i);
				c.setLocation(c.getX() - minx, c.getY() - miny);
			}
			panel.incViewPosition(-minx, -miny);
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
		
		adjustLeftAndUpperCoorsNonNegative(this.diagram);
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
		int MAIN_UNIT = Umlet.getInstance().getMainUnit();

		new_x_eff = MAIN_UNIT * ((xNewOffset - MAIN_UNIT / 2) / MAIN_UNIT);
		new_y_eff = MAIN_UNIT * ((yNewOffset - MAIN_UNIT / 2) / MAIN_UNIT);
		old_x_eff = MAIN_UNIT * ((_xOffset - MAIN_UNIT / 2) / MAIN_UNIT);
		old_y_eff = MAIN_UNIT * ((_yOffset - MAIN_UNIT / 2) / MAIN_UNIT);
		
		_xOffset = xNewOffset;
		_yOffset = yNewOffset;
	}
	
//	only call after mouseDragged
	protected final boolean doReturn() {
		return _return;
	}
	
	//only call after mouseDragged
	protected final Point getOldCoordinate() {
		return new Point(old_x_eff,old_y_eff);
	}
	
//	only call after mouseDragged
	protected final Point getNewCoordinate() {
		return new Point(new_x_eff,new_y_eff);
	}
	
	protected abstract Point getOffset(MouseEvent me);
}