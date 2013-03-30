package com.baselet.gui.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.AddLinePoint;
import com.baselet.diagram.command.Move;
import com.baselet.diagram.command.MoveLinePoint;
import com.baselet.diagram.command.RemoveElement;
import com.baselet.diagram.command.RemoveLinePoint;
import com.umlet.element.Relation;


public class RelationListener extends GridElementListener {

	private static HashMap<DiagramHandler, RelationListener> listener = new HashMap<DiagramHandler, RelationListener>();

	public static RelationListener getInstance(DiagramHandler handler) {
		if (!listener.containsKey(handler)) listener.put(handler, new RelationListener(handler));
		return listener.get(handler);
	}

	private boolean IS_DRAGGING_LINEPOINT = false;
	private boolean IS_DRAGGING_LINE = false;
	private int LINEPOINT = -1;

	private RelationListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		if (me.getButton() == MouseEvent.BUTTON1) {
			this.IS_DRAGGING = false;
			this.IS_RESIZING = false;
			Relation rel = (Relation) me.getComponent();

			int where = rel.getLinePoint(new Point(me.getX(), me.getY()));
			if (where >= 0) {
				IS_DRAGGING_LINEPOINT = true;
				LINEPOINT = where;
			}
			else if (rel.isWholeLine(me.getX(), me.getY())) {
				IS_DRAGGING_LINE = true;
				IS_DRAGGING = true;
			}
			else if (me.getButton() == MouseEvent.BUTTON2) {
				IS_DRAGGING_DIAGRAM = true;
				IS_DRAGGING_LINE = false;
				IS_DRAGGING_LINEPOINT = false;
				IS_DRAGGING = false;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		if (IS_DRAGGING_LINEPOINT & (LINEPOINT >= 0)) {
			Relation rel = (Relation) me.getComponent();
			if (rel.isPartOfGroup() == false) {
				if (rel.allPointsOnSamePos()) {
					// If mousebutton is released and all points of a relation are on the same position,
					// the command which moved all points to the same position gets undone and the relation gets removed instead
					this.controller.undo();
					this.controller.executeCommand(new RemoveElement(rel));
				}
				else if (rel.isOnLine(LINEPOINT)) {
					this.controller.executeCommand(
							new RemoveLinePoint(rel, LINEPOINT));
				}
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
			Main.getInstance().getGUI().setCursor(Constants.HAND_CURSOR);
		}
		else if (rel.isWholeLine(me.getX(), me.getY())) {
			Main.getInstance().getGUI().setCursor(Constants.MOVE_CURSOR);
		}
		else Main.getInstance().getGUI().setCursor(Constants.CROSS_CURSOR);
		return;
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		if (this.doReturn()) return;
		if (this.IS_DRAGGING) return;
		if (this.IS_DRAGGING_DIAGRAM) return;

		Relation r = (Relation) me.getComponent();
		int gridSize = Main.getInstance().getDiagramHandler().getGridSize();

		// delta
		int delta_x = 0;
		int delta_y = 0;
		if (IS_DRAGGING_LINEPOINT) {
			Vector<Point> tmp = r.getLinePoints();
			Point p = tmp.elementAt(LINEPOINT);
			delta_x = (r.getLocation().x + p.x) % gridSize;
			delta_y = (r.getLocation().y + p.y) % gridSize;
		}

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();

		int diffx = newp.x - oldp.x - delta_x;
		int diffy = newp.y - oldp.y - delta_y;

		if (IS_DRAGGING_LINEPOINT & (LINEPOINT >= 0)) {
			this.controller.executeCommand(
					new MoveLinePoint(r, LINEPOINT, diffx, diffy));
			return;
		}
		else if (IS_DRAGGING_LINE) {
			this.controller.executeCommand(new Move(r, diffx, diffy));
			return;
		}

		int where = r.getLinePoint(new Point(me.getX(), me.getY()));
		if (where >= 0) {
			IS_DRAGGING_LINEPOINT = true;
			LINEPOINT = where;
			this.controller.executeCommand(
					new MoveLinePoint(r, where, diffx, diffy));
			return;
		}
		else {
			Point p = new Point(me.getX(), me.getY());
			int ins = r.getWhereToInsert(p);
			if (ins > 0) {
				IS_DRAGGING_LINEPOINT = true;
				LINEPOINT = ins;
				this.controller.executeCommand(
						new AddLinePoint(r, ins, me.getX(), me.getY()));
				return;
			}
		}
	}
}
