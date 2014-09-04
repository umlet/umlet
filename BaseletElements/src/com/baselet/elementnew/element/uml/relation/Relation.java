package com.baselet.elementnew.element.uml.relation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.SharedConstants;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.UndoInformation;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.polygon.NoStickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.element.uml.relation.RelationPointHandler.Selection;
import com.baselet.elementnew.facet.common.LayerFacet;
import com.baselet.elementnew.facet.relation.LineDescriptionFacet;
import com.baselet.elementnew.facet.relation.LineDescriptionFacet.LineDescriptionFacetResponse;
import com.baselet.elementnew.facet.relation.RelationLineTypeFacet;
import com.baselet.elementnew.settings.Settings;

public class Relation extends NewGridElement implements Stickable {

	private RelationPointHandler relationPoints;

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		state.setStickingPolygonGenerator(NoStickingPolygonGenerator.INSTANCE);
		if (!state.getFacetResponse(RelationLineTypeFacet.class, false)) {
			RelationLineTypeFacet.drawDefaultLineAndArrows(drawer, relationPoints);
		}

		// all unused textboxes must be reset to default size (to make sure the relation size is correct even if LineDescriptionFacet is never called)
		LineDescriptionFacetResponse lineDescriptionFacetResponse = state.getFacetResponse(LineDescriptionFacet.class, new LineDescriptionFacetResponse());
		relationPoints.resetTextBoxIndexesExcept(lineDescriptionFacetResponse.getAlreadysetIndexes());
	}

	@Override
	protected void resetMetaDrawer(DrawHandler drawer) {
		drawer.clearCache();
		drawer.setBackgroundColor(ColorOwn.SELECTION_BG);

		// draw rectangle around whole element (basically a helper for developers to make sure the (invisible) size of the element is correct)
		if (SharedConstants.dev_mode) {
			drawer.setForegroundColor(ColorOwn.TRANSPARENT);
			drawer.drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());
			drawer.setBackgroundColor(ColorOwn.GREEN.transparency(Transparency.BACKGROUND));
			relationPoints.drawSelectionSpace(drawer);
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
		relationPoints = new RelationPointHandler(this, pointList);
		if (getHandler().isInitialized()) {
			relationPoints.resizeRectAndReposPoints();
		}
	}

	@Override
	public String getAdditionalAttributes() {
		return relationPoints.toAdditionalAttributesString();
	}

	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDragRelative, boolean isShiftKeyDown, boolean firstDrag, StickableMap stickables, boolean undoable) {
		String oldAddAttr = getAdditionalAttributes();
		Rectangle oldRect = getRectangle();
		Selection returnSelection = relationPoints.getSelectionAndMovePointsIfNecessary(pointAtDefaultZoom(mousePosBeforeDragRelative), toDefaultZoom(diffX), toDefaultZoom(diffY), firstDrag);
		if (returnSelection == Selection.DRAG_BOX) {
			setLocationDifference(diffX, diffY);
		}
		if (returnSelection != Selection.NOTHING) {
			updateModelFromText();
		}
		if (undoable) {
			undoStack.add(new UndoInformation(getRectangle(), oldRect, new HashMap<Stickable, List<PointChange>>(), getGridSize(), oldAddAttr));
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
			public RelationPointHandler getRelationPoints() {
				return relationPoints;
			}
		};
	}

	@Override
	protected void drawError(DrawHandler drawer, String errorText) {
		super.drawError(drawer, errorText.replace(">>", "\\>>").replace("<<", "\\<<"));
		RelationLineTypeFacet.drawDefaultLineAndArrows(drawer, relationPoints);
	}

}
