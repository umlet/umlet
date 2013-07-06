package com.umlet.element.experimental.uml.relation;

import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.settings.SettingsRelation;
import com.umlet.element.experimental.settings.facets.Facet;

public abstract class Arrow implements Facet {
	
	static final char SEP = '=';
	static final String START = "start" + SEP;
	static final String END = "end" + SEP;

	void drawArrowToLine(BaseDrawHandler drawer, Line line, boolean drawOnStart, boolean inverseArrow) {
		Point point = drawOnStart ? line.getStart() : line.getEnd();
		double angleOfSlopeOfLine = line.getAngleOfSlope();
		if (inverseArrow) {
			drawOnStart = !drawOnStart;
		}
		int angle = drawOnStart ? 150 : 30;
		drawArrowLine(drawer, point, angleOfSlopeOfLine, true, angle);
		drawArrowLine(drawer, point, angleOfSlopeOfLine, false, angle);
	}

	void drawArrowLine(BaseDrawHandler drawer, Point start, double angleOfSlopeOfLine, boolean first, int angle) {
		int arrowLength = RelationPoints.POINT_SELECTION_RADIUS;
		int arrowAngle = angle;
		double angleTotal = first ? angleOfSlopeOfLine-arrowAngle : angleOfSlopeOfLine+arrowAngle;
		double xx = start.x + arrowLength * Math.cos(Math.toRadians(angleTotal));
		double yx = start.y + arrowLength * Math.sin(Math.toRadians(angleTotal));
		drawer.drawLine(start.x, start.y, (float)xx, (float)yx);
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public boolean isGlobal() {
		return true;
	}

	public List<Line> getLinesFromConfig(PropertiesConfig config) {
		RelationPoints rp = ((SettingsRelation) config.getSettings()).getRelationPoints();
		return rp.getRelationPointLines();
	}
}
