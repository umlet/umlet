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

	void drawArrowToLine(BaseDrawHandler drawer, Line line, boolean arrowOnLineStart) {
		Point point = arrowOnLineStart ? line.getStart() : line.getEnd();
		double angleOfSlopeOfLine = line.getAngleOfSlope();
		int angle = arrowOnLineStart ? 150 : 30;
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
	public boolean checkStart(String line) {
		return line.equals(getKey());
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public boolean isGlobal() {
		return true;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {new AutocompletionText(getKey(), getDescription(), isGlobal())};
	}
	
	public List<Line> getLinesFromConfig(PropertiesConfig config) {
		RelationPoints rp = ((SettingsRelation) config.getSettings()).getRelationPoints();
		return rp.getRelationPointLines();
	}

	public abstract String getKey();
	public abstract String getDescription();
}
