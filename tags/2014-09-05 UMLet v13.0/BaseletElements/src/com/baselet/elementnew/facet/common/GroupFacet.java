package com.baselet.elementnew.facet.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.KeyValueFacet;

public class GroupFacet extends KeyValueFacet {

	public static GroupFacet INSTANCE = new GroupFacet();

	private GroupFacet() {}

	public static final String KEY = "group";

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(KEY, false, "1", "grouped elements are selected at once");
	}

	@Override
	public void handleValue(String value, DrawHandler drawer, PropertiesParserState state) {
		try {
			state.setFacetResponse(GroupFacet.class, Integer.valueOf(value));
		} catch (NumberFormatException e) {
			throw new StyleException("value must be a positive or negative integer");
		}
	}

	public static Map<Integer, Collection<GridElement>> createGroupElementMap(Collection<GridElement> elements) {
		Map<Integer, Collection<GridElement>> returnmap = new HashMap<Integer, Collection<GridElement>>();
		for (GridElement e : elements) {
			if (e.getGroup() != null) {
				Collection<GridElement> elementsWithGroup = returnmap.get(e.getGroup());
				if (elementsWithGroup == null) {
					elementsWithGroup = new ArrayList<GridElement>();
					returnmap.put(e.getGroup(), elementsWithGroup);
				}
				elementsWithGroup.add(e);
			}
		}
		return returnmap;
	}

	public static void replaceGroupsWithNewGroups(Collection<GridElement> elements, Selector selector) {
		Set<Integer> usedIds = new HashSet<Integer>(createGroupElementMap(selector.getAllElements()).keySet());
		Map<Integer, Collection<GridElement>> groupedElements = createGroupElementMap(elements);
		for (Entry<Integer, Collection<GridElement>> entry : groupedElements.entrySet()) {
			Integer unusedId = getUnusedGroupId(usedIds);
			usedIds.add(unusedId);
			for (GridElement e : entry.getValue()) {
				e.setProperty(KEY, unusedId);
			}
		}
	}

	public static Integer getUnusedGroupId(Collection<Integer> usedGroups) {
		Integer newGroup;
		if (usedGroups.isEmpty()) {
			newGroup = 1;
		}
		else {
			newGroup = Collections.max(usedGroups) + 1;
		}
		return newGroup;
	}
}
