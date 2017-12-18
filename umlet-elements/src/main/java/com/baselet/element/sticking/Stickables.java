package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.sticking.StickingPolygon.StickLine;

public class Stickables {

	private static Logger log = LoggerFactory.getLogger(Stickables.class);

	public static StickableMap getStickingPointsWhichAreConnectedToStickingPolygon(StickingPolygon oldStickingPolygon, Collection<? extends Stickable> stickables) {
		int maxDistance = SharedConstants.DEFAULT_GRID_SIZE - 1; // because stickables is always calculated at 10px gridsize, the maxdistance for sticking is 9px (this tolerance is important for diagonal stickinglines like the UseCase has, otherwise 0px tolerance should always work if the stickingLineEnd is always on the exact same Point as the stickingpolygon)
		log.debug("Polygon to check: " + oldStickingPolygon);
		StickableMap returnMap = new StickableMap();
		for (final Stickable stickable : stickables) {
			for (final PointDoubleIndexed p : stickable.getStickablePoints()) {
				PointDouble absolutePointPosition = getAbsolutePosition(stickable, p);
				for (StickLine sl : oldStickingPolygon.getStickLines()) {
					log.trace("CHECK " + sl + "/" + absolutePointPosition + "/" + maxDistance);
					if (sl.isConnected(absolutePointPosition, maxDistance)) {
						returnMap.add(stickable, p);
					}
				}
			}
		}
		return returnMap;
	}

	public static Map<Stickable, List<PointChange>> moveStickPointsBasedOnPolygonChanges(StickingPolygon oldStickingPolygon, StickingPolygon newStickingPolygon, StickableMap stickablePointsToCheck, int maxDistance) {
		// the first drag determines which stickables and which points of them will stick (eg: moving through other relations should NOT "collect" their stickingpoints)
		if (oldStickingPolygon == null || stickablePointsToCheck == null || stickablePointsToCheck.isEmpty()) {
			return Collections.emptyMap(); // if element has no stickingPolygon or no stickables located on it, nothing has to be checked
		}

		// determine which sticklines have changed and only check sticks for them
		List<StickLineChange> changedStickLines = getChangedStickLines(oldStickingPolygon, newStickingPolygon);
		// go through all stickpoints and handle the stickline-change
		Map<Stickable, List<PointChange>> changeList = new HashMap<Stickable, List<PointChange>>();
		for (final Stickable stickable : stickablePointsToCheck.getStickables()) {
			List<PointChange> calculatedStickingPointChanges = calculateStickingPointChanges(stickable, stickablePointsToCheck.getStickablePoints(stickable), changedStickLines, maxDistance);
			if (!calculatedStickingPointChanges.isEmpty()) {
				changeList.put(stickable, calculatedStickingPointChanges);
			}
		}
		applyChanges(changeList, stickablePointsToCheck);
		return changeList;
	}

	public static void applyChanges(Map<Stickable, List<PointChange>> changeList, StickableMap stickablePointsToCheck) {
		for (Entry<Stickable, List<PointChange>> entry : changeList.entrySet()) {
			Stickable stickable = entry.getKey();
			List<PointDoubleIndexed> updatedChangedPoints = stickable.movePoints(entry.getValue());
			if (stickablePointsToCheck != null) {
				stickablePointsToCheck.setStickablePoints(stickable, updatedChangedPoints);
			}
		}
	}

	private static List<StickLineChange> getChangedStickLines(StickingPolygon oldStickingPolygon, StickingPolygon newStickingPolygon) {
		List<StickLineChange> changedStickLines = new ArrayList<StickLineChange>();
		Iterator<StickLine> oldLineIter = oldStickingPolygon.getStickLines().iterator();
		Iterator<StickLine> newLineIter = newStickingPolygon.getStickLines().iterator();
		while (oldLineIter.hasNext()) {
			StickLine oldLine = oldLineIter.next();
			StickLine newLine = newLineIter.next();
			if (!oldLine.equals(newLine)) {
				changedStickLines.add(new StickLineChange(oldLine, newLine));
			}
		}
		return changedStickLines;
	}

