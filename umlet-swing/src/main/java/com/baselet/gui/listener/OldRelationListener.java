package com.baselet.gui.listener;

import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.old.element.Relation;
import com.baselet.element.sticking.StickableMap;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.Command;
import com.baselet.gui.command.Move;
import com.baselet.gui.command.OldMoveLinePoint;
import com.baselet.gui.command.RemoveElement;

/**
 * new relation doesnt need its own listener
 */
@Deprecated
public class OldRelationListener extends GridElementListener {

	private static class OldAddLinePoint extends Command {

		private final Relation _relation;
		private final int _where;

		private final int _x;
		private final int _y;

		private int getX() {
			return _x * HandlerElementMap.getHandlerForElement(_relation).getGridSize();
		}

		private int getY() {
			return _y * HandlerElementMap.getHandlerForElement(_relation).getGridSize();
		}

		public OldAddLinePoint(Relation r, int i, int x, int y) {
			_relation = r;
			_where = i;
			_x = x / HandlerElementMap.getHandlerForElement(_relation).getGridSize();
			_y = y / HandlerElementMap.getHandlerForElement(_relation).getGridSize();
		}

		@Override
		public void execute(DiagramHandler handler) {
			super.execute(handler);
			Vector<Point> tmp = _relation.getLinePoints();
			tmp.insertElementAt(new Point(getX(), getY()), _where);
			_relation.repaint();
		}

		@Override
		public void undo(DiagramHandler handler) {
			super.undo(handler);
			Vector<Point> tmp = _relation.getLinePoints();
			tmp.removeElementAt(_where);
			_relation.repaint();
		}
	}

	private static class OldRemoveLinePoint extends Command {

		private final Relation _relation;
		private final int _where;

		private final int _x;
		private final int _y;

		private int getX() {
			return _x * HandlerElementMap.getHandlerForElement(_relation).getGridSize();
		}

		private int getY() {
			return _y * HandlerElementMap.getHandlerForElement(_relation).getGridSize();
		}

		public OldRemoveLinePoint(Relation r, int i) {
			_relation = r;
			_where = i;
			Point p = r.getLinePoints().elementAt(i);

			_x = p.x / HandlerElementMap.getHandlerForElement(_relation).getGridSize();
			_y = p.y / HandlerElementMap.getHandlerForElement(_relation).getGridSize();
		}

		@Override
		public void execute(DiagramHandler handler) {
			super.execute(handler);
			Vector<Point> tmp = _relation.getLinePoints();
			tmp.removeElementAt(_where);
			_relation.repaint();
		}

		@Override
		public void undo(DiagramHandler handler) {
			super.undo(handler);
			Vector<Point> tmp = _relation.getLinePoints();
			tmp.insertElementAt(new Point(getX(), getY()), _where);
			_relation.repaint();
		}
	}

	private boolean IS_DRAGGING_LINEPOINT = false;
	private boolean IS_DRAGGING_LINE = false;
	private int LINEPOINT = -1;

	public OldRelationListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		if (me.getButton() == MouseEvent.BUTTON1) {
			IS_DRAGGING = false;
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
		if (IS_DRAGGING_LINEPOINT & LINEPOINT >= 0) {
			Relation rel = (Relation) me.getComponent();
			if (rel.allPointsOnSamePos()) {
				// If mousebutton is released and all points of a relation are on the same position,
				// the command which moved all points to the same position gets undone and the relation gets removed instead
				controller.undo();
				controller.executeCommand(new RemoveElement(rel));
			}
			else if (rel.isOnLine(LINEPOINT)) {
				controller.executeCommand(
						new OldRemoveLinePoint(rel, LINEPOINT));
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

		CursorOwn cursor;
		if (where >= 0) {
			cursor = CursorOwn.HAND;
		}
		else if (rel.isWholeLine(me.getX(), me.getY())) {
			cursor = CursorOwn.MOVE;
		}
		else {
			cursor = CursorOwn.CROSS;
		}
		CurrentGui.getInstance().getGui().setCursor(Converter.convert(cursor));
		return;
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		if (disableElementMovement()) {
			return;
		}
		if (IS_DRAGGING) {
			return;
		}
		if (IS_DRAGGING_DIAGRAM) {
			return;
		}

		Relation r = (Relation) me.getComponent();
		int gridSize = CurrentDiagram.getInstance().getDiagramHandler().getGridSize();

		// delta
		int delta_x = 0;
		int delta_y = 0;
		if (IS_DRAGGING_LINEPOINT) {
			Vector<Point> tmp = r.getLinePoints();
			Point p = tmp.elementAt(LINEPOINT);
			delta_x = (r.getRectangle().x + p.x) % gridSize;
			delta_y = (r.getRectangle().y + p.y) % gridSize;
		}

		Point newp = getNewCoordinate();
		Point oldp = getOldCoordinate();

		int diffx = newp.x - oldp.x - delta_x;
		int diffy = newp.y - oldp.y - delta_y;

		if (IS_DRAGGING_LINEPOINT & LINEPOINT >= 0) {
			controller.executeCommand(
					new OldMoveLinePoint(r, LINEPOINT, diffx, diffy));
			return;
		}
		else if (IS_DRAGGING_LINE) {
			controller.executeCommand(new Move(Collections.<Direction> emptySet(), r, diffx, diffy, oldp, me.isShiftDown(), false, true, StickableMap.EMPTY_MAP));
			return;
		}

		int where = r.getLinePoint(new Point(me.getX(), me.getY()));
		if (where >= 0) {
			IS_DRAGGING_LINEPOINT = true;
			LINEPOINT = where;
			controller.executeCommand(
					new OldMoveLinePoint(r, where, diffx, diffy));
			return;
		}
		else {
			Point p = new Point(me.getX(), me.getY());
			int ins = r.getWhereToInsert(p);
			if (ins > 0) {
				IS_DRAGGING_LINEPOINT = true;
				LINEPOINT = ins;
				controller.executeCommand(
						new OldAddLinePoint(r, ins, me.getX(), me.getY()));
				return;
			}
		}
	}
}
