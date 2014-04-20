package com.baselet.elementnew.element.uml.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.geom.GeometricFunctions;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.PointChange;

public class PointDoubleHolderList {
	List<PointDoubleHolder> points = new ArrayList<PointDoubleHolder>();

	public void add(PointDoubleHolder pointDoubleHolder) {
		points.add(pointDoubleHolder);
	}

	public List<PointDoubleHolder> getPointHolders() {
		return Collections.unmodifiableList(points);
	}

	public void addPointOnLine(Line line, PointDoubleHolder roundedPoint) {
		PointDouble endOfLine = line.getStart();
		for (ListIterator<PointDoubleHolder> iter = points.listIterator(); iter.hasNext();) {
			PointDoubleHolder point = iter.next();
			if (point.getPoint().equals(endOfLine)) {
				iter.add(roundedPoint);
				return;
			}
		}
		throw new RuntimeException("Point " + endOfLine + " not found in list " + points);
	}

	public void applyChangesToPoints(List<PointChange> changes) {
		applyPointChange(changes);
		// if there are only 2 points and they would overlap now (therefore the relation would have a size of 0x0px), revert the move
		revertChangesIfOnly2PointsOverlap(changes);
	}

	private void revertChangesIfOnly2PointsOverlap(List<PointChange> changes) {
		if (points.size() == 2 && points.get(0).getPoint().equals(points.get(1).getPoint())) {
			List<PointChange> inverse = new ArrayList<PointChange>();
			for (PointChange change : changes) {
				inverse.add(new PointChange(change.getPoint(), -change.getDiffX(), -change.getDiffY()));
			}
			applyPointChange(inverse);
		}
	}

	private void applyPointChange(List<PointChange> changes) {
		for (PointDoubleHolder p : points) {
			for (PointChange change : changes) {
				if (p.equals(change.getPoint())) {
					p.setPoint(new PointDouble(p.getPoint().getX() + change.getDiffX(), p.getPoint().getY() + change.getDiffY()));
				}
			}
		}
	}

	void moveRelationPointsOriginToUpperLeftCorner() {
		int displacementX = Integer.MAX_VALUE;
		int displacementY = Integer.MAX_VALUE;
		for (PointDoubleHolder p : points) {
			Rectangle r = RelationPointsUtils.toCircleRectangle(p.getPoint());
			displacementX = Math.min(displacementX, r.getX());
			displacementY = Math.min(displacementY, r.getY());
		}
		for (PointDoubleHolder pointHolder : points) {
			PointDouble p = pointHolder.getPoint();
			pointHolder.setPoint(new PointDouble(p.getX() - displacementX, p.getY() - displacementY));
			// If points are off the grid they can be realigned here (use the following 2 lines instead of move())
			//			p.setX(SharedUtils.realignTo(true, p.getX()-displacementX, false, SharedConstants.DEFAULT_GRID_SIZE));
			//			p.setY(SharedUtils.realignTo(true, p.getY()-displacementY, false, SharedConstants.DEFAULT_GRID_SIZE));
		}
	}

	public boolean removeRelationPointIfOnLineBetweenNeighbourPoints() {
		boolean updateNecessary = false;
		if (points.size() > 2) {
			ListIterator<PointDoubleHolder> iter = points.listIterator();
			PointDoubleHolder leftNeighbour = iter.next();
			PointDoubleHolder pointToCheck = iter.next();
			while (iter.hasNext()) {
				PointDoubleHolder rightNeighbour = iter.next();
				// if a point lies on the line between its 2 neighbourpoints, it will be removed
				if (GeometricFunctions.getDistanceBetweenLineAndPoint(leftNeighbour.getPoint(), rightNeighbour.getPoint(), pointToCheck.getPoint()) < 5) {
					updateNecessary = true;
					iter.previous();
					iter.previous();
					iter.remove();
					pointToCheck = iter.next();
				} else {
					leftNeighbour = pointToCheck;
					pointToCheck = rightNeighbour;
				}
			}
		}
		return updateNecessary;
	}

	public List<Line> getRelationPointLines() {
		List<Line> lines = new ArrayList<Line>();
		for (int i = 1; i < points.size(); i++) {
			lines.add(new Line(points.get(i - 1).getPoint(), points.get(i).getPoint()));
		}
		return lines;
	}

	public Line getFirstLine() {
		return new Line(points.get(0).getPoint(), points.get(1).getPoint());
	}

	public Line getLastLine() {
		return new Line(points.get(points.size()-2).getPoint(), points.get(points.size()-1).getPoint());
	}

	public Collection<PointDoubleHolder> getStickablePoints() {
		return Arrays.asList(points.get(0), points.get(points.size()-1));
	}

	public Rectangle getDragBox() {
		PointDoubleHolder begin = points.get(points.size() / 2);
		PointDoubleHolder end = points.get(points.size() / 2 - 1);
		PointDouble center = new Line(begin.getPoint(), end.getPoint()).getCenter();
		Rectangle rectangle = RelationPointsUtils.toRectangle(center, RelationPoints.DRAG_BOX_SIZE/2);
		return rectangle;
	}
	
	public String toAdditionalAttributesString() {
		String returnString = "";
		for (PointDoubleHolder p : points) {
			returnString += p.getPoint().getX() + ";" + p.getPoint().getY() + ";";
		}
		if (!returnString.isEmpty()) {
			returnString = returnString.substring(0, returnString.length()-1);
		}
		return returnString;
	}

	@Override
	public String toString() {
		return "Relationpoints: " + SharedUtils.listToString(",", points);
	}
}
