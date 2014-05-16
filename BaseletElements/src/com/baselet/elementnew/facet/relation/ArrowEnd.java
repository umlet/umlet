package com.baselet.elementnew.facet.relation;

import com.baselet.control.enumerations.Direction;
import com.baselet.control.enumerations.RegexValueHolder;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.elementnew.element.uml.relation.RelationPoints;

abstract class ArrowEnd implements RegexValueHolder {
	private final String regexValue;

	public ArrowEnd(String regexValue) {
		super();
		this.regexValue = regexValue;
	}

	@Override
	public String getRegexValue() {
		return regexValue;
	}

	abstract void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart);

	static ArrowEnd LEFT_NORMAL = new ArrowEnd("<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, false, false, false);
		}
	};

	static ArrowEnd RIGHT_NORMAL = new ArrowEnd(">") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, false, false, false);
		}
	};

	static ArrowEnd LEFT_CLOSED = new ArrowEnd("<<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, false, true, false);
		}
	};

	static ArrowEnd LEFT_DIAMOND = new ArrowEnd("<<<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, false, false, true);
		}
	};

	static ArrowEnd RIGHT_CLOSED = new ArrowEnd(">>") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, false, true, false);
		}
	};

	static ArrowEnd RIGHT_DIAMOND = new ArrowEnd(">>>") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, false, false, true);
		}
	};

	static ArrowEnd BOX_EMPTY = new ArrowEnd("\\[\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, null);
		}
	};

	static ArrowEnd BOX_DOWN_ARROW = new ArrowEnd("\\[v\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.DOWN);
		}
	};

	static ArrowEnd BOX_LEFT_ARROW = new ArrowEnd("\\[<\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.LEFT);
		}
	};

	static ArrowEnd BOX_RIGHT_ARROW = new ArrowEnd("\\[>\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.RIGHT);
		}
	};

	static ArrowEnd BOX_UP_ARROW = new ArrowEnd("\\[\\^\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.UP);
		}
	};

	static ArrowEnd BOX_EQUALS = new ArrowEnd("\\[=\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart) {
			RelationDrawer.drawBoxArrowEquals(drawer, lineToDraw, drawOnLineStart);
		}
	};
}