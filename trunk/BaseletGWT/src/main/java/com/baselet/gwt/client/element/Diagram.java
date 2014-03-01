package com.baselet.gwt.client.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.element.HasPanelAttributes;
import com.baselet.gui.AutocompletionText;
import com.baselet.gwt.client.view.SelectorNew.HasGridElements;
import com.umlet.element.experimental.element.uml.relation.Relation;

public class Diagram implements HasPanelAttributes, HasGridElements {

	private static final Comparator<GridElement> LAYER_COMPARATOR = new Comparator<GridElement>() {
		@Override
		public int compare(GridElement o1, GridElement o2) {
			return o1.getLayer().compareTo(o2.getLayer());
		}};

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

		public void moveGridElements(int diffX, int diffY, boolean firstDrag, List<GridElement> elements) {
			// only stickables which are not moved themselves must be used for sticking-checks (otherwise a stickable can be moved itself and by "sticking" to a stickingpolygon afterwards)
			List<Relation> stickablesToCheck = getRelations();
			for (Iterator<Relation> iter = stickablesToCheck.iterator(); iter.hasNext();) {
				if (elements.contains(iter.next())) {
					iter.remove();
				}
			}
			for (GridElement ge : elements) {
				ge.setLocationDifference(diffX, diffY, firstDrag, stickablesToCheck); //uses setLocationDifference() instead of drag() to avoid special handling (eg: from Relations)
			}
		}

		public List<GridElement> getGridElementsSortedByLayer() {
			Collections.sort(gridElements, LAYER_COMPARATOR);
			return getGridElements();
		}
		
		@Override
		public void setPanelAttributes(String panelAttributes) {
			this.helpText = panelAttributes;
		}

		@Override
		public String getPanelAttributes() {
			return helpText;
		}

		@Override
		public List<AutocompletionText> getAutocompletionList() {
			return Collections.<AutocompletionText>emptyList();
		}

}
