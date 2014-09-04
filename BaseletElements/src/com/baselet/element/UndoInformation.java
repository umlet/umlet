package com.baselet.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.Stickable;

public class UndoInformation {

	private final Rectangle invertedDiffRect;
	private final Map<Stickable, List<PointChange>> invertedStickableMoves;
	private final String additionalAttributes;

	private UndoInformation(Rectangle invertedDiffRect, Map<Stickable, List<PointChange>> invertedStickableMoves, String additionalAttributes) {
		this.invertedDiffRect = invertedDiffRect;
		this.invertedStickableMoves = invertedStickableMoves;
		this.additionalAttributes = additionalAttributes;
	}

	public UndoInformation(Rectangle invertedDiffRect, Map<Stickable, List<PointChange>> stickableMoves, int gridSize, String additionalAttributes) {
		this(toMinZoom(invertedDiffRect, gridSize), updateStickableMoves(stickableMoves, true), additionalAttributes);
	}

	private static Map<Stickable, List<PointChange>> updateStickableMoves(Map<Stickable, List<PointChange>> stickableMoves, boolean invert) {
		Map<Stickable, List<PointChange>> invertedMap = new HashMap<Stickable, List<PointChange>>();
		for (Entry<Stickable, List<PointChange>> entry : stickableMoves.entrySet()) {
			List<PointChange> invList = new ArrayList<PointChange>();
			for (PointChange p : entry.getValue()) {
				if (invert) {
					invList.add(new PointChange(p.getIndex(), -p.getDiffX(), -p.getDiffY()));
				}
				else {
					invList.add(new PointChange(p.getIndex(), p.getDiffX(), p.getDiffY()));
				}
			}
			invertedMap.put(entry.getKey(), invList);
		}
		return invertedMap;
	}

	public Rectangle getInvertedDiffRectangle(int gridSize) {
		return toCurrentZoom(invertedDiffRect, gridSize);
	}

	public Map<Stickable, List<PointChange>> getInvertedStickableMoves() {
		return updateStickableMoves(invertedStickableMoves, false);
	}

	public String getAdditionalAttributes() {
		return additionalAttributes;
	}

	private static Rectangle toMinZoom(Rectangle rectangle, int gridSize) {
		int xBefore = toMinZoom(rectangle.getX(), gridSize);
		int yBefore = toMinZoom(rectangle.getY(), gridSize);
		int wBefore = toMinZoom(rectangle.getWidth(), gridSize);
		int hBefore = toMinZoom(rectangle.getHeight(), gridSize);
		return new Rectangle(xBefore, yBefore, wBefore, hBefore);
	}

	private static int toMinZoom(int val, int gridSize) {
		return val / gridSize;
	}

	private static Rectangle toCurrentZoom(Rectangle rectangle, int gridSize) {
		int xBefore = toCurrentZoom(rectangle.getX(), gridSize);
		int yBefore = toCurrentZoom(rectangle.getY(), gridSize);
		int wBefore = toCurrentZoom(rectangle.getWidth(), gridSize);
		int hBefore = toCurrentZoom(rectangle.getHeight(), gridSize);
		return new Rectangle(xBefore, yBefore, wBefore, hBefore);
	}

	private static int toCurrentZoom(int val, int gridSize) {
		return val * gridSize;
	}

	public UndoInformation merge(UndoInformation other) {
		Rectangle mergedUndoDiffRect = invertedDiffRect.add(other.invertedDiffRect);
		Map<Stickable, List<PointChange>> mergedMap = new HashMap<Stickable, List<PointChange>>();
		mergeStickableMoves(mergedMap, invertedStickableMoves);
		mergeStickableMoves(mergedMap, other.invertedStickableMoves);
		return new UndoInformation(mergedUndoDiffRect, mergedMap, other.additionalAttributes);

	}

	private void mergeStickableMoves(Map<Stickable, List<PointChange>> targetMap, Map<Stickable, List<PointChange>> sourceMap) {
		for (Entry<Stickable, List<PointChange>> sourceEntry : sourceMap.entrySet()) {
			Stickable sourceStickable = sourceEntry.getKey();
			List<PointChange> sourceChangeList = sourceEntry.getValue();
			List<PointChange> targetPointChanges = targetMap.get(sourceStickable);
			if (targetPointChanges == null) { // stickable was not moved before in targetMap
				targetMap.put(sourceStickable, sourceChangeList);
			}
			else { // stickable was already moved and these moves must be merged
				mergeSourceToTarget(sourceChangeList, targetPointChanges);
			}
		}
	}

	private void mergeSourceToTarget(List<PointChange> sourceChangeList, List<PointChange> targetPointChanges) {
		for (PointChange sourceChange : sourceChangeList) {
			mergePoint(targetPointChanges, sourceChange);
		}
	}

	private void mergePoint(List<PointChange> targetPointChanges, PointChange sourceChange) {
		for (ListIterator<PointChange> iter = targetPointChanges.listIterator(); iter.hasNext();) {
			PointChange targetChange = iter.next();
			if (sourceChange.getIndex().equals(targetChange.getIndex())) {
				iter.set(new PointChange(targetChange.getIndex(), sourceChange.getDiffX() + targetChange.getDiffX(), sourceChange.getDiffY() + targetChange.getDiffY()));
				return; // index already in targetList and successfully updated
			}
		}
		targetPointChanges.add(sourceChange); // index not in targetList, therefore added here
	}
}
