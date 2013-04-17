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
			drawer.setForegroundColor(ColorOwn.SELECTION_FG);
			drawer.setBackgroundColor(ColorOwn.SELECTION_BG);
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
			repositionRelation();
			updateModelFromText();
			repaint();
			return true;
		}
		return false;
	}

	private void repositionRelation() {
		Rectangle newSize = new Rectangle(0, 0, 0, 0);
		for (Point p : points) {
			newSize.merge(toCircleRectangle(p));
		}
		// move the new bounds to the origin of the element
		newSize.move(getRectangle().getX(), getRectangle().getY());
		setRectangle(newSize);
	}
	
	private Rectangle toCircleRectangle(Point p) {
		return new Rectangle(p.x-SELECTCIRCLERADIUS, p.y-SELECTCIRCLERADIUS, SELECTCIRCLERADIUS*2, SELECTCIRCLERADIUS*2);
	}
}

