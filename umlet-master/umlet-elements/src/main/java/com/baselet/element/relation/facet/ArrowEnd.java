package com.baselet.element.relation.facet;

import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.RegexValueHolder;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.relation.helper.RelationDrawer;
import com.baselet.element.relation.helper.RelationDrawer.ArrowEndType;
import com.baselet.element.relation.helper.RelationPointConstants;
import com.baselet.element.relation.helper.ResizableObject;

abstract class ArrowEnd implements RegexValueHolder {

	static final String BOX_REGEX = "\\[[^\\]]*\\]";

	private final String regexValue;

	public ArrowEnd(String regexValue) {
		super();
		this.regexValue = regexValue;
	}

	@Override
	public String getRegexValue() {
		return regexValue;
	}

	abstract void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject);

	static ArrowEnd BOX = new ArrowEnd(BOX_REGEX) {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			String textWithoutBox = matchedText.substring(1, matchedText.length() - 1);
			RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, textWithoutBox, resizableObject);
		}
	};

	static ArrowEnd SINGLE_PIPE = new ArrowEnd("\\|") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.SINGLE_PIPE, false, true);
		}
	};

	static ArrowEnd DOUBLE_PIPE = new ArrowEnd("\\|\\|") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DOUBLE_PIPE, false, true);
		}
	};

	static ArrowEnd SMALL_CIRCLE = new ArrowEnd("o") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			PointDouble point = lineToDraw.getPointOnLineWithDistanceFrom(drawOnLineStart, RelationPointConstants.POINT_SELECTION_RADIUS * 2);
			RelationDrawer.drawCircle(point, RelationDrawer.SMALL_CIRCLE_RADIUS, drawer, lineToDraw, drawOnLineStart, resizableObject, null, false);
		}
	};

	static ArrowEnd LEFT_BOX = new ArrowEnd(BOX_REGEX + "<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			String textWithoutBox = matchedText.substring(1, matchedText.length() - 2);
			Rectangle r = RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, textWithoutBox, resizableObject);
			PointDouble intersection = lineToDraw.getIntersectionPoints(r).get(0);
			RelationDrawer.drawArrowToLine(intersection, drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, false);
		}
	};

	static ArrowEnd LEFT_NORMAL = new ArrowEnd("<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, false);
		}
	};

	static ArrowEnd LEFT_MEASURE_NORMAL = new ArrowEnd("\\|<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.MEASURE, false, false);
		}
	};

	static ArrowEnd LEFT_INVERTED = new ArrowEnd(">") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, true);
		}
	};

	static ArrowEnd LEFT_ZERO_TO_MANY = new ArrowEnd(">o") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			PointDouble point = lineToDraw.getPointOnLineWithDistanceFrom(drawOnLineStart, RelationPointConstants.POINT_SELECTION_RADIUS * 2);
			RelationDrawer.drawCircle(point, RelationDrawer.SMALL_CIRCLE_RADIUS, drawer, lineToDraw, drawOnLineStart, resizableObject, null, false);
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, true);
		}
	};

	static ArrowEnd LEFT_ONE_TO_MANY = new ArrowEnd(">\\|") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.MEASURE, false, true);
		}
	};

	static ArrowEnd LEFT_CLOSED = new ArrowEnd("<<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, false, false);
		}
	};

	static ArrowEnd LEFT_FILLED_CLOSED = new ArrowEnd("<<<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, true, false);
		}
	};

	static ArrowEnd LEFT_DIAMOND = new ArrowEnd("<<<<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, false, false);
		}
	};

	static ArrowEnd LEFT_FILLED_DIAMOND = new ArrowEnd("<<<<<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, true, false);
		}
	};

	static ArrowEnd LEFT_INTERFACE_OPEN = new ArrowEnd("\\)") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawCircle(drawer, lineToDraw, drawOnLineStart, resizableObject, Direction.LEFT, false);
		}
	};

	static ArrowEnd RIGHT_BOX = new ArrowEnd(">" + BOX_REGEX) {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			String textWithoutBox = matchedText.substring(2, matchedText.length() - 1);
			Rectangle r = RelationDrawer.drawBoxArrow(drawer, lineToDraw, drawOnLineStart, textWithoutBox, resizableObject);
			PointDouble intersection = lineToDraw.getIntersectionPoints(r).get(0);
			RelationDrawer.drawArrowToLine(intersection, drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, false);
		}
	};

	static ArrowEnd RIGHT_NORMAL = new ArrowEnd(">") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, false);
		}
	};

	static ArrowEnd RIGHT_MEASURE_NORMAL = new ArrowEnd(">\\|") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.MEASURE, false, false);
		}
	};

	static ArrowEnd RIGHT_INVERTED = new ArrowEnd("<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, true);
		}
	};

	static ArrowEnd RIGHT_ZERO_TO_MANY = new ArrowEnd("o<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			PointDouble point = lineToDraw.getPointOnLineWithDistanceFrom(drawOnLineStart, RelationPointConstants.POINT_SELECTION_RADIUS * 2);
			RelationDrawer.drawCircle(point, RelationDrawer.SMALL_CIRCLE_RADIUS, drawer, lineToDraw, drawOnLineStart, resizableObject, null, false);
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.NORMAL, false, true);
		}
	};

	static ArrowEnd RIGHT_ONE_TO_MANY = new ArrowEnd("\\|<") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.MEASURE, false, true);
		}
	};

	static ArrowEnd RIGHT_CLOSED = new ArrowEnd(">>") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, false, false);
		}
	};

	static ArrowEnd RIGHT_FILLED_CLOSED = new ArrowEnd(">>>") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.CLOSED, true, false);
		}
	};

	static ArrowEnd RIGHT_DIAMOND = new ArrowEnd(">>>>") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, false, false);
		}
	};

	static ArrowEnd RIGHT_FILLED_DIAMOND = new ArrowEnd(">>>>>") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawArrowToLine(drawer, lineToDraw, drawOnLineStart, ArrowEndType.DIAMOND, true, false);
		}
	};

	static ArrowEnd RIGHT_INTERFACE_OPEN = new ArrowEnd("\\(") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawCircle(drawer, lineToDraw, drawOnLineStart, resizableObject, Direction.RIGHT, false);
		}
	};

	static ArrowEnd CIRCLE_CROSS = new ArrowEnd("\\(\\+\\)") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawCircle(drawer, lineToDraw, drawOnLineStart, resizableObject, null, true);
		}
	};

	static ArrowEnd CIRCLE = new ArrowEnd("\\(\\)") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawCircle(drawer, lineToDraw, drawOnLineStart, resizableObject, null, false);
		}
	};

	static ArrowEnd DIAGONAL_CROSS = new ArrowEnd("x") {
		@Override
		public void print(DrawHandler drawer, Line lineToDraw, boolean drawOnLineStart, String matchedText, ResizableObject resizableObject) {
			RelationDrawer.drawDiagonalCross(drawer, lineToDraw, drawOnLineStart, resizableObject, null, false);
		}
	};
}