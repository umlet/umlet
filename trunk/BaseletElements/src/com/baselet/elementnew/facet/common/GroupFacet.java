package com.baselet.elementnew.facet.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.baselet.elementnew.facet.KeyValueFacet;
import com.baselet.elementnew.facet.PropertiesParserState;

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

	public static void replaceGroupsWithNewGroups(Collection<GridElement> elements, Selector selector) {
		Set<Integer> usedIds = new HashSet<Integer>(Selector.createGroupElementMap(selector.getAllElements()).keySet());
		Map<Integer, Collection<GridElement>> groupedElements = Selector.createGroupElementMap(elements);
		for (Entry<Integer, Collection<GridElement>> entry : groupedElements.entrySet()) {
			Integer unusedId = Selector.getUnusedGroupId(usedIds);
			usedIds.add(unusedId);
			for (GridElement e : entry.getValue()) {
				e.setProperty(KEY, unusedId);
			}
		}
	}
}
