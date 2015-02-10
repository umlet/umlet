package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.control.SharedUtils;

public class StickableMap {
	public static final StickableMap EMPTY_MAP = new StickableMap();

	/**
	 * only the index of sticking points is stored here, because the position of the sticking point can change inbetween
	 * (eg: a relation with description text which moves around and needs space on any side of the relation, therefore resizing and repositioning the relation)
	 */
	Map<Stickable, List<Integer>> stickingIndexMap = new HashMap<Stickable, List<Integer>>();

	public StickableMap() {}

	public boolean equalsMap(StickableMap other) {
		return checkMapsEqual(stickingIndexMap, other.stickingIndexMap);
	}

	private static boolean checkMapsEqual(Map<Stickable, List<Integer>> mapA, Map<Stickable, List<Integer>> mapB) {
		if (!containSameElements(mapA.keySet(), mapB.keySet())) {
			return false; // keys are not equal
		}

		for (Entry<Stickable, List<Integer>> entry : mapA.entrySet()) {
			List<Integer> setA = entry.getValue();
			List<Integer> setB = mapB.get(entry.getKey());
			if (!containSameElements(setA, setB)) {
				return false; // values for this key are not equal
			}
		}

		return true; // all keys and values are equal
	}

	private static boolean containSameElements(Collection<?> setA, Collection<?> setB) {
		return setA.containsAll(setB) && setB.containsAll(setA);
	}

	public boolean isEmpty() {
		for (List<Integer> valueList : stickingIndexMap.values()) {
			if (!valueList.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void add(Stickable stickable, PointDoubleIndexed p) {
		List<Integer> points = stickingIndexMap.get(stickable);
		if (points == null) {
			points = new ArrayList<Integer>();
			stickingIndexMap.put(stickable, points);
		}
		points.add(p.getIndex());
	}

	public Set<Stickable> getStickables() {
		return stickingIndexMap.keySet();
	}

	public List<PointDoubleIndexed> getStickablePoints(Stickable stickable) {
		// get the points of all indexes which stick (must be done now and not cached, because every info of the points except the index can change!
		List<Integer> stickingIndexes = stickingIndexMap.get(stickable);
		List<PointDoubleIndexed> returnList = new ArrayList<PointDoubleIndexed>();
		for (PointDoubleIndexed p : stickable.getStickablePoints()) {
			if (stickingIndexes.contains(p.getIndex())) {
				returnList.add(p);
			}
		}
		return returnList;
	}

	public void setStickablePoints(Stickable stickable, List<PointDoubleIndexed> updatedChangedPoints) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (PointDoubleIndexed p : updatedChangedPoints) {
			indexList.add(p.getIndex());
		}
		stickingIndexMap.put(stickable, indexList);
	}

	@Override
	public String toString() {
		return "StickableMap [map=" + SharedUtils.mapToString("\n", ",", stickingIndexMap) + "]";
	}

}
