package com.baselet.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.GridElement;

public abstract class Selector {

	private void selectHelper(boolean applyAfterAction, Collection<GridElement> elements) {
		for (GridElement e : expand(elements)) {
			if (!getSelectedElements().contains(e)) {
				getSelectedElements().add(e);
				doAfterSelect(e);
			}
		}
		if (applyAfterAction) {
			doAfterSelectionChanged();
		}
	}

	private void deselectHelper(boolean applyAfterAction, Collection<GridElement> elements) {
		for (GridElement e : expand(elements)) {
			Iterator<GridElement> iter = getSelectedElements().iterator();
			while (iter.hasNext()) {
				if (iter.next().equals(e)) {
					iter.remove();
					doAfterDeselect(e);
				}
			}
		}
		if (applyAfterAction) {
			doAfterSelectionChanged();
		}
	}

	private List<GridElement> expand(Collection<GridElement> elements) {
		Map<String, Collection<GridElement>> map = Selector.createGroupElementMap(getAllElements());
		List<GridElement> elemenentsWithGroups = new ArrayList<GridElement>();
		// add grouped elements BEFORE the really selected elements, to make sure the last element stays the same (because its content will be shown in the property panel)
		for (GridElement e : elements) {
			String groupValue = GroupFacet.getElementGroupValSafe(e.getGroup());
			if (groupValue != null) {
				Collection<GridElement> set = map.get(groupValue);
				if (set != null) { // TODO set can be null in standalone version because getAllElements is empty (eg if grouped elements are selected when diagram is closed)
					for (GridElement g : set) {
						if (g != e) {
							elemenentsWithGroups.add(g);
						}
					}
				}
			}
		}
		elemenentsWithGroups.addAll(elements);
		return elemenentsWithGroups;
	}

	public void select(GridElement element) {
		select(Arrays.asList(element));
	}

	public void deselect(GridElement element) {
		deselect(Arrays.asList(element));
	}

	public abstract List<GridElement> getSelectedElements();

	public boolean isSelected(GridElement ge) {
		return getSelectedElements().contains(ge);
	}

	public boolean isSelectedOnly(GridElement ge) {
		return getSelectedElements().size() == 1 && isSelected(ge);
	}

	public void selectOnly(GridElement element) {
		selectOnly(Arrays.asList(element));
	}

	public void selectOnly(Collection<GridElement> elements) {
		deselectHelper(false, getSelectedElements());
		selectHelper(true, elements);

	}

	public void select(Collection<GridElement> elements) {
		selectHelper(true, elements);
	}

	public void deselect(Collection<GridElement> elements) {
		deselectHelper(true, elements);
	}

	public void deselectAll() {
		deselect(getSelectedElements());
	}

	public void deselectAllWithoutAfterAction() {
		deselectHelper(false, getSelectedElements());
	}

	public void doAfterDeselect(GridElement e) {
		e.getComponent().afterModelUpdate();
	}

	public void doAfterSelectionChanged() {
		// hook method
	}

	public void doAfterSelect(GridElement e) {
		e.getComponent().afterModelUpdate();
	}

	public void moveToLastPosInList(GridElement element) {
		List<GridElement> elements = getSelectedElements();
		elements.remove(element);
		elements.add(element);
	}

	public String getUnusedGroup() {
		return getUnusedGroupId(createGroupElementMap(getAllElements()).keySet());
	}

	public abstract List<GridElement> getAllElements();

	public static void replaceGroupsWithNewGroups(Collection<GridElement> elements, Selector selector) {
		Set<String> usedIds = new HashSet<String>(createGroupElementMap(selector.getAllElements()).keySet());
		Map<String, Collection<GridElement>> groupedElements = createGroupElementMap(elements);
		for (Entry<String, Collection<GridElement>> entry : groupedElements.entrySet()) {
			String unusedId = getUnusedGroupId(usedIds);
			usedIds.add(unusedId);
			for (GridElement e : entry.getValue()) {
				e.setProperty(GroupFacet.KEY, unusedId);
			}
		}
	}

	public static String getUnusedGroupId(Collection<String> usedGroups) {
		String newGroup;
		while (true) {
			newGroup = GroupFacet.nextDefaultKey();
			if (usedGroups.contains(newGroup) == false) {
				break;
			}
		}
		return newGroup;
	}

	public static Map<String, Collection<GridElement>> createGroupElementMap(Collection<GridElement> elements) {
		Map<String, Collection<GridElement>> returnmap = new HashMap<String, Collection<GridElement>>();
		String groupValue;
		for (GridElement e : elements) {
			groupValue = GroupFacet.getElementGroupValSafe(e.getGroup());
			if (groupValue != null) {
				Collection<GridElement> elementsWithGroup = returnmap.get(groupValue);
				if (elementsWithGroup == null) {
					elementsWithGroup = new ArrayList<GridElement>();
					returnmap.put(groupValue, elementsWithGroup);
				}
				elementsWithGroup.add(e);
			}
		}
		return returnmap;
	}
}
