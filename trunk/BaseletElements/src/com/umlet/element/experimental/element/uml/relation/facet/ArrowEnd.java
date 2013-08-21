package com.umlet.element.experimental.element.uml.relation.facet;

import com.baselet.control.enumerations.ValueHolder;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.umlet.element.experimental.element.uml.relation.RelationPoints;

public interface ArrowEnd extends ValueHolder {
	public void print(BaseDrawHandler drawer, RelationPoints points);

	static ArrowEnd LEFT_NORMAL = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getFirstLine(), true, false, false);
		}
		@Override
		public String getValue() {
			return "<";
		}
	};

	static ArrowEnd RIGHT_NORMAL = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getLastLine(), false, false, false);
		}
		@Override
		public String getValue() {
			return ">";
		}
	};

	static ArrowEnd LEFT_CLOSED = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getFirstLine(), true, false, true);
		}
		@Override
		public String getValue() {
			return "<<";
		}
	};

	static ArrowEnd RIGHT_CLOSED = new ArrowEnd() {
		@Override
		public void print(BaseDrawHandler drawer, RelationPoints points) {
			RelationDrawer.drawArrowToLine(drawer, points.getLastLine(), false, false, true);
		}
		@Override
		public String getValue() {
			return ">>";
		}
	};
}