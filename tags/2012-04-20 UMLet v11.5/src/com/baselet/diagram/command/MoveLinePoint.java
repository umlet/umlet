package com.baselet.diagram.command;

import java.awt.Point;

import com.baselet.diagram.DiagramHandler;
import com.umlet.element.Relation;


public class MoveLinePoint extends Command {
	private Relation _relation;
	private int _linePointId, _diffx, _diffy;

	public int getLinePointId() {
		return _linePointId;
	}

	public Relation getRelation() {
		return _relation;
	}

	public int getDiffX() {
		return _diffx * _relation.getHandler().getGridSize();
	}

	public int getDiffY() {
		return _diffy * _relation.getHandler().getGridSize();
	}

	public MoveLinePoint(Relation rel, int i, int diffx, int diffy) {
		_relation = rel;
		_linePointId = i;
		_diffx = diffx / rel.getHandler().getGridSize();
		_diffy = diffy / rel.getHandler().getGridSize();
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		Point p = _relation.getLinePoints().elementAt(_linePointId);
		p.x = p.x + getDiffX();
		p.y = p.y + getDiffY();
		_relation.repaint();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		Point p = _relation.getLinePoints().elementAt(_linePointId);
		p.x = p.x - getDiffX();
		p.y = p.y - getDiffY();
		_relation.repaint();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof MoveLinePoint)) return false;
		MoveLinePoint mlp = (MoveLinePoint) c;
		if (this.getRelation() != mlp.getRelation()) return false;
		if (this.getLinePointId() != mlp.getLinePointId()) return false;
		return true;
	}

	@Override
	public Command mergeTo(Command c) {
		MoveLinePoint tmp = (MoveLinePoint) c;
		MoveLinePoint ret = new MoveLinePoint(this.getRelation(), this.getLinePointId(), this.getDiffX() + tmp.getDiffX(), this.getDiffY() + tmp.getDiffY());
		return ret;
	}
}
