package com.umlet.language;

import java.util.Comparator;

public class ClassElementHeightSorter implements Comparator<ClassElement> {

	@Override
	public int compare(ClassElement e1, ClassElement e2) {
		return e2.getElement().getSize().height - e1.getElement().getSize().height;
	}
}
