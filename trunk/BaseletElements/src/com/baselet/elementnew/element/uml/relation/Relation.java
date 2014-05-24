package com.baselet.elementnew.element.uml.relation;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.polygon.NoStickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.element.uml.relation.RelationPoints.Selection;
import com.baselet.elementnew.facet.common.LayerFacet;
import com.baselet.elementnew.facet.relation.RelationLineTypeFacet;
import com.baselet.elementnew.settings.Settings;

public class Relation extends NewGridElement implements Stickable {

	private RelationPoints relationPoints;

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		state.setStickingPolygonGenerator(NoStickingPolygonGenerator.INSTANCE);
		// relationPoints.drawLinesBetweenPoints(drawer);
		if (!state.getFacetResponse(RelationLineTypeFacet.class, false)) {
			RelationLineTypeFacet.drawLineBetweenPoints(drawer, relationPoints, LineType.SOLID);
		}
		relationPoints.resizeRectAndReposPoints(); // line description and relation-endings can change the relation size, therefore recalc it now
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
		relationPoints.drawCirclesAndDragBox(drawer);
	}

	@Override
	public void setAdditionalAttributes(String additionalAttributes) {
		super.setAdditionalAttributes(additionalAttributes);
		RelationPointList pointList = new RelationPointList();
		String[] split = additionalAttributes.split(";");
		for (int i = 0; i < split.length; i += 2) {
			pointList.add(Double.valueOf(split[i]), Double.valueOf(split[i + 1]));
		}
		relationPoints = new RelationPoints(this, pointList);
		if (getHandler().isInitialized()) {
			relationPoints.resizeRectAndReposPoints();
		}
	}

	@Override
	public String getAdditionalAttributes() {
		return relationPoints.toAdditionalAttributesString();
	}

	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDragRelative, boolean isShiftKeyDown, boolean firstDrag, StickableMap stickables) {
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
		boolean isSelectableOn = relationPoints.getSelection(pointAtDefaultZoom(relativePoint)) != Selection.NOTHING;
		return isSelectableOn;
	}

	@Override
	public Collection<PointDoubleIndexed> getStickablePoints() {
		return relationPoints.getStickablePoints();
	}

	@Override
	public List<PointDoubleIndexed> movePoints(List<PointChange> changedStickPoints) {
		List<PointDoubleIndexed> updatedChangedList = relationPoints.movePointAndResizeRectangle(changedStickPoints);
		updateModelFromText();
		return updatedChangedList;
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
