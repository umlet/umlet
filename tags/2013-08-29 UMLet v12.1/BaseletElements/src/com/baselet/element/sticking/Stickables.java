package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.sticking.StickingPolygon.StickLine;

public class Stickables {
	
	private static Logger log = Logger.getLogger(Stickables.class);

	public static Map<Stickable, Set<PointDouble>> getStickingPointsWhichAreConnectedToStickingPolygon(StickingPolygon oldStickingPolygon, Collection<? extends Stickable> stickables, int maxDistance) {
		log.debug("Polygon to check: " + oldStickingPolygon);
		Map<Stickable, Set<PointDouble>> returnMap = new HashMap<Stickable, Set<PointDouble>>();
		for (final Stickable stickable : stickables) {
			for (final PointDouble p : stickable.getStickablePoints()) {
				PointDouble absolutePointPosition = getAbsolutePosition(stickable, p);
				log.debug("Check if sticks: " + absolutePointPosition);
				for (StickLine sl : oldStickingPolygon.getStickLines()) {
					if (sl.isConnected(absolutePointPosition, maxDistance)) {
						Set<PointDouble> points = returnMap.get(stickable);
						if (points == null) {
							returnMap.put(stickable, new HashSet<PointDouble>(Arrays.asList(p)));
						} else {
							points.add(p);
						}
					}
				}
			}
		}
		log.debug(returnMap.size() + "point sticks to polygon");
		return returnMap;
	}


	public static void moveStickPointsBasedOnPolygonChanges(StickingPolygon oldStickingPolygon, StickingPolygon newStickingPolygon, Map<Stickable, Set<PointDouble>> stickablePointsToCheck, int maxDistance) {
		// determine which sticklines have changed and only check sticks for them
		List<StickLineChange> changedStickLines = getChangedStickLines(oldStickingPolygon, newStickingPolygon);
		// go through all stickpoints and handle the stickline-change
		for (final Stickable stickable : stickablePointsToCheck.keySet()) {
			for (final PointDouble pd : stickablePointsToCheck.get(stickable)) {
				handleStickLineChange(stickable, pd, changedStickLines, maxDistance);
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

	private static void handleStickLineChange(Stickable stickable, PointDouble pd, List<StickLineChange> changedStickLines, int maxDistance) {
		PointDouble absolutePositionOfStickablePoint = getAbsolutePosition(stickable, pd);

		StickLineChange change = getNearestStickLineChangeWhichWilLChangeTheStickPoint(changedStickLines, absolutePositionOfStickablePoint, maxDistance);
		
		if (change != null) {
			PointDouble newPointToUse, oldPointToUse;
			if (change.getNew().isConnected(absolutePositionOfStickablePoint, maxDistance)) {
				newPointToUse = change.getNew().getStart();
				oldPointToUse = change.getOld().getStart();
			} else {
				newPointToUse = change.getNew().getEnd();
				oldPointToUse = change.getOld().getEnd();
			}
			
			final int stickLineDiffX = SharedUtils.realignToGridRoundToNearest(true, newPointToUse.getX()-oldPointToUse.getX());
			final int stickLineDiffY = SharedUtils.realignToGridRoundToNearest(true, newPointToUse.getY()-oldPointToUse.getY());
			stickable.movePoint(pd, stickLineDiffX, stickLineDiffY);
		}
	}

	private static StickLineChange getNearestStickLineChangeWhichWilLChangeTheStickPoint(List<StickLineChange> changedStickLines, PointDouble absolutePositionOfStickablePoint, int maxDistance) {
		Double lowestDistance = null;
		StickLineChange changeMatchingLowestDistance = null;

		for (StickLineChange change : changedStickLines) {
			double distance = change.getOld().getDistanceToPoint(absolutePositionOfStickablePoint);
			// update best match if this distance is in range and better than the old best match
			if (distance < maxDistance && (lowestDistance == null || distance < lowestDistance)) {
				// if distance to start end end of the stickable line has changed, move the stickable point (avoids unwanted moves (eg stickablepoint in middle and resizing top or bottom -> no move necessary))
//				if ((Line.distanceBetweenTwoPoints(change.getOld().getStart(), absolutePositionOfStickablePoint) != Line.distanceBetweenTwoPoints(change.getNew().getStart(), absolutePositionOfStickablePoint)) &&
//						(Line.distanceBetweenTwoPoints(change.getOld().getEnd(), absolutePositionOfStickablePoint) != Line.distanceBetweenTwoPoints(change.getNew().getEnd(), absolutePositionOfStickablePoint))) {
					lowestDistance = distance;
					changeMatchingLowestDistance = change;
//				}
			}

		}
		return changeMatchingLowestDistance;
	}

	private static PointDouble getAbsolutePosition(Stickable stickable, PointDouble pd) {
		// the points are located relative to the upper left corner of the relation, therefore add this corner to have it located to the upper left corner of the diagram
		return new PointDouble(stickable.getRectangle().getX() + (int) pd.x, stickable.getRectangle().getY() + (int) pd.y);
	}

}
