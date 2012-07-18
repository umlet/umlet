package com.umlet.language;

import java.util.Comparator;

import com.baselet.element.GridElement;

public class GridElementHeightSorter implements Comparator<GridElement> {

	@Override
	public int compare(GridElement e1, GridElement e2) {
		return e2.getSize().height - e1.getSize().height;
	}
}
