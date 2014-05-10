package com.baselet.gwt.client.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.SharedConstants;
import com.baselet.element.GridElement;
import com.baselet.element.HasPanelAttributes;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.elementnew.element.uml.relation.Relation;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.view.SelectorNew.HasGridElements;

public class Diagram implements HasPanelAttributes, HasGridElements {

	private static final Comparator<GridElement> LAYER_COMPARATOR_ASCENDING = new Comparator<GridElement>() {
		@Override
		public int compare(GridElement o1, GridElement o2) {
			return o1.getLayer().compareTo(o2.getLayer());
		}
	};

	private static final Comparator<GridElement> LAYER_COMPARATOR_DESCENDING = new Comparator<GridElement>() {
		@Override
		public int compare(GridElement o1, GridElement o2) {
			return o2.getLayer().compareTo(o1.getLayer());
		}
	};

	private String helpText;
	private List<GridElement> gridElements;

	public Diagram(List<GridElement> gridElements) {
		this(null, gridElements);
	}

	public Diagram(String helpText, List<GridElement> gridElements) {
		super();
		this.helpText = helpText;
		this.gridElements = gridElements;
	}

	@Override
	public List<GridElement> getGridElements() {
		return gridElements;
	}

	public List<Relation> getRelations() {
		List<Relation> returnList = new ArrayList<Relation>();
		for (GridElement ge : gridElements) {
			if (ge instanceof Relation) {
				returnList.add((Relation) ge);
			}
		}
		return returnList;
	}

	public StickableMap getStickables(GridElement draggedElement) {
		return getStickables(draggedElement, Collections.<GridElement> emptyList());
	}

	public StickableMap getStickables(GridElement draggedElement, Collection<GridElement> excludeList) {
		if (!SharedConstants.stickingEnabled) {
			return StickableMap.EMPTY_MAP;
		}
		List<Relation> stickables = getRelations();
		stickables.removeAll(excludeList);

		StickingPolygon stickingBorder = draggedElement.generateStickingBorder(draggedElement.getRectangle());
		StickableMap stickingStickables = Stickables.getStickingPointsWhichAreConnectedToStickingPolygon(stickingBorder, stickables, SharedConstants.DEFAULT_GRID_SIZE);
		return stickingStickables;
	}

	public List<GridElement> getGridElementsByLayerLowestToHighest() {
		return getGridElementsByLayer(true);
	}

	public List<GridElement> getGridElementsByLayer(boolean ascending) {
		ArrayList<GridElement> list = new ArrayList<>(gridElements);
		if (ascending) {
			Collections.sort(list, LAYER_COMPARATOR_ASCENDING);
		}
		else {
			Collections.sort(list, LAYER_COMPARATOR_DESCENDING);
		}
		return list;
	}

	@Override
	public void setPanelAttributes(String panelAttributes) {
		helpText = panelAttributes;
	}

	@Override
	public String getPanelAttributes() {
		return helpText;
	}

	@Override
	public List<AutocompletionText> getAutocompletionList() {
		return Collections.<AutocompletionText> emptyList();
	}

}
