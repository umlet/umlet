package com.baselet.diagram.command;

import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.umlet.element.Relation;

public class RemoveLinePoint extends Command {

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
		return _x * Main.getHandlerForElement(_relation).getGridSize();
	}

	private int getY() {
		return _y * Main.getHandlerForElement(_relation).getGridSize();
	}

	public RemoveLinePoint(Relation r, int i) {
		_relation = r;
		_where = i;
		Point p = r.getLinePoints().elementAt(i);

		_x = p.x / Main.getHandlerForElement(_relation).getGridSize();
		_y = p.y / Main.getHandlerForElement(_relation).getGridSize();
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
