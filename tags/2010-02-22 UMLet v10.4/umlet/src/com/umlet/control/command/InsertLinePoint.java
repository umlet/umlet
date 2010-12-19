// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Relation;

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

public class InsertLinePoint extends Command {

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

	public InsertLinePoint(Relation r, int i, int x, int y) {
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
