package com.umlet.language.sorting;

import java.util.Comparator;

import com.umlet.language.SortableElement;

public class HeightSorter implements Comparator<SortableElement> {

	@Override
	public int compare(SortableElement e1, SortableElement e2) {
		return e2.getElement().getSize().height - e1.getElement().getSize().height;
	}
}
