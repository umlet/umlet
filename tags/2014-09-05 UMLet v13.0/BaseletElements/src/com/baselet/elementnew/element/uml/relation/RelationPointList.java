package com.baselet.elementnew.element.uml.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.geom.GeometricFunctions;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.PointChange;

public class RelationPointList {
	List<RelationPoint> points = new ArrayList<RelationPoint>();
	Map<Integer, Rectangle> textBoxSpaces = new HashMap<Integer, Rectangle>();

	public void add(double x, double y) {
		points.add(new RelationPoint(points.size(), x, y));
	}

	public List<RelationPoint> getPointHolders() {
		return Collections.unmodifiableList(points);
	}

	public PointDoubleIndexed addPointOnLine(Line line, double x, double y) {
		RelationPoint newPoint = null;
		PointDouble endOfLine = line.getStart();
		for (ListIterator<RelationPoint> iter = points.listIterator(); iter.hasNext();) {
			PointDoubleIndexed point = iter.next().getPoint();
			if (point.equals(endOfLine)) {
				newPoint = new RelationPoint(iter.nextIndex(), x, y);
				iter.add(newPoint);
			}
		}
		rebuildpointIndexes();
		if (newPoint == null) {
			throw new RuntimeException("Point " + endOfLine + " not found in list " + points);
		}
		return newPoint.getPoint();
	}

