package com.umlet.element.experimental.uml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsRelation;

public class Relation extends NewGridElement {

	private final int SELECTBOXSIZE = 5;
	private final int SELECTCIRCLERADIUS = 10;

	/**
	 * Points of this relation (point of origin is the upper left corner of the relation element (not the drawpanel!))
	 */
	private List<Point> points = new ArrayList<Point>();

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		//		properties.drawPropertiesText();

		// draw lines
		for (int i = 1; i < points.size(); i++) {
			Point a = points.get(i-1);
			Point b = points.get(i);
			drawer.drawLine(a.x, a.y, b.x, b.y);
		}
	}

	@Override
	protected void updateMetaDrawer(BaseDrawHandler drawer) {
		drawer.clearCache();
		if (isSelected()) {
			drawer.setBackgroundColor(ColorOwn.SELECTION_BG);
			
			// draw rectangle around whole element (basically a helper for developers and a reminder that the user uses a new element)
			drawer.setForegroundColor(ColorOwn.TRANSPARENT);
			drawer.drawRectangle(0, 0, getRectangle().getWidth(), getRectangle().getHeight());
			
			drawer.setForegroundColor(ColorOwn.SELECTION_FG);
			// draw selection circles
			for (Point p : points) {
				drawer.drawCircle(p.x, p.y, SELECTCIRCLERADIUS);
			}
			drawer.drawRectangle(getDragBox());
		}
	}

	private Rectangle getDragBox() {
		Point begin = points.get(points.size()/2);
		Point end = points.get(points.size()/2-1);
		Point center = new Line(begin, end).getCenter();
		Rectangle rectangle = toRectangle(center, SELECTBOXSIZE);
		return rectangle;
	}

	@Override
	public void setAdditionalAttributes(String additionalAttributes) {
		super.setAdditionalAttributes(additionalAttributes);
		String[] split = additionalAttributes.split(";");
		for (int i = 0; i < split.length; i += 2) {
			points.add(new Point(Integer.valueOf(split[i]), Integer.valueOf(split[i+1])));
		}
	}

	@Override
	public Settings getSettings() {
		return new SettingsRelation();
	}

	/**
	 * currently dragged point is stored to make sure the selection is not changed (eg if dragging over another point)
	 */
	private Point currentlyDraggedRelationPoint = null;

	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosAfterDrag, boolean isShiftKeyDown) {
		Point mousePosBeforeDrag = new Point(mousePosAfterDrag.x - diffX, mousePosAfterDrag.y - diffY);
		
		// if a relation-point is currently moved, it has preference
		if (currentlyDraggedRelationPoint != null && checkAndMoveRelationPoint(currentlyDraggedRelationPoint, mousePosBeforeDrag, diffX, diffY)) {
			return;
		} else {
			currentlyDraggedRelationPoint = null;
		}
		// otherwise the relation-dragbox is checked
		System.out.println(getDragBox() + "/" + mousePosBeforeDrag);
		if (currentlyDraggedRelationPoint == null && getDragBox().contains(mousePosBeforeDrag)) {
			this.setLocationDifference(diffX, diffY);
			updateModelFromText();
			repaint();
		}
		// at last the other relation points are checked
		for (Point relationPoint : points) {
			if (checkAndMoveRelationPoint(relationPoint, mousePosBeforeDrag, diffX, diffY)) {
				return;
			}
		}
	}

	private boolean checkAndMoveRelationPoint(Point relationPoint, Point mousePosBeforeDrag, int diffX, int diffY) {
		Rectangle rect = toCircleRectangle(relationPoint);
		if (rect.contains(mousePosBeforeDrag)) {
			relationPoint.move(diffX, diffY);
			currentlyDraggedRelationPoint = relationPoint;
			repositionRelationAndPointsBasedOnPoints();
			updateModelFromText();
			repaint();
			return true;
		}
		return false;
	}

	private void repositionRelationAndPointsBasedOnPoints() {
		// Calculate new Relation position and size
		Rectangle newSize = null;
		for (Point p : points) {
			Rectangle absoluteRectangle = toCircleRectangle(new Point(p.x + getRectangle().getX(), p.y + getRectangle().getY()));
			if (newSize == null) {
				newSize = absoluteRectangle;
			} else {
				newSize.merge(absoluteRectangle);
			}
		}
		// move relation points to their new position (their position is relative to the relation-position)
		int xDisplacement = Integer.MAX_VALUE;
		int yDisplacement = Integer.MAX_VALUE;
		for (Point p : points) {
			Rectangle r = toCircleRectangle(p);
			xDisplacement = Math.min(xDisplacement, r.getX());
			yDisplacement = Math.min(yDisplacement, r.getY());
		}
		for (Point p : points) {
			p.move(-xDisplacement, -yDisplacement);
		}
		// now apply the new rectangle size
		setRectangle(newSize);
	}

	private Rectangle toCircleRectangle(Point p) {
		return toRectangle(p, SELECTCIRCLERADIUS+1);
	}

	private Rectangle toRectangle(Point p, int size) {
		return new Rectangle(p.x-size, p.y-size, size*2, size*2);
	}
}

