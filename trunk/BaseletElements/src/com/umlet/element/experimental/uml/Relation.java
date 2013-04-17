package com.umlet.element.experimental.uml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.BaseDrawHandler;
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

	private final float SELECTBOXSIZE = 10;
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
			// draw drag-box TODO place position correctly
			int x = Math.abs(points.get(points.size()-2).x - points.get(points.size()-1).x)/2 + points.get(points.size()-2).x;
			int y = Math.abs(points.get(points.size()-2).y - points.get(points.size()-1).y)/2;
			drawer.drawRectangle(x - SELECTBOXSIZE/2, y - SELECTBOXSIZE/2, SELECTBOXSIZE, SELECTBOXSIZE);
		}
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

	private Point currentlyDragging = null;

	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosAfterDrag, boolean isShiftKeyDown) {
		Point mousePosBeforeDrag = new Point(mousePosAfterDrag.x - diffX, mousePosAfterDrag.y - diffY);
		// User is still moving the previously moved point
		if (currentlyDragging != null && checkAndMove(currentlyDragging, mousePosBeforeDrag, diffX, diffY)) {
			return;
		}
		// Otherwise check if another point should be moved
		for (Point relationPoint : points) {
			if (checkAndMove(relationPoint, mousePosBeforeDrag, diffX, diffY)) {
				return;
			}
		}
	}

	private boolean checkAndMove(Point relationPoint, Point mousePosBeforeDrag, int diffX, int diffY) {
		Rectangle rect = toCircleRectangle(relationPoint);
		if (rect.contains(mousePosBeforeDrag)) {
			relationPoint.move(diffX, diffY);
			currentlyDragging = relationPoint;
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
		int radius = SELECTCIRCLERADIUS + 1;
		return new Rectangle(p.x-radius, p.y-radius, radius*2, radius*2);
	}
}

