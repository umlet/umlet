package com.umlet.language;

import java.util.Comparator;

import com.baselet.element.GridElement;

public class GridElementHeightSorter implements Comparator<GridElement> {

	@Override
	public int compare(GridElement e1, GridElement e2) {
		return -Integer.compare(e1.getSize().height, e2.getSize().height);
	}
}
