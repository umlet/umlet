package com.baselet.gwt.client.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.config.SharedConfig;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.relation.Relation;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.AutocompletionText;

public class DiagramGwt implements Diagram {

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
	private final List<GridElement> gridElements;

	public DiagramGwt(List<GridElement> gridElements) {
		this(null, gridElements);
	}

	public DiagramGwt(String helpText, List<GridElement> gridElements) {
		super();
		this.helpText = helpText;
		this.gridElements = gridElements;
	}

	@Override
	public List<GridElement> getGridElements() {
		return gridElements;
	}

	@Override
	public List<Stickable> getStickables() {
		List<Stickable> returnList = new ArrayList<Stickable>();
		for (GridElement ge : gridElements) {
			if (ge instanceof Relation) {
				returnList.add((Relation) ge);
			}
		}
		return returnList;
	}

	@Override
	public StickableMap getStickables(GridElement draggedElement) {
		return getStickables(draggedElement, Collections.<GridElement> emptyList());
	}

	@Override
	public StickableMap getStickables(GridElement draggedElement, Collection<GridElement> excludeList) {
		if (!SharedConfig.getInstance().isStickingEnabled()) {
			return StickableMap.EMPTY_MAP;
		}
		List<Stickable> stickables = getStickables();
		stickables.removeAll(excludeList);

		StickingPolygon stickingBorder = draggedElement.generateStickingBorder();
		StickableMap stickingStickables = Stickables.getStickingPointsWhichAreConnectedToStickingPolygon(stickingBorder, stickables);
		return stickingStickables;
	}

	@Override
	public List<GridElement> getGridElementsByLayerLowestToHighest() {
		return getGridElementsByLayer(true);
	}

	@Override
	public List<GridElement> getGridElementsByLayer(boolean ascending) {
		ArrayList<GridElement> list = new ArrayList<GridElement>(gridElements);
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
