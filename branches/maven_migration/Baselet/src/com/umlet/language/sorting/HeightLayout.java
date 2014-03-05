package com.umlet.language.sorting;

import java.util.Comparator;
import java.util.List;

import com.umlet.language.SortableElement;

public class HeightLayout extends Layout {
	
	@Override
	public void layout(List<SortableElement> elements) {
		super.simpleLayout(new HeightSorter(), elements);
	}
	
	private class HeightSorter implements Comparator<SortableElement> {

		@Override
		public int compare(SortableElement e1, SortableElement e2) {
			return e2.getElement().getRectangle().height - e1.getElement().getRectangle().height;
		}
	}
}
