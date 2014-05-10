package com.baselet.element.sticking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.control.SharedUtils;
import com.baselet.elementnew.element.uml.relation.PointDoubleIndexed;

public class StickableMap {
	public static final StickableMap EMPTY_MAP = new StickableMap();

	Map<Stickable, List<PointDoubleIndexed>> map = new HashMap<Stickable, List<PointDoubleIndexed>>();

	public StickableMap() {}

	public boolean equalsMap(StickableMap other) {
		return checkMapsEqual(map, other.map);
	}

	private static boolean checkMapsEqual(Map<Stickable, List<PointDoubleIndexed>> mapA, Map<Stickable, List<PointDoubleIndexed>> mapB) {
		if (!containSameElements(mapA.keySet(), mapB.keySet())) return false; // keys are not equal

		for (Entry<Stickable, List<PointDoubleIndexed>> entry : mapA.entrySet()) {
			List<PointDoubleIndexed> setA = entry.getValue();
			List<PointDoubleIndexed> setB = mapB.get(entry.getKey());
			if (!containSameElements(setA, setB)) return false; // values for this key are not equal
		}

		return true; // all keys and values are equal
	}

	private static boolean containSameElements(Collection<?> setA, Collection<?> setB) {
		return setA.containsAll(setB) && setB.containsAll(setA);
	}

	public boolean isEmpty() {
		for (List<PointDoubleIndexed> valueList : map.values()) {
			if (!valueList.isEmpty()) return false;
		}
		return true;
	}

	public void add(Stickable stickable, PointDoubleIndexed p) {
		List<PointDoubleIndexed> points = map.get(stickable);
		if (points == null) {
			points = new ArrayList<PointDoubleIndexed>();
			map.put(stickable, points);
		}
		points.add(p);
	}

	public Set<Stickable> getStickables() {
		return map.keySet();
	}

	public List<PointDoubleIndexed> getStickablePoints(Stickable stickable) {
		return map.get(stickable);
	}

	public void setStickablePoints(Stickable stickable, List<PointDoubleIndexed> updatedChangedPoints) {
		map.put(stickable, updatedChangedPoints);
	}

	@Override
	public String toString() {
		return "StickableMap [map=" + SharedUtils.mapToString("\n", ",", map) + "]";
	}

}
