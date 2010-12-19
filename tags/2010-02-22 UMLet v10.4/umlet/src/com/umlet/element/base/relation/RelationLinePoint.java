// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base.relation;

import com.umlet.element.base.Relation;

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
