package com.umlet.language.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.Main;
import com.umlet.language.SortableElement;

public abstract class Layout {
	
	private static final int GRIDSIZE = Main.getInstance().getDiagramHandler().getGridSize();

	public abstract void layout(List<SortableElement> elements);
	
	public void simpleLayout(Comparator<SortableElement> comparator, List<SortableElement> elements) {
		int maxHeight = 0;
		int sumWidth = 0;
		for (SortableElement e: elements) {
			if (e.getElement().getSize().height > maxHeight) {
				maxHeight = e.getElement().getSize().height;
			}
			sumWidth += e.getElement().getSize().width;
		}
		// start with a rectangle with one row with all elements in it and determine
		// the multiplicator by solving: (x / m) / (y * m) = desired relation of width to height  
		double m = Math.sqrt(sumWidth / (0.4 * maxHeight));
		int desiredWidth = (int) (sumWidth / m);
		
		Collections.sort(elements, comparator);
		
		int curWidth = GRIDSIZE; 
		int curHeight = GRIDSIZE;
		int maxHeightThisRow = 0;
		for (SortableElement e: elements) {
			e.getElement().setLocation(curWidth, curHeight);
			if (e.getElement().getSize().height > maxHeightThisRow) {
				maxHeightThisRow = e.getElement().getSize().height;
			}
			if (curWidth > desiredWidth) { 
				curHeight += maxHeightThisRow + GRIDSIZE;
				curWidth = GRIDSIZE;
				maxHeightThisRow = 0;
			} else {
				curWidth += e.getElement().getSize().width + GRIDSIZE;
			}
		}
	}
}
