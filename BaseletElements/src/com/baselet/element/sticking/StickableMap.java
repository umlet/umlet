package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.elementnew.element.uml.relation.PointDoubleHolder;


public class StickableMap {
	public static final StickableMap EMPTY_MAP = new StickableMap();

	Map<Stickable, List<PointDoubleHolder>> map = new HashMap<Stickable, List<PointDoubleHolder>>();
	
	public StickableMap() {
	}

	public boolean equalsMap(StickableMap other) {
		return checkMapsEqual(this.map, other.map);
	}

	private static boolean checkMapsEqual(Map<Stickable, List<PointDoubleHolder>> mapA, Map<Stickable, List<PointDoubleHolder>> mapB) {
		if (!containSameElements(mapA.keySet(), mapB.keySet())) return false; // keys are not equal

		for (Entry<Stickable, List<PointDoubleHolder>> entry : mapA.entrySet()) {
			List<PointDoubleHolder> setA = entry.getValue();
			List<PointDoubleHolder> setB = mapB.get(entry.getKey());
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
		for (List<PointDoubleHolder> valueList : map.values()) {
			if (!valueList.isEmpty()) return false;
		}
		return true;
	}

	public void add(Stickable stickable, PointDoubleHolder p) {
		List<PointDoubleHolder> points = map.get(stickable);
		if (points == null) {
			points = new ArrayList<PointDoubleHolder>();
			map.put(stickable, points);
		}
		points.add(p);
	}

	public Set<Stickable> getStickables() {
		return map.keySet();
	}
	
	public List<PointDoubleHolder> getStickablePoints(Stickable stickable) {
		return map.get(stickable);
	}

	public void setStickablePoints(Stickable stickable, List<PointDoubleHolder> updatedChangedPoints) {
		map.put(stickable, updatedChangedPoints);
	}
}
