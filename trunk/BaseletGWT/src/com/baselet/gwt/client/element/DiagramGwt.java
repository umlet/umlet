package com.baselet.gwt.client.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.constants.SharedConstants;
import com.baselet.diagram.Diagram;
import com.baselet.element.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.elementnew.element.uml.relation.Relation;
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

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getGridElements() */
	@Override
	public List<GridElement> getGridElements() {
		return gridElements;
	}

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getRelations() */
	@Override
	public List<Relation> getRelations() {
		List<Relation> returnList = new ArrayList<Relation>();
		for (GridElement ge : gridElements) {
			if (ge instanceof Relation) {
				returnList.add((Relation) ge);
			}
		}
		return returnList;
	}

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getStickables(com.baselet.element.GridElement) */
	@Override
	public StickableMap getStickables(GridElement draggedElement) {
		return getStickables(draggedElement, Collections.<GridElement> emptyList());
	}

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getStickables(com.baselet.element.GridElement, java.util.Collection) */
	@Override
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

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getGridElementsByLayerLowestToHighest() */
	@Override
	public List<GridElement> getGridElementsByLayerLowestToHighest() {
		return getGridElementsByLayer(true);
	}

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getGridElementsByLayer(boolean) */
	@Override
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

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#setPanelAttributes(java.lang.String) */
	@Override
	public void setPanelAttributes(String panelAttributes) {
		helpText = panelAttributes;
	}

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getPanelAttributes() */
	@Override
	public String getPanelAttributes() {
		return helpText;
	}

	/* (non-Javadoc)
	 * @see com.baselet.gwt.client.element.Diagramx#getAutocompletionList() */
	@Override
	public List<AutocompletionText> getAutocompletionList() {
		return Collections.<AutocompletionText> emptyList();
	}

}
