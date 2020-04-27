package com.baselet.generator.sorting;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class HeightLayout extends Layout {

	@Override
	public void layout(List<SortableElement> elements) {
		super.simpleLayout(new HeightSorter(), elements);
	}

	private static class HeightSorter implements Comparator<SortableElement>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(SortableElement e1, SortableElement e2) {
			return e2.getElement().getRectangle().height - e1.getElement().getRectangle().height;
		}
	}
}
