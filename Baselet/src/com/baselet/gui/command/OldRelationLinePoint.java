package com.baselet.gui.command;

import com.baselet.element.old.element.Relation;

public class OldRelationLinePoint {
	private Relation _relation;
	private int _linePointId;
	private int stickingLineId;

	public Relation getRelation() {
		return _relation;
	}

	public int getLinePointId() {
		return _linePointId;
	}

	public int getStickingLineId() {
		return stickingLineId;
	}

	public OldRelationLinePoint(Relation r, int lp, int stick) {
		_relation = r;
		_linePointId = lp;
		stickingLineId = stick;
	}
}
