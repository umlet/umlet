package com.baselet.gwt.client.element;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.google.gwt.canvas.client.Canvas;
import com.umlet.element.experimental.ElementId;

public class Diagram {

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

	public String getHelpText() {
		return helpText;
	}

	public List<GridElement> getGridElements() {
		return gridElements;
	}

	public void drawEmptyInfoText(Canvas elementCanvas) {
		double elWidth = 440;
		double elHeight = 80;
		double elXPos = elementCanvas.getCoordinateSpaceWidth()/2 - elWidth/2;
		double elYPos = elementCanvas.getCoordinateSpaceHeight()/2 - elHeight;
		GridElement emptyElement = ElementFactory.create(ElementId.Text, new Rectangle(elXPos, elYPos, elWidth, elHeight), "halign=center\nDouble-click on an element to add it to the diagram\n\nImport uxf Files using the Menu \"Import\" or simply drag them into the diagram\n\nSave diagrams persistent in browser storage using the \"Save\" menu", "");
		((GwtComponent) emptyElement.getComponent()).drawOn(elementCanvas.getContext2d(), false);

	}

	public Collection<GridElement> getGridElementsSorted() {
		Collections.sort(gridElements, LAYER_COMPARATOR);
		return getGridElements();
	}

}
