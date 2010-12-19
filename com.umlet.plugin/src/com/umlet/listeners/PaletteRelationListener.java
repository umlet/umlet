package com.umlet.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.command.*;
import com.umlet.control.Umlet;
import com.umlet.control.command.RemoveLinePoint;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Relation;

public class PaletteRelationListener extends PaletteEntityListener {

	private static HashMap<DiagramHandler,PaletteRelationListener> listener = new HashMap<DiagramHandler,PaletteRelationListener>();

	public static PaletteRelationListener getInstance(DiagramHandler handler) {
		if(!listener.containsKey(handler))
			listener.put(handler,new PaletteRelationListener(handler));
		return listener.get(handler);
	}
	
	private boolean IS_DRAGGING_LINEPOINT = false;
	private boolean IS_DRAGGING_LINE = false;
	private int LINEPOINT = -1;
	
	private PaletteRelationListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		if(me.getButton() == MouseEvent.BUTTON1)
		{
			this.IS_DRAGGING = false;
			Relation rel = (Relation) me.getComponent();
			
			int where = rel.getLinePoint(new Point(me.getX(), me.getY()));
			if (where >= 0) {
				IS_DRAGGING_LINEPOINT = true;
				LINEPOINT = where;
			}
			else if(rel.isWholeLine(me.getX(), me.getY())) {
				IS_DRAGGING_LINE = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		if (IS_DRAGGING_LINEPOINT & LINEPOINT >= 0) {
			Relation rel = (Relation) me.getComponent();
			if (rel.isOnLine(LINEPOINT) && rel.isPartOfGroup() == false) { // L.Trescher
				this.controller.executeCommand(
						new RemoveLinePoint(rel, LINEPOINT));
			}
		}
		IS_DRAGGING_LINEPOINT = false;
		IS_DRAGGING_LINE = false;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		Relation rel = (Relation) me.getComponent();
		int where = rel.getLinePoint(new Point(me.getX(), me.getY()));
		if (where >= 0) {
			Umlet.getInstance().getGUI().setCursor(Constants.handCursor);
		} else if(rel.isWholeLine(me.getX(), me.getY())) {
			Umlet.getInstance().getGUI().setCursor(Constants.moveCursor);
		} else
			Umlet.getInstance().getGUI().setCursor(Constants.crossCursor);
		return;
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		if(this.doReturn())
			return;
		
		Relation r = (Relation) me.getComponent();
		int MAIN_UNIT = Umlet.getInstance().getMainUnit();
		
//		 delta
		int delta_x = 0;
		int delta_y = 0;
		if (IS_DRAGGING_LINEPOINT) {	
			Vector<Point> tmp = r.getLinePoints();
			Point p = tmp.elementAt(LINEPOINT);
			delta_x = (r.getX() + p.x) % MAIN_UNIT;
			delta_y = (r.getY() + p.y) % MAIN_UNIT;
		}

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();
		
		int diffx = newp.x - oldp.x - delta_x;
		int diffy = newp.y - oldp.y - delta_y;

		if (IS_DRAGGING_LINEPOINT & LINEPOINT >= 0) {
			this.controller.executeCommand(
					new MoveLinePoint(r, LINEPOINT, diffx, diffy));
			return;
		}
		else if(IS_DRAGGING_LINE) {
			this.controller.executeCommand(new Move(r,diffx,diffy));
			return;
		}

		int where = r.getLinePoint(new Point(me.getX(), me.getY()));
		if (where >= 0) {
			IS_DRAGGING_LINEPOINT = true;
			LINEPOINT = where;
			this.controller.executeCommand(
					new MoveLinePoint(r, where, diffx, diffy));
			return;
		} else {
			Point p = new Point(me.getX(), me.getY());
			int ins = r.getWhereToInsert(p);
			if (ins > 0) {
				IS_DRAGGING_LINEPOINT = true;
				LINEPOINT = ins;
				this.controller.executeCommand(
						new InsertLinePoint(r, ins, me.getX(), me.getY()));
				return;
			}
		}
	}
}
