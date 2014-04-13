package com.baselet.elementnew.facet.relation;

import com.baselet.control.enumerations.ValueHolder;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.elementnew.element.uml.relation.RelationPoints;

interface ArrowEnd extends ValueHolder {
	void print(DrawHandler drawer, RelationPoints points);

	static ArrowEnd LEFT_NORMAL = new ArrowEnd() {
		@Override
		public void print(DrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getFirstLine(), true, false, false);
		}
		@Override
		public String getValue() {
			return "<";
		}
	};

	static ArrowEnd RIGHT_NORMAL = new ArrowEnd() {
		@Override
		public void print(DrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getLastLine(), false, false, false);
		}
		@Override
		public String getValue() {
			return ">";
		}
	};

	static ArrowEnd LEFT_CLOSED = new ArrowEnd() {
		@Override
		public void print(DrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getFirstLine(), true, false, true);
		}
		@Override
		public String getValue() {
			return "<<";
		}
	};

	static ArrowEnd RIGHT_CLOSED = new ArrowEnd() {
		@Override
		public void print(DrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getLastLine(), false, false, true);
		}
		@Override
		public String getValue() {
			return ">>";
		}
	};
}