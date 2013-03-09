package com.baselet.diagram.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.diagram.DiagramHandler;
import com.umlet.element.Relation;

public class AddLinePoint extends Command {

	private Relation _relation;
	private int _where;

	private int _x;
	private int _y;

	public Relation getRelation() {
		return _relation;
	}

	public int getWhere() {
		return _where;
	}

	private int getX() {
		return _x * _relation.getHandler().getGridSize();
	}

	private int getY() {
		return _y * _relation.getHandler().getGridSize();
	}

	public AddLinePoint(Relation r, int i, int x, int y) {
		_relation = r;
		_where = i;
		_x = x / _relation.getHandler().getGridSize();
		_y = y / _relation.getHandler().getGridSize();
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
