package com.baselet.diagram.command;

import com.baselet.control.Main;
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
		return _diffx * Main.getHandlerForElement(_relation).getGridSize();
	}

	public int getDiffY() {
		return _diffy * Main.getHandlerForElement(_relation).getGridSize();
	}

	public MoveLinePoint(Relation rel, int i, int diffx, int diffy) {
		_relation = rel;
		_linePointId = i;
		_diffx = diffx / Main.getHandlerForElement(rel).getGridSize();
		_diffy = diffy / Main.getHandlerForElement(rel).getGridSize();
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		_relation.moveLinePoint(_linePointId, getDiffX(), getDiffY());
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		_relation.moveLinePoint(_linePointId, -getDiffX(), -getDiffY());
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
