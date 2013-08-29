package com.umlet.element.relation;

import com.umlet.element.Relation;

public class RelationLinePoint {
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
		return this.stickingLineId;
	}

	public RelationLinePoint(Relation r, int lp, int stick) {
		_relation = r;
		_linePointId = lp;
		this.stickingLineId = stick;
	}
}
