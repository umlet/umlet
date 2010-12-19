package com.umlet.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JComponent;

import com.umlet.constants.Constants;
import com.umlet.control.command.Command;
import com.umlet.control.command.Macro;
import com.umlet.control.command.Move;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.SelectorFrame;
import com.umlet.control.Umlet;
import com.umlet.element.base.Entity;

public class DiagramListener extends UniversalListener {
	
	public DiagramListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		
		if ((me.getModifiers() & Constants.CTRLkey) != 0) {
			SelectorFrame selframe = this.selector.getSelectorFrame();
			selframe.setLocation(me.getX(), me.getY());
			selframe.setSize(1, 1);
			((JComponent) me.getComponent()).add(selframe, 0);
			selector.setSelectorFrameActive(true);
		}
		else
			selector.deselectAll();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		Umlet.getInstance().getGUI().setCursor(Constants.defCursor);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		if(this.doReturn())
			return;
		
		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();
		
		int diffx = newp.x - oldp.x;
		int diffy = newp.y - oldp.y;
		
		Vector<Entity> v = this.diagram.getAllEntities();
		Vector<Command> moveCommands = new Vector<Command>();
		for (int i = 0; i < v.size(); i++) {
			Entity e = v.elementAt(i);
			moveCommands.add(new Move(e, diffx, diffy));
		}
		this.controller.executeCommand(new Macro(moveCommands));
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX(), me.getY());
	}
}
