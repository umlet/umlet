package com.baselet.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.umlet.element.experimental.facets.defaults.GroupFacet;

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
		Map<String, Set<GridElement>> map = buildGroupMap();
		List<GridElement> elemenentsWithGroups = new ArrayList<GridElement>();
		elemenentsWithGroups.addAll(elements);
		for (GridElement e : elements) {
			if (!e.getGroup().equals(GroupFacet.DEFAULT_VALUE)) {
				Set<GridElement> set = map.get(e.getGroup());
				if (set != null) { // TODO set can be null in standalone version because getAllElements is empty (eg if grouped elements are selected when diagram is closed)
					for (GridElement g : set) {
						if (g != e) {
							elemenentsWithGroups.add(g);
						}
					}
				}
			}
		}
		return elemenentsWithGroups;
	}

	private Map<String, Set<GridElement>> buildGroupMap() {
		Map<String, Set<GridElement>> groupMap = new HashMap<String, Set<GridElement>>();
		for (GridElement e : getAllElements()) {
			if (!e.getGroup().equals(GroupFacet.DEFAULT_VALUE)) {
				Set<GridElement> set = groupMap.get(e.getGroup());
				if (set == null) {
					set = new HashSet<GridElement>();
					groupMap.put(e.getGroup(), set);
				}
				set.add(e);
			}
		}
		return groupMap;
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
		//hook method
	}

	public void doAfterSelect(GridElement e) {
		e.getComponent().afterModelUpdate();
	}

	public void moveToLastPosInList(GridElement element) {
		List<GridElement> elements = getSelectedElements();
		elements.remove(element);
		elements.add(element);
	}

	public abstract List<GridElement> getAllElements();
}
