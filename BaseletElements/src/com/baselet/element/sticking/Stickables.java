package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.SharedConstants;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.sticking.StickingPolygon.StickLine;
import com.baselet.elementnew.element.uml.relation.PointDoubleIndexed;

public class Stickables {

	private static Logger log = Logger.getLogger(Stickables.class);

	public static StickableMap getStickingPointsWhichAreConnectedToStickingPolygon(StickingPolygon oldStickingPolygon, Collection<? extends Stickable> stickables, int maxDistance) {
		log.debug("Polygon to check: " + oldStickingPolygon);
		StickableMap returnMap = new StickableMap();
		for (final Stickable stickable : stickables) {
			for (final PointDoubleIndexed p : stickable.getStickablePoints()) {
				PointDouble absolutePointPosition = getAbsolutePosition(stickable, p);
				log.debug("Check if sticks: " + absolutePointPosition);
				for (StickLine sl : oldStickingPolygon.getStickLines()) {
					if (sl.isConnected(absolutePointPosition, maxDistance)) {
						returnMap.add(stickable, p);
					}
				}
			}
		}
		return returnMap;
	}

	public static void moveStickPointsBasedOnPolygonChanges(StickingPolygon oldStickingPolygon, StickingPolygon newStickingPolygon, StickableMap stickablePointsToCheck, int maxDistance) {
		// determine which sticklines have changed and only check sticks for them
		List<StickLineChange> changedStickLines = getChangedStickLines(oldStickingPolygon, newStickingPolygon);
		// go through all stickpoints and handle the stickline-change
		for (final Stickable stickable : stickablePointsToCheck.getStickables()) {
			List<PointChange> calculatedStickingPointChanges = calculateStickingPointChanges(stickable, stickablePointsToCheck.getStickablePoints(stickable), changedStickLines, maxDistance);
			if (!calculatedStickingPointChanges.isEmpty()) {
				List<PointDoubleIndexed> updatedChangedPoints = stickable.movePoints(calculatedStickingPointChanges);
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
				StickLine oldLine = relevantStickline.getOld();
				StickLine newLine = relevantStickline.getNew();
				boolean stickLineMoved = oldLine.getLength() == newLine.getLength(); // a stickline is only moved of the length stays the same
				boolean newStickLineLosingPoint = newLine.getDistanceToPoint(absolutePosOfStickablePoint) > 1; // a stickline would lose a point if the distance of the new line to the point is larger than 0 (1 is used as a small buffer for rounding)
				// Move the stickpoint if the stickline was moved (user moves the element) or if the stickline was resized (user resizes the element) and the point would be lost du to resizing
				// Intended behavior for user: Move Command always moves sticking points, Resize Command only moves if the point would be lost otherwise
				if (stickLineMoved || newStickLineLosingPoint) {
					PointDouble newPointToUse, oldPointToUse;
					// To calculate the diffs use the start or end of oldLine and newLine, whichever is nearer to the stickingpoint
					// Necessary to make sticking work even in edge cases like a autoresize element of 500px width where a relation is attached to 20px and another one to 480px. now remove a large portion of the text to let it shrink by a large amount at once -> the relations should correctly move with the new size
					if (oldLine.getStart().distance(absolutePosOfStickablePoint) < oldLine.getEnd().distance(absolutePosOfStickablePoint)) {
						newPointToUse = newLine.getStart();
						oldPointToUse = oldLine.getStart();
					}
					else {
						newPointToUse = newLine.getEnd();
						oldPointToUse = oldLine.getEnd();
					}

					int diffX = newPointToUse.getX().intValue() - oldPointToUse.getX().intValue();
					int diffY = newPointToUse.getY().intValue() - oldPointToUse.getY().intValue();

					// the diff values are in current zoom, therefore normalize them (invert operation done in getAbsolutePosition())
					int diffXdefaultZoom = diffX / stickable.getGridSize() * SharedConstants.DEFAULT_GRID_SIZE;
					int diffYdefaultZoom = diffY / stickable.getGridSize() * SharedConstants.DEFAULT_GRID_SIZE;

					changedPoints.add(new PointChange(stickablePoint.getIndex(), diffXdefaultZoom, diffYdefaultZoom));
				}
			}
		}
		return changedPoints;
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
		int x = stickable.getRectangle().getX() + pd.getX().intValue() * stickable.getGridSize() / SharedConstants.DEFAULT_GRID_SIZE;
		int y = stickable.getRectangle().getY() + pd.getY().intValue() * stickable.getGridSize() / SharedConstants.DEFAULT_GRID_SIZE;
		return new PointDouble(x, y);
	}

}