	private void rebuildpointIndexes() {
		List<RelationPoint> rebuiltList = new ArrayList<RelationPoint>();
		for (int i = 0; i < points.size(); i++) {
			rebuiltList.add(new RelationPoint(i, points.get(i).getPoint().getX(), points.get(i).getPoint().getY()));
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
		if (points.size() == 2 && points.get(0).getPoint().getX().equals(points.get(1).getPoint().getX()) && points.get(0).getPoint().getY().equals(points.get(1).getPoint().getY())) {
			List<PointChange> inverse = new ArrayList<PointChange>();
			for (PointChange change : changes) {
				inverse.add(new PointChange(change.getIndex(), -change.getDiffX(), -change.getDiffY()));
			}
			applyPointChange(inverse);
		}
	}

	private void applyPointChange(List<PointChange> changes) {
		for (ListIterator<RelationPoint> iter = points.listIterator(); iter.hasNext();) {
			RelationPoint p = iter.next();
			PointDoubleIndexed pt = p.getPoint();
			for (PointChange change : changes) {
				if (pt.getIndex().equals(change.getIndex())) {
					iter.set(new RelationPoint(pt.getIndex(), pt.getX() + change.getDiffX(), pt.getY() + change.getDiffY(), p.getSize()));
				}
			}
		}
	}

	void moveRelationPointsAndTextSpacesByToUpperLeftCorner() {
		Rectangle rect = createRectangleContainingAllPointsAndTextSpace();
		int displacementX = SharedUtils.realignToGrid(false, rect.getX(), false);
		int displacementY = SharedUtils.realignToGrid(false, rect.getY(), false);
		moveRelationPointsAndTextSpacesBy(-displacementX, -displacementY);
	}

	void moveRelationPointsAndTextSpacesBy(int displacementX, int displacementY) {
		for (ListIterator<RelationPoint> iter = points.listIterator(); iter.hasNext();) {
			RelationPoint p = iter.next();
			iter.set(new RelationPoint(p.getPoint().getIndex(), p.getPoint().getX() + displacementX, p.getPoint().getY() + displacementY, p.getSize()));
			// If points are off the grid they can be realigned here (use the following 2 lines instead of move())
			// p.setX(SharedUtils.realignTo(true, p.getX()-displacementX, false, SharedConstants.DEFAULT_GRID_SIZE));
			// p.setY(SharedUtils.realignTo(true, p.getY()-displacementY, false, SharedConstants.DEFAULT_GRID_SIZE));
		}
		for (Entry<Integer, Rectangle> textSpace : textBoxSpaces.entrySet()) {
			Rectangle old = textSpace.getValue();
			textSpace.setValue(new Rectangle(old.getX() + displacementX, old.getY() + displacementY, old.getWidth(), old.getHeight()));
		}
	}

	public boolean removeRelationPointIfOnLineBetweenNeighbourPoints() {
		boolean updateNecessary = false;
		if (points.size() > 2) {
			ListIterator<RelationPoint> iter = points.listIterator();
			PointDoubleIndexed leftNeighbour = iter.next().getPoint();
			PointDoubleIndexed pointToCheck = iter.next().getPoint();
			while (iter.hasNext()) {
				PointDoubleIndexed rightNeighbour = iter.next().getPoint();
				// if a point lies on the line between its 2 neighbourpoints, it will be removed
				if (GeometricFunctions.getDistanceBetweenLineAndPoint(leftNeighbour, rightNeighbour, pointToCheck) < 5) {
					updateNecessary = true;
					iter.previous();
					iter.previous();
					iter.remove();
					pointToCheck = iter.next().getPoint();
				}
				else {
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
			lines.add(new Line(points.get(i - 1).getPoint(), points.get(i).getPoint()));
		}
		return lines;
	}

	public Line getFirstLine() {
		return new Line(points.get(0).getPoint(), points.get(1).getPoint());
	}

	public Line getLastLine() {
		return new Line(points.get(points.size() - 2).getPoint(), points.get(points.size() - 1).getPoint());
	}

	public Collection<PointDoubleIndexed> getStickablePoints() {
		return Arrays.asList(points.get(0).getPoint(), points.get(points.size() - 1).getPoint());
	}

	public Rectangle getDragBox() {
		PointDoubleIndexed begin = points.get(points.size() / 2).getPoint();
		PointDoubleIndexed end = points.get(points.size() / 2 - 1).getPoint();
		PointDouble center = new Line(begin, end).getCenter();
		Rectangle rectangle = RelationPointHandlerUtils.toRectangle(center, RelationPointHandler.DRAG_BOX_SIZE / 2);
		return rectangle;
	}

	public String toAdditionalAttributesString() {
		String returnString = "";
		for (RelationPoint p : points) {
			returnString += p.getPoint().getX() + ";" + p.getPoint().getY() + ";";
		}
		if (!returnString.isEmpty()) {
			returnString = returnString.substring(0, returnString.length() - 1);
		}
		return returnString;
	}

	@Override
	public String toString() {
		return "Relationpoints: " + SharedUtils.listToString(",", points);
	}

	public PointDoubleIndexed get(int index) {
		return points.get(index).getPoint();
	}

	public void setSize(int index, Rectangle size) {
		for (RelationPoint p : points) {
			if (p.getPoint().getIndex() == index) {
				p.setSize(size);
				return;
			}
		}
		throw new RuntimeException("Unknown Point Index " + index);
	}

	public void setTextBox(int index, Rectangle rect) {
		if (rect == null) {
			textBoxSpaces.remove(index);
		}
		else {
			textBoxSpaces.put(index, rect);
		}
	}

	public Set<Integer> getTextBoxIndexes() {
		return textBoxSpaces.keySet();
	}

	public Rectangle createRectangleContainingAllPointsAndTextSpace() {
		Rectangle rectangleContainingAllPointsAndTextSpace = null;
		for (RelationPoint p : points) {
			rectangleContainingAllPointsAndTextSpace = addWithNullCheck(rectangleContainingAllPointsAndTextSpace, p.getSizeAbsolute());
		}
		for (Rectangle textSpace : textBoxSpaces.values()) {
			rectangleContainingAllPointsAndTextSpace = addWithNullCheck(rectangleContainingAllPointsAndTextSpace, textSpace);
		}
		return rectangleContainingAllPointsAndTextSpace;
	}

	private Rectangle addWithNullCheck(Rectangle rectangleContainingAllPointsAndTextSpace, Rectangle rectangle) {
		if (rectangleContainingAllPointsAndTextSpace == null) {
			rectangleContainingAllPointsAndTextSpace = rectangle;
		}
		else {
			rectangleContainingAllPointsAndTextSpace.merge(rectangle);
		}
		return rectangleContainingAllPointsAndTextSpace;
	}
}
