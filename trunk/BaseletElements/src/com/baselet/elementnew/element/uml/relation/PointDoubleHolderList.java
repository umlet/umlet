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
	List<PointDoubleIndexed> points = new ArrayList<PointDoubleIndexed>();

	public void add(double x, double y) {
		points.add(new PointDoubleIndexed(points.size(), x, y));
	}

	public List<PointDoubleIndexed> getPointHolders() {
		return Collections.unmodifiableList(points);
	}

	public PointDoubleIndexed addPointOnLine(Line line, double x, double y) {
		PointDoubleIndexed newPoint = null;
		PointDouble endOfLine = line.getStart();
		for (ListIterator<PointDoubleIndexed> iter = points.listIterator(); iter.hasNext();) {
			PointDoubleIndexed point = iter.next();
			if (point.equals(endOfLine)) {
				newPoint = new PointDoubleIndexed(iter.nextIndex(), x, y);
				iter.add(newPoint);
			}
		}
		rebuildpointIndexes();
		if (newPoint == null) {
			throw new RuntimeException("Point " + endOfLine + " not found in list " + points);
		}
		return newPoint;
	}

	private void rebuildpointIndexes() {
		List<PointDoubleIndexed> rebuiltList = new ArrayList<PointDoubleIndexed>();
		for (int i = 0; i < points.size(); i++) {
			rebuiltList.add(new PointDoubleIndexed(i, points.get(i).getX(), points.get(i).getY()));
		}
		points.clear();
		points.addAll(rebuiltList);
	}

	public void applyChangesToPoints(List<PointChange> changes) {
		applyPointChange(changes);
		// if there are only 2 points and they would overlap now (therefore the relation would have a size of 0x0px), revert the move
		revertChangesIfOnly2PointsOverlap(changes);
	}

	private void revertChangesIfOnly2PointsOverlap(List<PointChange> changes) {
		if (points.size() == 2 && points.get(0).equals(points.get(1))) {
			List<PointChange> inverse = new ArrayList<PointChange>();
			for (PointChange change : changes) {
				inverse.add(new PointChange(change.getPointHolder(), -change.getDiffX(), -change.getDiffY()));
			}
			applyPointChange(inverse);
		}
	}

	private void applyPointChange(List<PointChange> changes) {
		for (ListIterator<PointDoubleIndexed> iter = points.listIterator(); iter.hasNext();) {
			PointDoubleIndexed p = iter.next();
			for (PointChange change : changes) {
				if (p.equals(change.getPointHolder())) {
					iter.set(new PointDoubleIndexed(p.getIndex(), p.getX() + change.getDiffX(), p.getY() + change.getDiffY()));
				}
			}
		}
	}

	void moveRelationPointsOriginToUpperLeftCorner() {
		int displacementX = Integer.MAX_VALUE;
		int displacementY = Integer.MAX_VALUE;
		for (PointDoubleIndexed p : points) {
			Rectangle r = RelationPointsUtils.toCircleRectangle(p);
			displacementX = Math.min(displacementX, r.getX());
			displacementY = Math.min(displacementY, r.getY());
		}
		for (ListIterator<PointDoubleIndexed> iter = points.listIterator(); iter.hasNext();) {
			PointDoubleIndexed p = iter.next();
			iter.set(new PointDoubleIndexed(p.getIndex(), p.getX() - displacementX, p.getY() - displacementY));
			// If points are off the grid they can be realigned here (use the following 2 lines instead of move())
			//			p.setX(SharedUtils.realignTo(true, p.getX()-displacementX, false, SharedConstants.DEFAULT_GRID_SIZE));
			//			p.setY(SharedUtils.realignTo(true, p.getY()-displacementY, false, SharedConstants.DEFAULT_GRID_SIZE));
		}
	}

	public boolean removeRelationPointIfOnLineBetweenNeighbourPoints() {
		boolean updateNecessary = false;
		if (points.size() > 2) {
			ListIterator<PointDoubleIndexed> iter = points.listIterator();
			PointDoubleIndexed leftNeighbour = iter.next();
			PointDoubleIndexed pointToCheck = iter.next();
			while (iter.hasNext()) {
				PointDoubleIndexed rightNeighbour = iter.next();
				// if a point lies on the line between its 2 neighbourpoints, it will be removed
				if (GeometricFunctions.getDistanceBetweenLineAndPoint(leftNeighbour, rightNeighbour, pointToCheck) < 5) {
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
		if (updateNecessary) {
			rebuildpointIndexes();
		}
		return updateNecessary;
	}

	public List<Line> getRelationPointLines() {
		List<Line> lines = new ArrayList<Line>();
		for (int i = 1; i < points.size(); i++) {
			lines.add(new Line(points.get(i - 1), points.get(i)));
		}
		return lines;
	}

	public Line getFirstLine() {
		return new Line(points.get(0), points.get(1));
	}

	public Line getLastLine() {
		return new Line(points.get(points.size()-2), points.get(points.size()-1));
	}

	public Collection<PointDoubleIndexed> getStickablePoints() {
		return Arrays.asList(points.get(0), points.get(points.size()-1));
	}

	public Rectangle getDragBox() {
		PointDoubleIndexed begin = points.get(points.size() / 2);
		PointDoubleIndexed end = points.get(points.size() / 2 - 1);
		PointDouble center = new Line(begin, end).getCenter();
		Rectangle rectangle = RelationPointsUtils.toRectangle(center, RelationPoints.DRAG_BOX_SIZE/2);
		return rectangle;
	}

	public String toAdditionalAttributesString() {
		String returnString = "";
		for (PointDoubleIndexed p : points) {
			returnString += p.getX() + ";" + p.getY() + ";";
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

	public PointDoubleIndexed get(int index) {
		return points.get(index);
	}
}
