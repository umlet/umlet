package com.baselet.diagram.command;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.umlet.element.Relation;

/**
 * only for old deprecated relation; new relation handles linepoint movement in the class-code
 */
@Deprecated
public class OldMoveLinePoint extends Command {
	private final Relation _relation;
	private final int _linePointId, _diffx, _diffy;

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

	public OldMoveLinePoint(Relation rel, int i, int diffx, int diffy) {
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
		if (!(c instanceof OldMoveLinePoint)) {
			return false;
		}
		OldMoveLinePoint mlp = (OldMoveLinePoint) c;
		if (getRelation() != mlp.getRelation()) {
			return false;
		}
		if (getLinePointId() != mlp.getLinePointId()) {
			return false;
		}
		return true;
	}

	@Override
	public Command mergeTo(Command c) {
		OldMoveLinePoint tmp = (OldMoveLinePoint) c;
		OldMoveLinePoint ret = new OldMoveLinePoint(getRelation(), getLinePointId(), getDiffX() + tmp.getDiffX(), getDiffY() + tmp.getDiffY());
		return ret;
	}
}
