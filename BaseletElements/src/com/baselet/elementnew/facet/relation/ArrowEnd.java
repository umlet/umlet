package com.baselet.elementnew.facet.relation;

import com.baselet.control.enumerations.Direction;
import com.baselet.control.enumerations.RegexValueHolder;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.elementnew.element.uml.relation.RelationPoints;
import com.baselet.elementnew.element.uml.relation.ResizableObject;
import com.baselet.elementnew.facet.relation.RelationDrawer.ArrowEndType;

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

	abstract void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject);

	static ArrowEnd LEFT_NORMAL = new ArrowEnd("<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false);
		}
	};

	static ArrowEnd LEFT_CLOSED = new ArrowEnd("<<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, false);
		}
	};

	static ArrowEnd LEFT_FILLED_CLOSED = new ArrowEnd("<<<<<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, true);
		}
	};

	static ArrowEnd LEFT_DIAMOND = new ArrowEnd("<<<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, false);
		}
	};

	static ArrowEnd LEFT_FILLED_DIAMOND = new ArrowEnd("<<<<") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, true);
		}
	};

	static ArrowEnd RIGHT_NORMAL = new ArrowEnd(">") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false);
		}
	};

	static ArrowEnd RIGHT_CLOSED = new ArrowEnd(">>") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, false);
		}
	};

	static ArrowEnd RIGHT_FILLED_CLOSED = new ArrowEnd(">>>>>") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, true);
		}
	};

	static ArrowEnd RIGHT_DIAMOND = new ArrowEnd(">>>") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, false);
		}
	};

	static ArrowEnd RIGHT_FILLED_DIAMOND = new ArrowEnd(">>>>") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, true);
		}
	};

	static ArrowEnd BOX_EMPTY = new ArrowEnd("\\[\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, null);
		}
	};

	static ArrowEnd BOX_DOWN_ARROW = new ArrowEnd("\\[v\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.DOWN);
		}
	};

	static ArrowEnd BOX_LEFT_ARROW = new ArrowEnd("\\[<\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.LEFT);
		}
	};

	static ArrowEnd BOX_RIGHT_ARROW = new ArrowEnd("\\[>\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.RIGHT);
		}
	};

	static ArrowEnd BOX_UP_ARROW = new ArrowEnd("\\[\\^\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, Direction.UP);
		}
	};

	static ArrowEnd BOX_EQUALS = new ArrowEnd("\\[=\\]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawBoxArrowEquals(drawer, lineToDraw, drawOnLineStart);
		}
	};

	static ArrowEnd BOX_TEXT = new ArrowEnd("\\[[^\\]]+]") {
		@Override
		public void print(DrawHandler drawer, RelationPoints points, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			String textWithoutBox = matchedText.substring(1, matchedText.length() - 1);
			RelationDrawer.drawBoxArrowText(drawer, lineToDraw, drawOnLineStart, textWithoutBox, resizableObject);
		}
	};
}