	private static List<PointChange> calculateStickingPointChanges(Stickable stickable, List<PointDoubleIndexed> stickablePoints, List<StickLineChange> changedStickLines, int maxDistance) {
		List<PointChange> changedPoints = new ArrayList<PointChange>();
		for (PointDoubleIndexed stickablePoint : stickablePoints) {
			PointDouble absolutePosOfStickablePoint = getAbsolutePosition(stickable, stickablePoint);

			StickLineChange relevantStickline = getNearestStickLineChangeWhichWillChangeTheStickPoint(changedStickLines, absolutePosOfStickablePoint, maxDistance);

			if (relevantStickline != null) {
				PointChange changedPoint = calcPointDiffBasedOnStickLineChange(stickablePoint.getIndex(), absolutePosOfStickablePoint, relevantStickline);
				if (changedPoint.getDiffX() != 0 || changedPoint.getDiffY() != 0) {
					changedPoints.add(changedPoint);
				}
			}
		}
		return changedPoints;
	}

	static PointChange calcPointDiffBasedOnStickLineChange(Integer index, PointDouble stickablePoint, StickLineChange stickline) {
		StickLine oldLine = stickline.getOld();
		StickLine newLine = stickline.getNew();

		int diffX = 0;
		int diffY = 0;

		if (newLine.getDirectionOfLine(true).isHorizontal()) {
			diffY = newLine.getStart().getY().intValue() - oldLine.getStart().getY().intValue();
			diffX = calcOtherCoordinate(stickablePoint, oldLine, newLine, 0, diffY).getX().intValue();
		}
		else {
			diffX = newLine.getStart().getX().intValue() - oldLine.getStart().getX().intValue();
			diffY = calcOtherCoordinate(stickablePoint, oldLine, newLine, diffX, 0).getY().intValue();
		}

		return new PointChange(index, diffX, diffY);
	}

	private static PointDouble calcOtherCoordinate(PointDouble stickablePoint, StickLine oldLine, StickLine newLine, int diffX, int diffY) {
		PointDouble stickablePointWithDiff = new PointDouble(stickablePoint.getX() + diffX, stickablePoint.getY() + diffY);
		// if the line length has not changed, the point must be moved like the line
		if (oldLine.getLength() == newLine.getLength()) {
			return new PointDouble(newLine.getStart().getX().intValue() - oldLine.getStart().getX().intValue(), newLine.getStart().getY().intValue() - oldLine.getStart().getY().intValue());
		}
		// if line length has changed and the changed stickablePoint is not on line anymore, it will move to the nearest newLineend (start or end of line)
		else if (newLine.getDistanceToPoint(stickablePointWithDiff) > 1) {
			PointDouble point;
			if (newLine.getStart().distance(stickablePointWithDiff) < newLine.getEnd().distance(stickablePointWithDiff)) {
				point = newLine.getStart();
			}
			else {
				point = newLine.getEnd();
			}
			return new PointDouble(point.getX() - stickablePoint.getX(), point.getY() - stickablePoint.getY());
		}
		return new PointDouble(0, 0);
	}

	private static StickLineChange getNearestStickLineChangeWhichWillChangeTheStickPoint(List<StickLineChange> changedStickLines, PointDouble absolutePositionOfStickablePoint, int maxDistance) {
		Double lowestDistance = null;
		StickLineChange changeMatchingLowestDistance = null;

		for (StickLineChange change : changedStickLines) {
			double distance = change.getOld().getDistanceToPoint(absolutePositionOfStickablePoint);
			// update best match if this distance is in range and better than the old best match
			if (distance < maxDistance && (lowestDistance == null || distance < lowestDistance)) {
				// if distance to start end end of the stickable line has changed, move the stickable point (avoids unwanted moves (eg stickablepoint in middle and resizing top or bottom -> no move necessary))
				// if ((Line.distanceBetweenTwoPoints(change.getOld().getStart(), absolutePositionOfStickablePoint) != Line.distanceBetweenTwoPoints(change.getNew().getStart(), absolutePositionOfStickablePoint)) &&
				// (Line.distanceBetweenTwoPoints(change.getOld().getEnd(), absolutePositionOfStickablePoint) != Line.distanceBetweenTwoPoints(change.getNew().getEnd(), absolutePositionOfStickablePoint))) {
				lowestDistance = distance;
				changeMatchingLowestDistance = change;
				// }
			}

		}
		return changeMatchingLowestDistance;
	}

	private static PointDouble getAbsolutePosition(Stickable stickable, PointDouble pd) {
		// the points are located relative to the upper left corner of the relation, therefore add this corner to have it located to the upper left corner of the diagram
		int x = stickable.getRealRectangle().getX() + pd.getX().intValue();
		int y = stickable.getRealRectangle().getY() + pd.getY().intValue();
		return new PointDouble(x, y);
	}

}
