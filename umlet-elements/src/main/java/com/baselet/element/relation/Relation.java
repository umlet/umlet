package com.baselet.element.relation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.element.NewGridElement;
import com.baselet.element.UndoInformation;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.relation.facet.RelationLineTypeFacet;
import com.baselet.element.relation.facet.SettingsRelation;
import com.baselet.element.relation.helper.RelationPointHandler;
import com.baselet.element.relation.helper.RelationPointHolder;
import com.baselet.element.relation.helper.RelationPointList;
import com.baselet.element.relation.helper.RelationSelection;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.PointDoubleIndexed;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.polygon.NoStickingPolygonGenerator;

public class Relation extends NewGridElement implements Stickable, RelationPointHolder {

	private RelationPointHandler relationPoints;

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		state.setStickingPolygonGenerator(NoStickingPolygonGenerator.INSTANCE);
	}

	@Override
	protected void resetAndDrawMetaDrawerContent(DrawHandler drawer) {
		ColorOwn currentColor = Theme.getCurrentThemeColor();
		drawer.clearCache();
		drawer.setBackgroundColor(currentColor.getStyleColorMap().get(ColorOwn.ColorStyle.SELECTION_BG));

		// draw rectangle around whole element (basically a helper for developers to make sure the (invisible) size of the element is correct)
		if (SharedConfig.getInstance().isDev_mode()) {
			drawer.setForegroundColor(currentColor.getColorMap().get(ColorOwn.PredefinedColors.TRANSPARENT));
			drawer.drawRectangle(0, 0, getRealSize().getWidth(), getRealSize().getHeight());
			drawer.setBackgroundColor(currentColor.getColorMap().get(ColorOwn.PredefinedColors.GREEN).transparency(Transparency.BACKGROUND));
			relationPoints.drawSelectionSpace(drawer);
		}

		drawer.setForegroundColor(currentColor.getStyleColorMap().get(ColorOwn.ColorStyle.SELECTION_FG));
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
		RelationSelection returnSelection = relationPoints.getSelectionAndMovePointsIfNecessary(pointAtDefaultZoom(mousePosBeforeDragRelative), toDefaultZoom(diffX), toDefaultZoom(diffY), firstDrag);
		if (returnSelection == RelationSelection.DRAG_BOX) {
			setLocationDifference(diffX, diffY);
		}
		if (returnSelection != RelationSelection.NOTHING) {
			updateModelFromText();
		}
		if (undoable) {
			undoStack.add(new UndoInformation(getRectangle(), oldRect, new HashMap<Stickable, List<PointChange>>(), getGridSize(), oldAddAttr, getAdditionalAttributes()));
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
		Point relativePoint = toRelative(point);
		boolean isSelectableOn = relationPoints.getSelection(pointAtDefaultZoom(relativePoint)) != RelationSelection.NOTHING;
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

	protected Point toRelative(Point point) {
		return new Point(point.getX() - getRectangle().getX(), point.getY() - getRectangle().getY());
	}

	@Override
	public CursorOwn getCursor(Point point, Set<Direction> resizeDirections) {
		RelationSelection selection = relationPoints.getSelection(pointAtDefaultZoom(toRelative(point)));
		switch (selection) {
			case DRAG_BOX:
				return CursorOwn.MOVE;
			case LINE:
				return CursorOwn.CROSS;
			case RELATION_POINT:
				return CursorOwn.HAND;
			default:
				return super.getCursor(point, resizeDirections);
		}
	}

}
