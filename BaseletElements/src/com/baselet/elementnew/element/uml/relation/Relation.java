package com.baselet.elementnew.element.uml.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.sticking.Stickable;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.element.uml.relation.RelationPoints.Selection;
import com.baselet.elementnew.facet.common.LayerFacet;
import com.baselet.elementnew.settings.Settings;

public class Relation extends NewGridElement implements Stickable {

	private RelationPoints relationPoints;

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		relationPoints.drawLinesBetweenPoints(drawer);
	}

	@Override
	protected void resetMetaDrawer(DrawHandler drawer) {
		drawer.clearCache();
		drawer.setBackgroundColor(ColorOwn.SELECTION_BG);

		// draw rectangle around whole element (basically a helper for developers to make sure the (invisible) size of the element is correct)
		if (SharedConstants.dev_mode) {
			drawer.setForegroundColor(ColorOwn.TRANSPARENT);
			drawer.drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());
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
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, Collection<? extends Stickable> stickables) {
		Point mousePosBeforeDragRelative = new Point(mousePosBeforeDrag.getX() - getRectangle().getX(), mousePosBeforeDrag.getY() - getRectangle().getY());
		Selection returnSelection = relationPoints.getSelectionAndMovePointsIfNecessary(pointAtDefaultZoom(mousePosBeforeDragRelative), toDefaultZoom(diffX), toDefaultZoom(diffY), firstDrag);
		if (returnSelection == Selection.DRAG_BOX) {
			setLocationDifference(diffX, diffY);
		}
		if (returnSelection != Selection.NOTHING) {
			updateModelFromText();
		}
	}

	/**
	 * Calculate the point for DEFAULT_ZOOM to allow ignoring zoom-level from now on
	 */
	private Point pointAtDefaultZoom(Point p) {
		return new Point(toDefaultZoom(p.getX()), toDefaultZoom(p.getY()));
	}

	private int toDefaultZoom(int input) {
		return input * SharedConstants.DEFAULT_GRID_SIZE / getGridSize();
	}

	@Override
	public void dragEnd() {
		boolean updateNecessary = relationPoints.removeRelationPointIfOnLineBetweenNeighbourPoints();
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
		return relationPoints.getSelection(pointAtDefaultZoom(relativePoint)) != Selection.NOTHING;
	}

	@Override
	public Collection<PointDouble> getStickablePoints() {
		return relationPoints.getStickablePoints();
	}

	@Override
	public void movePoint(PointDouble pointToMove, int diffX, int diffY) {
		relationPoints.movePointAndResizeRectangle(pointToMove, diffX, diffY);
		updateModelFromText();
	}

	@Override
	public Integer getLayer() {
		return state.getFacetResponse(LayerFacet.class, LayerFacet.DEFAULT_VALUE_RELATION);
	}

	@Override
	protected Settings createSettings() {
		return new SettingsRelation() {
			@Override
			public RelationPoints getRelationPoints() {
				return relationPoints;
			}
		};
	}
}

