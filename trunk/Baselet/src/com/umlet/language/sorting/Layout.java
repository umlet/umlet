package com.umlet.language.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baselet.control.Main;
import com.baselet.element.Dimension;
import com.umlet.language.SortableElement;

public abstract class Layout {
	
	protected final int GRIDSIZE;
	protected Dimension bounds;

	public Layout() {
		GRIDSIZE = Main.getInstance().getDiagramHandler().getGridSize();
	}

	public abstract void layout(List<SortableElement> elements);
	
	public void simpleLayout(Comparator<SortableElement> comparator, List<SortableElement> elements) {
		int maxHeight = 0;
		int sumWidth = 0;
		for (SortableElement e: elements) {
			if (e.getElement().getZoomedSize().height > maxHeight) {
				maxHeight = e.getElement().getZoomedSize().height;
			}
			sumWidth += e.getElement().getZoomedSize().width;
		}
		// start with a rectangle with one row with all elements in it and determine
		// the multiplicator by solving: (x / m) / (y * m) = desired relation of width to height  
		double m = Math.sqrt(sumWidth / (0.4 * maxHeight));
		int desiredWidth = (int) (sumWidth / m);
		
		Collections.sort(elements, comparator);
		
		int rows = 1;
		int curX = GRIDSIZE; 
		int curY = GRIDSIZE;
		Dimension d = new Dimension(curX, curY);
		int maxHeightThisRow = 0;
		for (SortableElement e: elements) {
			e.getElement().setLocation(curX, curY);
			if (e.getElement().getZoomedSize().height > maxHeightThisRow) {
				maxHeightThisRow = e.getElement().getZoomedSize().height;
			}
			// determine outer x-bounds of all elements placed
			Dimension dim = e.getElement().getZoomedSize();
			if (curX + dim.width > d.width) {
				d.width = curX + e.getElement().getZoomedSize().width; 
			}
			if (curX > desiredWidth) {
				++rows;
				curY += maxHeightThisRow + GRIDSIZE;
				curX = GRIDSIZE;
				maxHeightThisRow = 0;
			} else {
				curX += e.getElement().getZoomedSize().width + GRIDSIZE;
			}
			// determine outer y-bounds of alle elements placed
			if (elements.indexOf(e) == elements.size()-1) {// element is the last one
				d.height = curY + maxHeightThisRow + (rows+1)*GRIDSIZE;
			}
		}
		d.width += GRIDSIZE;
		bounds = d;
	}
}
