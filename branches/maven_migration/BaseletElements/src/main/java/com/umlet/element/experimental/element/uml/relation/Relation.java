package com.umlet.element.experimental.element.uml.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.sticking.Stickable;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.element.uml.relation.RelationPoints.Selection;
import com.umlet.element.experimental.settings.Settings;

public class Relation extends NewGridElement implements Stickable {

	private RelationPoints relationPoints;

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		relationPoints.drawLinesBetweenPoints(drawer);
	}

	@Override
	protected void resetMetaDrawer(BaseDrawHandler drawer) {
		drawer.clearCache();
		drawer.setBackgroundColor(ColorOwn.SELECTION_BG);

		// draw rectangle around whole element (basically a helper for developers to make sure the (invisible) size of the element is correct)
		if (SharedConstants.isDevMode) {
			drawer.setForegroundColor(ColorOwn.TRANSPARENT);
			drawer.drawRectangle(0, 0, getRectangle().getWidth(), getRectangle().getHeight());
		}

		drawer.setForegroundColor(ColorOwn.SELECTION_FG);
		relationPoints.drawPointCircles(drawer);
		relationPoints.drawDragBox(drawer);
	}

	@Override
	public void setAdditionalAttributes(String additionalAttributes) {
		super.setAdditionalAttributes(additionalAttributes);
		List<PointDouble> pointList = new ArrayList<PointDouble>();
		String[] split = additionalAttributes.split(";");
		for (int i = 0; i < split.length; i += 2) {
			pointList.add(new PointDouble(Double.valueOf(split[i]), Double.valueOf(split[i+1])));
		}
		relationPoints = new RelationPoints(this, pointList);
	}

	@Override
	public String getAdditionalAttributes() {
		return relationPoints.toAdditionalAttributesString();
	}

	@Override
	protected Settings createSettings() {
		return new SettingsRelation(relationPoints);
	}

	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, Collection<? extends Stickable> stickables) {
		Point mousePosBeforeDragRelative = new Point(mousePosBeforeDrag.getX() - getRectangle().getX(), mousePosBeforeDrag.getY() - getRectangle().getY());
		Selection returnSelection = relationPoints.getSelectionAndApplyChanges(mousePosBeforeDragRelative, diffX, diffY, this, firstDrag);
		if (returnSelection != Selection.NOTHING) {
			updateModelFromText();
		}
	}

	@Override
	public void dragEnd() {
		boolean updateNecessary = relationPoints.removeRelationPointOfCurrentDragIfItOverlaps();
		if (updateNecessary) {
			updateModelFromText();
		}
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>();
	}

	@Override
	public boolean isSelectableOn(Point point) {
		Point relativePoint = new Point(point.getX() - getRectangle().getX(), point.getY() - getRectangle().getY());
		return relationPoints.getSelection(relativePoint) != Selection.NOTHING;
	}

	public Collection<PointDouble> getStickablePoints() {
		return relationPoints.getStickablePoints();
	}

	@Override
	public void movePoint(PointDouble pointToMove, int diffX, int diffY) {
		relationPoints.movePointAndResizeRectangle(pointToMove, diffX, diffY);
		updateModelFromText();
	}

}

